package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import interfaces.*;

public class CreateAccountListener implements LoginButtonsSubject,ActionListener {
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
		observer.onCreateAccount();
	}

	@Override
	public void registerObserver(LoginButtonsObserver observer) {
		this.observer = observer;
	}
}