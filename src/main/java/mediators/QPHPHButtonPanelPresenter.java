/**
 * @author CSE 110 - Team 12
 */
package mediators;
import java.io.*;
import java.util.ArrayList;
import javax.mail.Session;
import javax.swing.*;
import api.MongoDB;
import interfaces.*;
import listeners.*;
import mainframe.*;
import processing.*;

/**
 * Main mediator for app
 * Creates instances of APIs, listeners, interfaces, mainframe, mediators, processing, and server
 *
 */
public class QPHPHButtonPanelPresenter implements ButtonObserver, PanelObserver, MediatorSubject{

	//Mediator
	ArrayList<MediatorObserver> parentFrames;

	//API instances
	Recorder recorder;
	WhisperInterface WhisperSession;
	ChatGPTInterface ChatGPTSession;
	ServerInterface ServerSession;
	ErrorMessagesInterface ErrorMessages;
	MongoDB MongoDBSession;

	//References to instantiated UI elements and listeners
	ArrayList<ButtonSubject> allButtons = new ArrayList<ButtonSubject>();
	EmailSetupFrame ef;
	AppFrame af;
	QuestionPanel qp;
	PromptHistory ph;
	EmailSetupPanel ep;

	//Email util
	TLSEmail tlsEmail;
	EmailUtil emailUtil;

	/**
	 * Constructor used for mediator
	 * 
	 * @param esFrame
	 * @param appFrame
	 * @param recorder
	 * @param WhisperSession
	 * @param ChatGPTSession
	 * @param ServerSession
	 * @param ErrorMessages
	 * @param MongoDBSession
	 * @param tlsEmail
	 * @param emailUtil
	 */
	public QPHPHButtonPanelPresenter(EmailSetupFrame esFrame, AppFrame appFrame, Recorder recorder, WhisperInterface WhisperSession, 
			ChatGPTInterface ChatGPTSession, ServerInterface ServerSession, ErrorMessagesInterface ErrorMessages, MongoDB MongoDBSession, TLSEmail tlsEmail, 
			EmailUtil emailUtil){

		//Sets created panels that mediator uses
		ef = esFrame;
		af = appFrame;
		qp = appFrame.getQuestionPanel();
		ph = appFrame.getPromptHistory();
		ep = ef.getSetupPanel();

		//Sets APIs that mediator uses.
		this.recorder = recorder;
		this.WhisperSession = WhisperSession;
		this.ChatGPTSession = ChatGPTSession;
		this.ServerSession = ServerSession;
		this.ErrorMessages = ErrorMessages;
		this.MongoDBSession = MongoDBSession;

		//Email util
		this.tlsEmail = tlsEmail;
		this.emailUtil = emailUtil;

		//Register listeners that mediator observes and then the observers of the mediator
		this.parentFrames = new ArrayList<MediatorObserver>();
		ArrayList<ButtonSubject> allListeners = getListeners();
		for (ButtonSubject listener : allListeners){
			listener.registerObserver(this);
		}
		qp.registerObserver(this);
		ph.registerObserver(this);
		this.registerObserver(appFrame);
		this.registerObserver(esFrame);
		this.MongoDBSession.registerObserver(this);
	}

	//////////////////////////////////BUTTON OBSERVER METHODS///////////////////////////////////

	/**
	 * When user first opens the appframe after login
	 */
	@Override
	public void onStart() {
		//pulls data from MongoDB server
		ArrayList<Entry> savedPromptHistory = this.MongoDBSession.retrieveSavedPromptHistory();
		//populates prompt history with entries and posts to local server.
		for (Entry entry : savedPromptHistory){
			if (entry != null){
				ph.onNewEntry(entry);
				this.ServerSession.postToServer(entry);
			}
		}
	}

	/**
	 * When start button is clicked
	 * Can be used to start recording or stop recording
	 */
	@Override
	public void onStartStop(boolean startedRecording) {
		
		//starts recording
		if (startedRecording){
			recorder.startRecording();
			qp.startedRecording();

		//stops recording
		} else {
			File audioFile = recorder.stopRecording();
			qp.stoppedRecording();
			String question = null;
			try {
				this.WhisperSession.setWhisperFile(audioFile);
				question = WhisperSession.getQuestionText();
			} catch (Exception e) {
				e.printStackTrace();
			}
			getResultsFromAudio(question);
		}
	}

	/**
	 * Changes UI when new entry clicked in prompt history
	 * String prompt is formatted as "Command: <Prompt>"
	 */
	@Override
	public void onListChange(String prompt) {
		String answer = this.ServerSession.getFromServer(prompt);
		qp.onListChange(prompt, answer);
	}

