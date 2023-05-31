package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import interfaces.*;

public class CreateAccountListener implements LoginUISubject,ActionListener {
	LoginUIObserver observer;
	protected String email, pass1, pass2;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		notifyObservers();

	}

	@Override
	public void notifyObservers() {
		observer.onLogin();
	}

	@Override
	public void registerObserver(LoginUIObserver observer) {
		this.observer = observer;
	}
}
