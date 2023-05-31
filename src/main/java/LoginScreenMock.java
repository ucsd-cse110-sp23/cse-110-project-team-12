
@SuppressWarnings("serial")
public class LoginScreenMock extends LoginScreen {
	LoginScreenMock(String email, String pass1, String pass2) {
		super();
		super.setEmail(email);
		super.setPass1(pass1);
		super.setPass2(pass2);
		createAccount.addActionListener(new CreateAccountListenerMock(this));
	}
	

}
