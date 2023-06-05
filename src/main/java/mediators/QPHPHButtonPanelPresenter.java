package mediators;
import java.io.*;
import java.util.ArrayList;

import api.*;
import interfaces.*;
import mainframe.*;
import processing.*;

public class QPHPHButtonPanelPresenter implements ButtonObserver, PanelObserver{
    private static String filePath = "bin/main/questionFile.txt";
    ArrayList<ButtonSubject> allButtons = new ArrayList<ButtonSubject>();
    QuestionPanel qp;
    PromptHistory ph;
    Recorder recorder;
    WhisperInterface WhisperSession;
    ChatGPTInterface ChatGPTSession;
    ServerInterface ServerSession;

    public QPHPHButtonPanelPresenter(ArrayList<ButtonSubject> createdButtons, QuestionPanel createdqp, PromptHistory createdph, Recorder recorder, WhisperInterface WhisperSession, ChatGPTInterface ChatGPTSession, ServerInterface ServerSession){
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
    
    //BUTTON OBSERVER METHODS
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
                System.out.println(question);
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
                    entry = new QuestionEntry(command, prompt, answer);
                }
                qp.onNewEntry(entry);
                ph.onNewEntry(entry);
                this.ServerSession.postToServer(entry);   
            }
            else{
                qp.InvalidInputDetected(question);
            }
        }
            
        }
        //String prompt is formatted as "Question: <Prompt>"
    @Override
    public void onListChange(String prompt) {
        System.out.println("listChanged");
        if (prompt.startsWith("Question")){
            String question = prompt.substring(prompt.indexOf(":") + 2); 
            String answer = this.ServerSession.getFromServer(question);
            qp.onListChange(question, answer);
        }   
    }

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
                        String prompt = question.substring(COMMANDS[i].length()+2);
                        result.add(prompt);
                        System.out.println(prompt);
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
}

    

    // @Override
    // public void onDelete() {
    //     System.out.println("deleted");
    //     String question = qp.getQuestion();
    //     int questionIndex = ph.getIndexInPH(question);
    //     System.out.println(questionIndex);
    //     ServerCalls.deleteFromServer(question);
    //     ph.removePH(questionIndex);
    //     qp.setQuestion("Your Question will appear here");
    //     qp.setAnswer("Your Answer will appear here");
    // }

    // @Override
    // public void onClear(){
    //     System.out.println("cleared");
    //     for (int i = 0; i < ph.getPHSize(); i++) {
    //         String question = ph.getElementInPH(i);
    //         ServerCalls.deleteFromServer(question);
    //     }
    //       //TODO Should be some kind of update method with ClearListener
    //     qp.setQuestion("Your Question will appear here");
    //     qp.setAnswer("Your Answer will appear here");
    //     ph.resetPH();
    // }
    //_______________________________
//<TODO>

// // * save asked questions to text file
// // */
// public void saveQuestions() {
//   try {
//     FileWriter questionFile = new FileWriter(filePath);
    
//     for (int i = 0; i < ph.getPHSize(); i++) {
      
//     questionFile.write(ph.getElementInPH(i));
//     questionFile.write("\n");
      
//     }
//     questionFile.close();
//   } catch (IOException e) {
//       System.out.println("saveQuestions() failed");
//   }	  
// }


//  /*
// * load asked questions from text file
// */
// public void loadQuestions() {

//   try {
//     BufferedReader questionFile = new BufferedReader(new FileReader(filePath));
//     String currLine;

//     while ((currLine = questionFile.readLine()) != null) {      		
//       String response = ServerCalls.getFromServer(currLine);
//       // String question = response.substring(0,);
//       // String answer = response.substring()
//     }
    
//     questionFile.close();
    
//   } catch (IOException e){
//     System.out.println("loadQuestions() failed");
//   }
  
// }