	/**
	 * When user closes the appframe
	 * 		Confirms they want to close
	 * 		saves new entries to server
	 */
	@Override
	public void onClose() {
		
		boolean confirmation = ErrorMessages.confirmClosing();
		
		if (confirmation){
			
			ArrayList<Entry> savedHistory = new ArrayList<Entry>();
			ListModel<String> modelPromptHistory = ph.getListModel();
			
			//save entries from prompt history into MongoDB
			for (int i = 0; i < modelPromptHistory.getSize(); i++){
				ArrayList<String> deconstructedEntry = 
						parseCommand(modelPromptHistory.getElementAt(i));
				
				Entry entry = convertToEntry(deconstructedEntry);
				savedHistory.add(entry);
			}
			
			this.MongoDBSession.updateSavedPromptHistory(savedHistory);       
			af.dispose();     
		}        
	}

	//////////////////////////LOGIC HELPER METHODS//////////////////////////
	
	/**
	 * Handles commands given to app
	 * 
	 * @param String text returned from Whisper
	 */
	private void getResultsFromAudio(String question) {
		
		//gets result
		ArrayList<String> result = parseCommand(question);
		if (result != null){
			String command = result.get(0);
			String prompt = result.get(1);

			//Case 1 where command is a question
			if (command.equalsIgnoreCase("Question")) {            
				onQuestionCommand(command, prompt);
			}

			//Case 2 where command is email setup
			if (command.equalsIgnoreCase("Setup Email")) {
				notifyObservers();
			}

			//Case 3 where command is delete
			if (command.equalsIgnoreCase("Delete Prompt")){
				onDelete();
			}

			//Case 4 where command is clear all
			if (command.equalsIgnoreCase("Clear All")){
				onClear();
			}

			//Case 5 where command is to create email draft
			if (command.equalsIgnoreCase("Create email")) {
				onCreateEmail(command, prompt);
			}
			//Case 6 where command is send email
			if (command.equalsIgnoreCase("Send Email") && prompt != null) {
				onSendEmail(command, prompt);
			}
			
		//invalid input detected
		} else{
			qp.InvalidInputDetected(question);
		}
	}
	
	/**
	 * Create new Entry, update UI, and add it to server
	 * 
	 * @param command
	 * @param prompt
	 * @param answer
	 */
	private void onNewEntry(String command, String prompt, String answer) {
		Entry entry = new Entry(command, prompt, answer);
		qp.onNewEntry(entry);
		ph.onNewEntry(entry);
		this.ServerSession.postToServer(entry);
	}
	
	/**
	 * Stop button is clicked and question command detected
	 * Gets results for question, adds to server, and updates UI
	 * 
	 * @param command Question
	 * @param prompt user's question
	 */
	private void onQuestionCommand(String command, String prompt){
		//ask question to chatGPT    
		try {
			this.ChatGPTSession.askChatGPT(prompt);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		} 

		String answer = this.ChatGPTSession.getAnswer().trim();
		
		onNewEntry(command, prompt, answer); 
	}

	/**
	 * Stop button is clicked and delete prompt command detected
	 * Deletes prompt from UI and server
	 */
	private void onDelete(){
		int index = ph.getSelectedIndex();
		if (index != -1){
			String title = ph.getTitle(index);
			this.ServerSession.deleteFromServer(title);
			qp.onDelete();
			ph.removePH(index);
		}
	}
	
	/**
	 * Stop button is clicked and clear all command detected
	 * Clears prompts from UI and server
	 */
	private void onClear(){
		//iterate through all elements in Prompt History
		for (int index = 0 ; index < ph.getPHSize(); index++){
			String title = ph.getTitle(index);
			this.ServerSession.deleteFromServer(title);
		}
		ph.resetPH();
		qp.onDelete();
	}

	/**
	 * Stop button is clicked and create email command detected
	 * Has ChatGPT create email, updates UI and server with created Entry
	 * 
	 * @param command
	 * @param prompt
	 */
	private void onCreateEmail(String command, String prompt) {
		String displayName = getDisplayName();
		//create email with chatGPT    
		String ask = "Create an email draft twhose body is '" + prompt + 
				"' and ends in 'Best regards, " + displayName + "'";
		try {
			this.ChatGPTSession.askChatGPT(ask);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		} 

		String answer = this.ChatGPTSession.getAnswer().trim();
		
		onNewEntry(command, prompt, answer); 
	}

