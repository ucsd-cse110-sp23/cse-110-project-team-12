package listeners;

import java.awt.event.*;

import listeners.interfaces.ButtonSubject;
import java.awt.event.ActionListener;
import mainframe.ButtonPanelPresenter;
//TODO - MAKE BUTTON INVISIBLE WHEN NOT VALID 

/*
  * clicking DELETE button
  * 
  * Deletes selected question from prompt history and server
  * Resets question and answer fields
  */

public class DeleteListener implements ButtonSubject,ActionListener {
    ButtonPanelPresenter presenter;
    String question;
    int questionIndex;
    boolean retrievedQuestion = false;
    
    //Now assumes that action only performed when valid.
    public void actionPerformed(ActionEvent e) {
        notifyObservers();
    }

        public void registerObserver(ButtonPanelPresenter presenter) {
            this.presenter = presenter;
        }
    
        public void notifyObservers() {
            presenter.onDelete();
        }
    
}
