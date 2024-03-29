/**
 * @author CSE 110 - Team 12
 */
package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import interfaces.LoginButtonsObserver;
import interfaces.LoginButtonsSubject;

/**
 * Listener for clicking Login button on login screen
 *
 */
public class LoginListener implements LoginButtonsSubject,ActionListener {
	LoginButtonsObserver observer;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			notifyObservers();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	@Override
	public void notifyObservers() throws Exception {
		observer.onLogin();
	}

	@Override
	public void registerObserver(LoginButtonsObserver observer) {
		this.observer = observer;
	}
}
