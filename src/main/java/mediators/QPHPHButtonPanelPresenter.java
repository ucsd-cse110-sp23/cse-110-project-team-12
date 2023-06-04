package mediators;
import java.io.*;
import java.util.ArrayList;

import api.ChatGPT;
import api.Whisper;
import interfaces.*;
import mainframe.*;
import processing.*;
import server.ServerCalls;

public class QPHPHButtonPanelPresenter implements ButtonObserver, PanelObserver{
    private static String filePath = "bin/main/questionFile.txt";
    ArrayList<ButtonSubject> allButtons = new ArrayList<ButtonSubject>();
    QuestionPanel qp;
    PromptHistory ph;
    Recorder recorder;
    AudioToResultInterface audioToResult;

    int TEMPCOUNT = 0;


    public QPHPHButtonPanelPresenter(ArrayList<ButtonSubject> createdButtons, QuestionPanel createdqp, PromptHistory createdph, Recorder recorder, AudioToResultInterface audioToResult){
        this.recorder = recorder;
        this.audioToResult = audioToResult;
        allButtons = createdButtons;
        qp = createdqp;
        ph = createdph;
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
          recorder = new Recorder(); 
          System.out.println("startedRecording");
          recorder.startRecording();
          qp.startedRecording();

        }
        else {
            System.out.println("stoppedRecording");
            File audioFile = recorder.stopRecording();
            qp.stoppedRecording();
            WhisperInterface WhisperSession = null;
            try {
                WhisperSession = new Whisper(audioFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            audioToResult = new AudioToResult(WhisperSession, new ChatGPT());
            Entry entry = audioToResult.getEntry();

            // //TESTING
            // Entry entry = new QuestionEntry("Question", "When is christmas" , "25 December" );
            // TEMPCOUNT++;
            qp.onNewEntry(entry);
            ph.onNewEntry(entry);
            ServerCalls.postToServer(entry);   
        }
    }

    //String prompt is formatted as "Question: <Prompt>"
    @Override
    public void onListChange(String prompt) {
        System.out.println("listChanged");
        if (prompt.startsWith("Question")){
            String question = prompt.substring(prompt.indexOf(":") + 2); 
            String answer = ServerCalls.getFromServer(question);
            qp.onListChange(question, answer);
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
}
