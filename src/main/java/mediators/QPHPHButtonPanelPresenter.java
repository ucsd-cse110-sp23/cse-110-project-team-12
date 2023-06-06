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
    AppFrame af;
    QuestionPanel qp;
    PromptHistory ph;

    //Constructor used in app
    public QPHPHButtonPanelPresenter(AppFrame appFrame, Recorder recorder, WhisperInterface WhisperSession, 
    ChatGPTInterface ChatGPTSession, ServerInterface ServerSession, ErrorMessagesInterface ErrorMessages, MongoDB MongoDBSession){
        
        //Sets created panels that mediator uses
        af = appFrame;
        qp = appFrame.getQuestionPanel();
        ph = appFrame.getPromptHistory();
        
        
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
    }
    
    //////////////////////////////////////////////////////////BUTTON OBSERVER METHODS//////////////////////////////////////////////////////////////////////////////////////////////////

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
                }

                //Case 2 where command is email setup
                if (command.equalsIgnoreCase("Setup Email")) {            
                    notifyObservers();
                //     //real ask question to chatGPT    
                //     try {
                //         this.ChatGPTSession.askChatGPT(prompt);
                //     } 
                //     catch (InterruptedException e) {
                //         e.printStackTrace();
                //     }
                //     catch (IOException e) {
                //         e.printStackTrace();
                //     } 
        
                //     String answer = this.ChatGPTSession.getAnswer();
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
                System.out.println(entry.getTitle());
                System.out.println(entry.getResult());
                ph.onNewEntry(entry);
                this.ServerSession.postToServer(entry);
            }
        }
    }

    //////////////////////////////////////////////////////////Helper METHODS//////////////////////////////////////////////////////////////////////////////////////////////////
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
    public void setQPPHListeners(StartStopListener ssListener, QuestionListHandler lListener, ClosingFrameListener closeListener){
        JButton startButton = qp.getStartButton();
        JList<String> promptList = ph.getPromptList(); 
        startButton.addActionListener(ssListener);
        promptList.addListSelectionListener(lListener);
        this.af.addWindowListener(closeListener);
    }

    //Helper method to get listeners for mediator to observe
    public ArrayList<ButtonSubject> getListeners(){
        ArrayList<ButtonSubject> allListeners = new ArrayList<ButtonSubject>();
        ClosingFrameListener closeListener = new ClosingFrameListener();
        StartStopListener ssListener = new StartStopListener();
        QuestionListHandler lListener = new QuestionListHandler();
        setQPPHListeners(ssListener, lListener, closeListener);
        allListeners.add(ssListener);
        allListeners.add(lListener);
        allListeners.add(closeListener);
        return allListeners;
    }
    

    //Helper method to deciper what command was captured by voice recording
    public ArrayList<String> parseCommand(String question){
        final String[] COMMANDS = {"Question", "Send email", "Create email", "Setup email", "Delete prompt", "Clear all"};
        ArrayList<String> result = null;
        if (question != null){
            for (int i = 0; i < COMMANDS.length; i++) {
			
                if (question.startsWith(COMMANDS[i])) {
                    result = new ArrayList<String>();
                    String command = COMMANDS[i];
                    result.add(command);
                    //check if there are remaining words first
                    try {
                        String prompt = question.substring(COMMANDS[i].length()+1).trim();
                        result.add(prompt);
                    }
                    catch (StringIndexOutOfBoundsException e){
                        //No question detected
                        return null;
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
        for (MediatorObserver panel : parentFrames){
            panel.onEmailSetup();
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

//Helper method to get listeners for mediator to observe
public ArrayList<ButtonSubject> getListeners(QuestionPanel qp,PromptHistory ph){
    ArrayList<ButtonSubject> allListeners = new ArrayList<ButtonSubject>();
    ClosingFrameListener closeListener = new ClosingFrameListener();
    StartStopListener ssListener = new StartStopListener();
    QuestionListHandler lListener = new QuestionListHandler();
    setQPPHListeners(ssListener, lListener, closeListener);
    allListeners.add(ssListener);
    allListeners.add(lListener);
    allListeners.add(closeListener);
    return allListeners;
}


}
    

   