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

    //Constructor used in app
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
    
    //////////////////////////////////////////////////////////BUTTON OBSERVER METHODS//////////////////////////////////////////////////////////////////////////////////////////////////

    //When start button is clicked. Can be used to start recording or stop recording.
    @Override
    public void onStartStop(boolean startedRecording) {
        if (startedRecording){
          recorder.startRecording();
          qp.startedRecording();

        }
        else {
            File audioFile = recorder.stopRecording();
            qp.stoppedRecording();
            String question = null;
            try {
                this.WhisperSession.setWhisperFile(audioFile);
                question = WhisperSession.getQuestionText();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //System.out.println(question);
            ArrayList<String> result = parseCommand(question);
            if (result != null){
                String command = result.get(0);
                String prompt = result.get(1);
                Entry entry;

                //Case 1 where command is a question
                if (command.equalsIgnoreCase("Question")) {            
                
                    //real ask question to chatGPT    
                    try {
                        this.ChatGPTSession.askChatGPT(prompt);
                    } 
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    } 
        
                    String answer = this.ChatGPTSession.getAnswer();
                    entry = new Entry(command, prompt, answer);
                    qp.onNewEntry(entry);
                    ph.onNewEntry(entry);
                    this.ServerSession.postToServer(entry); 
                    
                  //Case 2 where command is to create email draft
                } else if (command.equalsIgnoreCase("Create email")) {
                	String displayName;
                	if (this.MongoDBSession.getDisplayNameEmail() != null) {
                		displayName = this.MongoDBSession.getDisplayNameEmail();
                	} else {
                		displayName = "Default User";
                	}
                	
                	//create email with chatGPT    
                    try {
                        this.ChatGPTSession.askChatGPT("Create an email draft that ends in Best regards, " + displayName + "and the email's body is" + prompt);
                    } 
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    } 
        
                    String answer = this.ChatGPTSession.getAnswer().trim();
                    entry = new Entry(command, prompt, answer);
                    qp.onNewEntry(entry);
                    ph.onNewEntry(entry);
                    this.ServerSession.postToServer(entry);
                }

                //Case 3 where command is email setup
                if (command.equalsIgnoreCase("Setup Email")) {           
                    notifyObservers();
                }
                
                //Case 4 where command is send email
                if (command.equalsIgnoreCase("Send Email") && prompt != null && prompt.startsWith("to ")) {
                	
                	//can only send email if created email is selected in PH
                	if (this.qp.getQuestion().startsWith("Create Email")) {
                		String firstName;
                		//can only send email if email setup has been configured
	                	if ((firstName = this.MongoDBSession.getFirstName()) != null ) {
		                	String lastName = this.MongoDBSession.getLastName();
		                	String emailBody = this.qp.getAnswer();
		                	String fromEmail = this.MongoDBSession.getFromEmail();
		                	
		                	//configure toEmail from input
		                	prompt = prompt.replace(" at ", "@");
		                	prompt = prompt.replace(" dot ", ".");
		                	prompt = prompt.toLowerCase();
		                	//removing trailing period
		                	if (prompt.charAt(prompt.length()-1) == '.') {
		                		prompt = prompt.substring(0, prompt.length()-1);
		                	}
		                	String toEmail = prompt.replace("to ", "");
		                	toEmail = toEmail.trim();
		                	
		                	this.tlsEmail.setFromEmail(this.MongoDBSession.getFromEmail());
		                	this.tlsEmail.setSMTPHost(this.MongoDBSession.getSMTPHost());
		                	this.tlsEmail.setTLSPort(this.MongoDBSession.getTLSPort());
		                	this.tlsEmail.setPassword(this.MongoDBSession.getPassword());
		                	
		                	Session session = this.tlsEmail.startEmailSession();
		                	
		                	if (this.emailUtil.sendEmail(session, toEmail,fromEmail,"New Message from " + firstName + " " + lastName + " on SayItAssistant2", emailBody)) {
		                		entry = new Entry(command, prompt, "Email successfully sent.");
		                        qp.onNewEntry(entry);
		                        
		                	} else {
		                		entry = new Entry(command, prompt, "Email not sent. SMTP Host: " + this.MongoDBSession.getSMTPHost());
		                        qp.onNewEntry(entry);
		                	} 
	                	} else {
	                		ErrorMessages.showErrorMessage("Use Setup Email command before sending emails.");
	                	}
                	} else {
                		ErrorMessages.showErrorMessage("Must select created email before sending email.");
                	}
                }
                 
            } else {
                qp.InvalidInputDetected(question);
            }
        }
            
    }

    //When user selects entry in prompt history
    @Override
    public void onListChange(String prompt) {
        String answer = this.ServerSession.getFromServer(prompt);
        qp.onListChange(prompt, answer);
    }

    //When user closes the frame
    @Override
    public void onClose() {
        boolean confirmation = ErrorMessages.confirmClosing();
        if (confirmation){
            ArrayList<Entry> savedHistory = new ArrayList<Entry>();
            ListModel<String> modelPromptHistory = ph.getListModel();
            for (int i = 0; i < modelPromptHistory.getSize(); i++){
                ArrayList<String> deconstructedEntry = parseCommand(modelPromptHistory.getElementAt(i));
                Entry entry = convertToEntry(deconstructedEntry);
                savedHistory.add(entry);
            }
            this.MongoDBSession.updateSavedPromptHistory(savedHistory);       
            af.dispose();     
        }        
    }

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

    //
    public void onEmailSetup(){
        if (!ep.checkAllFieldsFilled()){
            ErrorMessages.showErrorMessage("Fill up all fields");
        }
        else {
            ef.setVisible(false);
            ArrayList<String> fields = ep.getAllFields();
            this.MongoDBSession.setupEmail(fields);
        }
        
        
    }
    
    public void onCancel() {
    	ef.setVisible(false);
    }

    //////////////////////////////////////////////////////////Helper METHODS//////////////////////////////////////////////////////////////////////////////////////////////////
    public Entry convertToEntry (ArrayList<String> deconstructedEntry) {
        String command = deconstructedEntry.get(0);
        String question = deconstructedEntry.get(1);
        String answer = this.ServerSession.getFromServer(command + ": " + question);

        return new Entry(command, question, answer);
    }

    //Helper method to set instantiated listeners to respective panels
    public void setQPPHListeners(StartStopListener ssListener, QuestionListHandler lListener, ClosingFrameListener closeListener, SetupEmailListener seListener, CancelListener cancelListener){
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

    //Helper method to get listeners for mediator to observe
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
    

    //Helper method to decipher what command was captured by voice recording
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
                   
                    //check if there are remaining words first
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

        //////////////////////////////////////////////////////////LISTENER METHODS//////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void registerObserver(MediatorObserver parentFrame) {
        this.parentFrames.add(parentFrame);
    }

    @Override
    public void notifyObservers() {
        for (MediatorObserver frame : parentFrames){
           
            frame.onEmailSetup();
        }  
    }



        //////////////////////////////////////////////////////////TODO OLD METHODS//////////////////////////////////////////////////////////////////////////////////////////////////

//Old constructor still used in tests
public QPHPHButtonPanelPresenter(ArrayList<ButtonSubject> createdButtons, QuestionPanel createdqp, PromptHistory createdph, Recorder recorder, WhisperInterface WhisperSession, ChatGPTInterface ChatGPTSession, ServerInterface ServerSession){
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

// //Helper method to get listeners for mediator to observe
// public ArrayList<ButtonSubject> getListeners(QuestionPanel qp,PromptHistory ph){
//     ArrayList<ButtonSubject> allListeners = new ArrayList<ButtonSubject>();
//     ClosingFrameListener closeListener = new ClosingFrameListener();
//     StartStopListener ssListener = new StartStopListener();
//     setQPPHListeners(ssListener, closeListener);
//     allListeners.add(ssListener);
//     allListeners.add(lListener);
//     allListeners.add(closeListener);
//     return allListeners;
// }


}
    

   