	/**
	 * Helper for onCreateEmail
	 * 
	 * @return user's display name
	 */
	private String getDisplayName() {
		if (this.MongoDBSession.getDisplayNameEmail() != null) {
			return this.MongoDBSession.getDisplayNameEmail();
		} else {
			return "Default User";
		}
	}

	/**
	 * Stop button has been clicked and send email command detected
	 * 
	 * @param command
	 * @param prompt
	 */
	private void onSendEmail(String command, String prompt) {
		String start = "to ";
		String start2 = "To ";
				
		//can only send email if created email is selected in PH
		if (this.qp.getQuestion().startsWith("Create Email")) {
			
			//check if prompt mentions "to <email>"
			if (prompt.startsWith(start) || prompt.startsWith(start2)) {
				
				String firstName;
				
				//can only send email if email setup has been configured
				if ((firstName = this.MongoDBSession.getFirstName()) != null ) {
					send(command, prompt, firstName);
				} else {
					ErrorMessages.showErrorMessage("Use Setup Email command before sending emails.");
				}
			} else {
				ErrorMessages.showErrorMessage("Please end command in 'TO <EMAIL>");
			}
		} else {
			ErrorMessages.showErrorMessage("Must select created email before sending email.");
		}
	}

	/**
	 * Method to send email 
	 * 
	 * @param command
	 * @param prompt
	 * @param firstName
	 */
	private void send(String command, String prompt, String firstName) {
		Entry entry;
		
		//configure toEmail from input
		prompt = configurePrompt(prompt);
		
		//get fields for email
		String toEmail = configureToEmail(prompt);
		String lastName = this.MongoDBSession.getLastName();
		String emailBody = this.qp.getAnswer();
		String fromEmail = this.MongoDBSession.getFromEmail();
		String subject = "New Message from " + firstName + " " + lastName +
				" on SayItAssistant2";
		
		//configures TLSUtil for session
		configureEmailSession();
		Session session = this.tlsEmail.startEmailSession();

		
		//if email sent, update UI with success message
		if (this.emailUtil.sendEmail(session, toEmail,fromEmail,subject, emailBody)) {
			entry = new Entry(command, prompt, "Email successfully sent.");
			qp.onNewEntry(entry);

		//if email not sent, update UI with error message
		} else {
			entry = new Entry(command, prompt, "Email not sent. SMTP Host: " +
							  this.MongoDBSession.getSMTPHost());
			qp.onNewEntry(entry);
		}
	}

	/**
	 * Helper method for onSendEmail
	 * 
	 * @param prompt
	 * @return email user wants to send to
	 */
	private String configureToEmail(String prompt) {
		String toEmail = prompt.replace("to ", "");
		toEmail = toEmail.trim();
		return toEmail;
	}

	/**
	 * Helper method for onSendEmail
	 * 
	 * @param prompt
	 * @return configured prompt
	 */
	private String configurePrompt(String prompt) {
		prompt = prompt.replace(" at ", "@");
		prompt = prompt.replace(" dot ", ".");
		prompt = prompt.toLowerCase();
		//removing trailing period
		if (prompt.charAt(prompt.length()-1) == '.') {
			prompt = prompt.substring(0, prompt.length()-1);
		}
		return prompt;
	}

	/**
	 * Helper method for onSendEmail
	 * Configures TLSUtil for sending email
	 */
	private void configureEmailSession() {
		this.tlsEmail.setFromEmail(this.MongoDBSession.getFromEmail());
		this.tlsEmail.setSMTPHost(this.MongoDBSession.getSMTPHost());
		this.tlsEmail.setTLSPort(this.MongoDBSession.getTLSPort());
		this.tlsEmail.setPassword(this.MongoDBSession.getPassword());
	}

	/**
	 * Setup button is clicked: Setup Email is completed
	 * Email info saved to MongoDB
	 * 
	 */
	public void onEmailSetup(){
		if (!ep.checkAllFieldsFilled()){
			ErrorMessages.showErrorMessage("Fill up all fields");
		} else {
			ef.setVisible(false);
			ArrayList<String> fields = ep.getAllFields();
			this.MongoDBSession.setupEmail(fields);
		}
	}

	/**
	 * Cancel button is clicked on email setup screen
	 * Results are not saved and screen closes
	 */
	public void onCancel() {
		ef.setVisible(false);
	}

	/**
	 * Helper method to convert recordings to entries
	 * 
	 * @param deconstructedEntry
	 * @return Entry from deconstructed entry
	 */
	public Entry convertToEntry (ArrayList<String> deconstructedEntry) {
		String command = deconstructedEntry.get(0);
		String question = deconstructedEntry.get(1);
		String answer = this.ServerSession.getFromServer(command + ": " + question);

		return new Entry(command, question, answer);
	}

