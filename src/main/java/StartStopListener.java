import java.awt.List;
import java.awt.event.*;
import java.util.ArrayList;

/*
         * clicking START button
         * 
         * Starts and stops recording, changes labels accordingly
         * 
         * After recording stopped:
         *       add <question,answer> to server
         *       add "question" to prompt history list
         */

public class StartStopListener implements myListener{
  ArrayList<listenerObserver> panels = new ArrayList<listenerObserver>();
  boolean startedRecording = false;
    
    public void actionPerformed(ActionEvent e) {
        if (!startedRecording) {
          Recorder.startRecording();
          startedRecording = true;
          QuestionPanel.setButtonText("Stop Recording");
        } 
        else {
          Recorder.stopRecording();
          startedRecording = false;
          notifyObservers();
          //add question to server
          
          //add question to prompt history list
          PromptHistory.addPH(QuestionPanel.getQuestion());
          ServerCalls.postToServer();
          QuestionPanel.setButtonText("New Question");
        }

    }

    public void registerObserver(listenerObserver panel){
      panels.add(panel);
    } 

    
    public void notifyObservers(){
      for (listenerObserver panel : panels) {
        panel.onListenerChange();
      }
    } 


}