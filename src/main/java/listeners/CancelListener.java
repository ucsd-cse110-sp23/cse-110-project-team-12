package listeners;
import java.awt.event.*;

import interfaces.ButtonSubject;
import mediators.QPHPHButtonPanelPresenter;

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
