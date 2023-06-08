/**
 * @author CSE 110 - Team 12
 */
package listeners;
import java.awt.event.*;

import interfaces.ButtonSubject;
import mediators.QPHPHButtonPanelPresenter;

/**
 * Listener for Start/Stop button on QP screen
 * 	Starts and stops recording, changing button label accordingly
 * 	Adds Entry to server and PH when recording stopped
 * 
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