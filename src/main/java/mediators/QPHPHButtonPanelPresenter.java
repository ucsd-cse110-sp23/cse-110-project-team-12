package mediators;
import java.io.*;
import java.util.ArrayList;

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

    //Constructor used in app
    public QPHPHButtonPanelPresenter(EmailSetupFrame esFrame, AppFrame appFrame, Recorder recorder, WhisperInterface WhisperSession, 
    ChatGPTInterface ChatGPTSession, ServerInterface ServerSession, ErrorMessagesInterface ErrorMessages, MongoDB MongoDBSession){
        
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

    //When user first opens the appframe after login
    @Override
    public void onStart() {
        //pulls data from MongoDB server
        ArrayList<Entry> savedPromptHistory = this.MongoDBSession.retrieveSavedPromptHistory();
        //populates prompt history with entries and posts to local server.
        for (Entry entry : savedPromptHistory){
            if (entry != null){
                System.out.println(entry.getTitle());
                System.out.println(entry.getResult());
                ph.onNewEntry(entry);
                this.ServerSession.postToServer(entry);
            }
        }
    }

    //When start button is clicked. Can be used to start recording or stop recording.
    @Override
    public void onStartStop(boolean startedRecording) {
        if (startedRecording){
          System.out.println("startedRecording");
          recorder.startRecording();
          qp.startedRecording();

        }
        else {
            System.out.println("stoppedRecording");
            File audioFile = recorder.stopRecording();
            qp.stoppedRecording();
            String question = null;
            try {
                this.WhisperSession.setWhisperFile(audioFile);
                question = WhisperSession.getQuestionText();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayList<String> result = parseCommand(question);
            if (result != null){
                String command = result.get(0);
                String prompt = result.get(1);
                Entry entry = null;

                //Case 1 where command is a question
                if (command.equalsIgnoreCase("Question")) {            
                    onQuestionCommand(command, prompt);
                     
                }

                //Case 2 where command is email setup
                if (command.equalsIgnoreCase("Setup Email")) {           
                    notifyObservers();
                }

                //Case 3 where command is delete
                if (command.equalsIgnoreCase("Delete")){
                    onDelete();
                }

                
                 
            }
            else{
                qp.InvalidInputDetected(question);
            }
        }
            
        }
        //String prompt is formatted as "Question: <Prompt>"

    //When user selects entry in prompt history
    @Override
    public void onListChange(String prompt) {
        if (prompt.startsWith("Question")){
            System.out.println("listChanged");
            String answer = this.ServerSession.getFromServer(prompt);
            qp.onListChange(prompt, answer);
        }   
    }

    //When user closes the appframe
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



    //////////////////////////////////////////////////////////LOGIC HELPER METHODS//////////////////////////////////////////////////////////////////////////////////////////////////
    
    //Stop button is clicked:Question command is parsed
    public Entry onQuestionCommand(String command, String prompt){
        Entry entry;
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
        System.out.println(command + prompt + answer);
        this.ServerSession.postToServer(entry); 
        return entry;
    }

    //Stop button is clicked: Delete is the given command
    public void onDelete(){

    }

    //Setup button is clicked: Setup Email is completed.
    public void onEmailSetup(){
        if (!ep.checkAllFieldsFilled()){
            ErrorMessages.showErrorMessage("Fill up all fields");
        }
        else {
            System.out.println("here");
            ef.setVisible(false);
            ArrayList<String> fields = ep.getAllFields();
            this.MongoDBSession.setupEmail(fields);
            Entry entry= new Entry("Setup Email", null, null);
            qp.onNewEntry(entry);
        }
        
        
    }
    

    //////////////////////////////////////////////////////////Helper METHODS//////////////////////////////////////////////////////////////////////////////////////////////////

    //Helper method to convert recordings to entries.
    public Entry convertToEntry (ArrayList<String> deconstructedEntry) {
        String command = deconstructedEntry.get(0);
        String question = deconstructedEntry.get(1);
        String answer;
        if (command.equalsIgnoreCase("Question")){
            answer = this.ServerSession.getFromServer(command + ": " + question);
        }
        else {
            answer = null;
        }

        return new Entry(command, question, answer);
    }

    //Helper method to set instantiated listeners to respective panels
    public void setQPPHListeners(StartStopListener ssListener, QuestionListHandler lListener, ClosingFrameListener closeListener, SetupEmailListener seListener){
        JButton startButton = qp.getStartButton();
        JButton setupButton = ep.getSetupButton();
        JList<String> promptList = ph.getPromptList(); 
        startButton.addActionListener(ssListener);
        promptList.addListSelectionListener(lListener);
        setupButton.addActionListener(seListener);
        this.af.addWindowListener(closeListener);
    }

    //Helper method to get listeners for mediator to observe
    public ArrayList<ButtonSubject> getListeners(){
        ArrayList<ButtonSubject> allListeners = new ArrayList<ButtonSubject>();
        ClosingFrameListener closeListener = new ClosingFrameListener();
        StartStopListener ssListener = new StartStopListener();
        QuestionListHandler lListener = new QuestionListHandler();
        SetupEmailListener seListener = new SetupEmailListener();
        setQPPHListeners(ssListener, lListener, closeListener, seListener);
        allListeners.add(ssListener);
        allListeners.add(lListener);
        allListeners.add(closeListener);
        allListeners.add(seListener);
        return allListeners;
    }
    

    //Helper method to deciper what command was captured by voice recording
    public ArrayList<String> parseCommand(String question){
        final String[] LOWERCASECOMMANDS = {"question", "send email", "create email", "setup email", "delete prompt", "clear all"};
        final String[] COMMANDS = {"Question", "Send Email", "Create Email", "Setup Email", "Delete Prompt", "Clear All"};
        ArrayList<String> result = null;
        if (question != null){
            for (int i = 0; i < LOWERCASECOMMANDS.length; i++) {
                String tempQuestion = question;
                if (tempQuestion.toLowerCase().startsWith(LOWERCASECOMMANDS[i])) {
                    result = new ArrayList<String>();
                    String command = COMMANDS[i];
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
    

   