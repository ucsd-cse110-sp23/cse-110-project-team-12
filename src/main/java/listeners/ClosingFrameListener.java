/**
 * @author CSE 110 - Team 12
 */
package listeners;

import interfaces.ButtonSubject;
import mediators.QPHPHButtonPanelPresenter;

import java.awt.event.*;

/**
 * Listener for closing app
 *
 */
public class ClosingFrameListener  extends WindowAdapter implements ButtonSubject {

    QPHPHButtonPanelPresenter presenter;

    @Override
    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        notifyObservers();
    }
    
    @Override
    public void registerObserver(QPHPHButtonPanelPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void notifyObservers() {
        presenter.onClose();
    }
    
}
