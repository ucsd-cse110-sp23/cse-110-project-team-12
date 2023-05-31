import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JOptionPane;

public class CreateAccountListenerMock extends CreateAccountListener {

	public CreateAccountListenerMock(LoginScreenMock screen) {
		super(screen);
		
		email = screen.emailText;
		pass1 = screen.pass1Text;
		pass2 = screen.pass2Text;
		
		CheckEmailDupeMock checkEmail = new CheckEmailDupeMock(email, "TestUsers");
		
		if (pass1.equals(pass2) && checkEmail.emailExists == false) {
			new CreateAccountMock(email, pass1, "TestUsers");
			
			myScreen.dispatchEvent(new WindowEvent(myScreen, WindowEvent.WINDOW_CLOSING));
    		
		} else {
			JOptionPane.showMessageDialog(null, "Error: Email or Password Invalid!");
		}
	}

}
