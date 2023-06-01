package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import interfaces.*;

public class CreateAccountListener implements LoginUISubject,ActionListener {
	LoginUIObserver observer;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		notifyObservers();

	}

	@Override
	public void notifyObservers() {
		observer.onCreateAccount();
	}

	@Override
	public void registerObserver(LoginUIObserver observer) {
		this.observer = observer;
	}
}