	/**
	 * Helper method to set instantiated listeners to respective panels
	 * 
	 * @param ssListener
	 * @param lListener
	 * @param closeListener
	 * @param seListener
	 * @param cancelListener
	 */
	public void setQPPHListeners(StartStopListener ssListener, QuestionListHandler lListener,
			ClosingFrameListener closeListener, SetupEmailListener seListener, 
			CancelListener cancelListener){
		
		JButton startButton = qp.getStartButton();
		JButton setupButton = ep.getSetupButton();
		JButton cancelButton = ep.getCancelButton();
		JList<String> promptList = ph.getPromptList(); 
		startButton.addActionListener(ssListener);
		promptList.addListSelectionListener(lListener);
		setupButton.addActionListener(seListener);
		cancelButton.addActionListener(cancelListener);
		this.af.addWindowListener(closeListener);
	}

	/**
	 * Helper method to get listeners for mediator to observe
	 * 
	 * @return button subjects
	 */
	public ArrayList<ButtonSubject> getListeners(){
		ArrayList<ButtonSubject> allListeners = new ArrayList<ButtonSubject>();
		ClosingFrameListener closeListener = new ClosingFrameListener();
		StartStopListener ssListener = new StartStopListener();
		QuestionListHandler lListener = new QuestionListHandler();
		SetupEmailListener seListener = new SetupEmailListener();
		CancelListener cancelListener = new CancelListener();
		setQPPHListeners(ssListener, lListener, closeListener, seListener, cancelListener);
		allListeners.add(ssListener);
		allListeners.add(lListener);
		allListeners.add(closeListener);
		allListeners.add(seListener);
		allListeners.add(cancelListener);
		return allListeners;
	}


	/**
	 * Helper method to decipher what command was captured by voice recording
	 * 
	 * @param question
	 * @return ArrayList<String> {Command,Prompt}
	 */
	public ArrayList<String> parseCommand(String question){

		final String[] LOWERCASECOMMANDS = {"question", "send email", "create email", "setup email", "set up email", "delete prompt", "clear all"};
		final String[] COMMANDS = {"Question", "Send Email", "Create Email", "Setup Email", "Set up email", "Delete Prompt", "Clear All"};
		ArrayList<String> result = null;

		if (question != null){
			for (int i = 0; i < LOWERCASECOMMANDS.length; i++) {
				String tempQuestion = question;
				if (tempQuestion.toLowerCase().startsWith(LOWERCASECOMMANDS[i])) {
					result = new ArrayList<String>();
					String command = COMMANDS[i];

					//special case
					if (command.equalsIgnoreCase("set up email")) {
						command = "Setup Email";
					}

					result.add(command);

					//check if there are remaining words first, add them as prompt
					try {
						String prompt = question.substring(COMMANDS[i].length()+1).trim();
						result.add(prompt);
					}
					catch (StringIndexOutOfBoundsException e){
						result.add(null);
					}
				}
			}
		}

		return result;
	}

	///////////////////////////LISTENER METHODS////////////////////////////////////

	/**
	 * Registers observers
	 */
	@Override
	public void registerObserver(MediatorObserver parentFrame) {
		this.parentFrames.add(parentFrame);
	}

	/**
	 * notifies observers when setup email command detected
	 */
	@Override
	public void notifyObservers() {
		for (MediatorObserver frame : parentFrames){

			frame.onEmailSetup();
		}  
	}

	/////////////////////////////////TEST METHODS////////////////////////////////////

	/**
	 * Old constructor still used in tests
	 * 
	 * @param createdButtons
	 * @param createdqp
	 * @param createdph
	 * @param recorder
	 * @param WhisperSession
	 * @param ChatGPTSession
	 * @param ServerSession
	 */
	public QPHPHButtonPanelPresenter(ArrayList<ButtonSubject> createdButtons, 
			QuestionPanel createdqp, PromptHistory createdph, Recorder recorder, 
			WhisperInterface WhisperSession, ChatGPTInterface ChatGPTSession, 
			ServerInterface ServerSession){
		
		this.parentFrames = new ArrayList<MediatorObserver>();
		allButtons = createdButtons;
		qp = createdqp;
		ph = createdph;
		this.recorder = recorder;
		this.WhisperSession = WhisperSession;
		this.ChatGPTSession = ChatGPTSession;
		this.ServerSession = ServerSession;

		for (ButtonSubject button : allButtons){
			button.registerObserver(this);
		}
		qp.registerObserver(this);
		ph.registerObserver(this);
	}
}