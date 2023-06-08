/**
 * @author CSE 110 - Team 12
 */
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import processing.ErrorMessages;
import processing.SavefileWriter;
import interfaces.ErrorMessagesInterface;
import interfaces.LoginButtonsSubject;
import interfaces.MongoInterface;
import mainframe.*;
import mediators.*;
import api.MongoDB;
import java.util.ArrayList;
import javax.swing.JButton;

/**
 * Test for US9 - user logs in using Login button
 *
 */
public class DS9LoginTest {

	//Mock classes
	private static MongoInterface mongoDBMock;
	private static ErrorMessagesInterface errorMessagesMock ;
	private static SavefileWriter sfWriterMock;
	private static LoginPanel LoginPanelMock;
	private static AppFrame AppFrameMock ;
	private static LoginFrame LoginFrameMock ;
	private static QuestionPanel qpMock;
	private static PromptHistory phMock;

	//buttons
	private static JButton createButton;
	private static JButton loginButton;

	//mediator
	private static LoginMediator testLogic;

	/**
	 * Sets up environment for mocking
	 */
	@BeforeEach
	public void setUp(){
		ArrayList<LoginButtonsSubject> allButtons = new ArrayList<LoginButtonsSubject>();

		//Mock classes
		mongoDBMock = mock(MongoDB.class);
		errorMessagesMock = mock(ErrorMessages.class);
		sfWriterMock = mock(SavefileWriter.class);
		LoginFrameMock = mock(LoginFrame.class);
		AppFrameMock = mock(AppFrame.class);
		LoginPanelMock = mock(LoginPanel.class);
		qpMock = mock(QuestionPanel.class);
		phMock = mock(PromptHistory.class);

		//Mock Method calls
		when(AppFrameMock.getQuestionPanel()).thenReturn(qpMock);
		when(AppFrameMock.getPromptHistory()).thenReturn(phMock);
		when(LoginFrameMock.getLoginPanel()).thenReturn(LoginPanelMock);
		when(LoginPanelMock.getLoginButton()).thenReturn(loginButton = new JButton());
		when(LoginPanelMock.getcreateAccountButton()).thenReturn(createButton = new JButton());

		testLogic = new LoginMediator(LoginFrameMock, AppFrameMock, mongoDBMock,errorMessagesMock, sfWriterMock);
	}

	/**
	 * Scenario 1: User logs in with registered email and password
	 * Given that I already have an account 
	 * And I’m on the create account/login page
	 * When I enter my registered email in the email field
	 * And I enter my password in the password field
	 * And press the “Log In” button
	 * Then I’m logged into my account
	 * And the app continues to the prompt/message page
	 * 
	 * @throws Exception
	 */
	@Test
	void test_1_1_ValidEmail() throws Exception {
		String takenEmail = "12345";
		String ValidPassword = "password";
		when(LoginPanelMock.getEmail()).thenReturn(takenEmail);
		when(LoginPanelMock.getPass1()).thenReturn(ValidPassword);
		when(mongoDBMock.checkValidLogin(takenEmail,ValidPassword)).thenReturn(true);
		testLogic.onLogin();
		//check that UI responds to successful login
		verify(AppFrameMock).onLoginClosing();
		verify(LoginFrameMock).onLoginClosing();
	}

	/**
	 * Scenario 2: User tries to login with unregistered email
	 * Given that I already have an account
	 * And I’m on the create account/login page
	 * When I enter an email not linked to an existing account in the email field
	 * And I enter a password in the password field
	 * And I press the “Log In” button
	 * Then an error message pops up saying the email is not recognized
	 * And I am returned to the create account/login page
	 * 
	 * @throws Exception
	 */
	@Test
	void test_2_1_inValidEmail() throws Exception {
		String newEmail = "12345";
		String arbitraryPassword = "password";
		String expectedErrorMessage = "Incorrect Login Details";
		when(LoginPanelMock.getEmail()).thenReturn(newEmail);
		when(LoginPanelMock.getPass1()).thenReturn(arbitraryPassword);
		when(mongoDBMock.checkValidLogin(newEmail,arbitraryPassword)).thenReturn(false);
		testLogic.onLogin();
		//check that error message shows
		verify(errorMessagesMock).showErrorMessage(expectedErrorMessage);

	}

	/*----------------3------------------
	 * Given that I already have an account
	 * And I’m on the create account/login page
	 * And I fail to fill out my email, password, or both
	 * When I press the “Log In” button
	 * Then an error message pops up
	 * And I am returned to the create account/login page
	 */
	
	/**
	 * Scenario 3.1: User tries to log in without filling in email
	 * S
	 * @throws Exception
	 */
	@Test
	void test_3_1_missingEmail() throws Exception {
		String emptyEmail = "";
		String arbitraryPassword = "password";
		String expectedErrorMessage = "Missing Email";
		when(LoginPanelMock.getEmail()).thenReturn(emptyEmail);
		when(LoginPanelMock.getPass1()).thenReturn(arbitraryPassword);
		testLogic.onLogin();
		//check that error message shows
		verify(errorMessagesMock).showErrorMessage(expectedErrorMessage);

	}

	/**
	 * Scenario 3.2: User tries to log in without filling in password
	 * 
	 * @throws Exception
	 */
	@Test
	void test_3_2_missingPassword() throws Exception {
		String arbitraryEmail = "Email";
		String emptyPassword = "";
		String expectedErrorMessage = "Missing Password";
		when(LoginPanelMock.getEmail()).thenReturn(arbitraryEmail);
		when(LoginPanelMock.getPass1()).thenReturn(emptyPassword);
		testLogic.onLogin();
		//check that error message shown
		verify(errorMessagesMock).showErrorMessage(expectedErrorMessage);

	}

	/**
	 * Scenario 4: User tries to log in with wrong password
	 * Given that I already have an account
	 * And I’m on the create account/login page
	 * When I enter my registered email in the email field
	 * And I enter a wrong password in the password field
	 * And I press the “Log In” button
	 * Then an error message pops up saying the password is incorrect
	 * And I am returned to the create account/login page
	 * 
	 * @throws Exception
	 */
	@Test
	void test_4_1_InvalidPassword() throws Exception {
		String takenEmail = "12345";
		String InvalidPassword = "wrong";
		String expectedErrorMessage = "Incorrect Login Details";
		when(LoginPanelMock.getEmail()).thenReturn(takenEmail);
		when(LoginPanelMock.getPass1()).thenReturn(InvalidPassword);
		when(mongoDBMock.checkValidLogin(takenEmail,InvalidPassword)).thenReturn(false);
		testLogic.onLogin();
		//check that error message shown
		verify(errorMessagesMock).showErrorMessage(expectedErrorMessage);
	}
	
}
