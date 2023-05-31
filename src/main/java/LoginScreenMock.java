
@SuppressWarnings("serial")
public class LoginScreenMock extends LoginScreen {
	
	LoginScreenMock(String email, String pass1, String pass2) {
		
		super(email,pass1,pass2);
		
		new CreateAccountListenerMock(this);
	}
	

}
