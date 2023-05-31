package listeners;
import java.awt.event.*;

import interfaces.ButtonSubject;
import mainframe.ButtonPanelPresenter;

/*
  * clicking CLEAR ALL button
  * 
  * Deletes every q/a from server
  * Clears and updates prompt history list
  */

public class ClearListener implements ButtonSubject,ActionListener{
  ButtonPanelPresenter presenter;

    public void actionPerformed(ActionEvent e) {
        notifyObservers();
    }

    public void registerObserver(ButtonPanelPresenter presenter) {
      this.presenter = presenter;
    }

    public void notifyObservers(){
      presenter.onClear();
    } 

  }
