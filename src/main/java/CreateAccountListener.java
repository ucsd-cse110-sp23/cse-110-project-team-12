import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JOptionPane;

public class CreateAccountListener implements myListener {
	protected String email, pass1, pass2;
	protected LoginScreen myScreen;
	
	public CreateAccountListener(LoginScreen screen) {
		myScreen = screen;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.email = myScreen.emailField.getText();
		this.pass1 = myScreen.passField1.getText();
		this.pass2 = myScreen.passField2.getText();
		
		CheckEmailDupe checkEmail = new CheckEmailDupe(email, "Users");
		
		if (pass1.equals(pass2) && checkEmail.emailExists == false) {
			new CreateAccount(email, pass1, "Users");
			
			myScreen.dispatchEvent(new WindowEvent(myScreen, WindowEvent.WINDOW_CLOSING));

			try {
				AppFrame app = new AppFrame(email);
				app.setVisible(true);
				//force exit app if server not connected
	            MyServer.checkServerAvailability();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
    		
		} else {
			JOptionPane.showMessageDialog(null, "Error: Email or Password Invalid!");
		}
		
	}

	@Override
	public void registerObserver(listenerObserver panel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyObservers() {
		// TODO Auto-generated method stub
		
	}

}
