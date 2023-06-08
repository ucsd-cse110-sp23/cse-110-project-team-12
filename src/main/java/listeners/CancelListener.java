/**
 * @author CSE 110 - Team 12
 */
package listeners;
import java.awt.event.*;

import interfaces.ButtonSubject;
import mediators.QPHPHButtonPanelPresenter;

/**
 * Listener for pressing Cancel button on email setup
 *
 */
public class CancelListener implements ButtonSubject,ActionListener{
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
		this.presenter.onCancel();
	}

}
