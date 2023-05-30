import java.awt.List;
import java.awt.event.*;
import java.io.IOException;
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
	private QuestionPanel myPanel;
    
    public StartStopListener(QuestionPanel questionPanel) {
    	myPanel = questionPanel;
    }

	public void actionPerformed(ActionEvent e) {
        if (!startedRecording) {
          Recorder.startRecording(myPanel.getUser());
          startedRecording = true;
          myPanel.setButtonText("Stop");
        } 
        else {
          try {
			Recorder.stopRecording();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        startedRecording = false;
        notifyObservers();
        myPanel.setButtonText("Start");
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