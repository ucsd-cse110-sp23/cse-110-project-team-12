package listeners;
import java.awt.event.*;

import interfaces.ButtonSubject;
import mediators.QPHPHButtonPanelPresenter;

/*
         * clicking START button
         * 
         * Starts and stops recording, changes labels accordingly
         * 
         * After recording stopped:
         *       add <question,answer> to server
         *       add "question" to prompt history list
         */

public class StartStopListener implements ButtonSubject,ActionListener{
  QPHPHButtonPanelPresenter presenter;
  boolean startedRecording = false;

    public void actionPerformed(ActionEvent e) {
        if (!startedRecording) {
          startedRecording = true;
        } 
        else {
          startedRecording = false;
        }
        notifyObservers();
    }

    public void registerObserver(QPHPHButtonPanelPresenter presenter) {
      this.presenter = presenter;
    }

    public void notifyObservers(){
      presenter.onStartStop(startedRecording);
    } 

}