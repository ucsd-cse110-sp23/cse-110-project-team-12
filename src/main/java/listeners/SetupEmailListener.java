/**
 * @author CSE 110 - Team 12
 */
package listeners;
import java.awt.event.*;

import interfaces.ButtonSubject;
import mediators.QPHPHButtonPanelPresenter;

/**
 * Listener for clicking Save button on email setup screen
 *
 */
public class SetupEmailListener implements ButtonSubject,ActionListener{
    QPHPHButtonPanelPresenter presenter;

    @Override
    public void actionPerformed(ActionEvent e) {
        notifyObservers();
    }

    @Override
    public void registerObserver(QPHPHButtonPanelPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void notifyObservers() {
        this.presenter.onEmailSetup();
    }
    
}
