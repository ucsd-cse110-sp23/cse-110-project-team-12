import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import processing.ErrorMessages;
import processing.SavefileWriter;
import interfaces.ErrorMessagesInterface;
import interfaces.LoginButtonsSubject;
import interfaces.MongoInterface;
import listeners.CreateAccountListener;
import mainframe.*;
import mediators.*;
import api.MongoDB;

import java.util.ArrayList;

import javax.swing.JButton;

public class DS8CreateAccountTest {
    
	private static MongoInterface mongoDBMock;
    private static ErrorMessagesInterface errorMessagesMock ;
    private static SavefileWriter sfWriterMock;
    private static LoginPanel lpMock;
    private static AppFrame afMock ;
    private static LoginFrame lfMock ;
    private static QuestionPanel qpMock;
    private static PromptHistory phMock;

    private static JButton createButton;
    private static JButton loginButton;

	private static LoginMediator testLogic;

    @BeforeAll
	public static void setUp(){
		ArrayList<LoginButtonsSubject> allButtons = new ArrayList<LoginButtonsSubject>();
		
        //Mock classes
        mongoDBMock = mock(MongoDB.class);
        errorMessagesMock = mock(ErrorMessages.class);
        sfWriterMock = mock(SavefileWriter.class);
        lfMock = mock(LoginFrame.class);
        afMock = mock(AppFrame.class);
        lpMock = mock(LoginPanel.class);
        qpMock = mock(QuestionPanel.class);
        phMock = mock(PromptHistory.class);

        //Mock Method calls
        when(afMock.getQuestionPanel()).thenReturn(qpMock);
        when(afMock.getPromptHistory()).thenReturn(phMock);
        when(lfMock.getLoginPanel()).thenReturn(lpMock);
        when(lpMock.getLoginButton()).thenReturn(loginButton = new JButton());
        when(lpMock.getcreateAccountButton()).thenReturn(createButton = new JButton());

		testLogic = new LoginMediator(lfMock, afMock, mongoDBMock,errorMessagesMock, sfWriterMock);
    }
	/*
	 * Scenario 1: User creates account with unique email and valid password
	 * Given I’m on the create account/login page
	 * And have never used my information to create an account
	 * When I enter my email
	 * And the same new password twice
	 * And I click “Create Account” button
	 * Then an account associated with that email and password is created
	 */
	@Test
	void test_1_1_UniqueEmailAndPassword() throws Exception {
		String expectedErrorMessage = "Email taken";
		String availableEmail = "123";
		String ValidPassword = "password";
		String takenEmail = "12345";
		when(lpMock.getEmail()).thenReturn(takenEmail);
		when(lpMock.getPass1()).thenReturn(ValidPassword);
		when(lpMock.getPass2()).thenReturn(ValidPassword);
		when(mongoDBMock.checkEmailExists(availableEmail)).thenReturn(false);
		when(mongoDBMock.checkEmailExists(takenEmail)).thenReturn(true);
		testLogic.onCreateAccount();
		verify(errorMessagesMock).showErrorMessage(expectedErrorMessage);
	}
	
	/*
	 * Scenario 2: User tries to create account with an existing email
	 * Given I’m on the login page
	 * And I have already created an account with my email
	 * When I enter my email and password
	 * And I click “Create Account” button
	 * Then I see an error message letting me know that the email is taken
	 * And I am returned to the create account/login page
	 */
	@Test
	void test_2_1_NoExistingEmail() throws Exception {
		String expectedErrorMessage = "Email taken";
		String availableEmail = "123";
		String ValidPassword = "password";
		String takenEmail = "12345";
		when(lpMock.getEmail()).thenReturn(availableEmail);
		when(lpMock.getPass1()).thenReturn(ValidPassword);
		when(lpMock.getPass2()).thenReturn(ValidPassword);
		when(mongoDBMock.checkEmailExists(availableEmail)).thenReturn(false);
		when(mongoDBMock.checkEmailExists(takenEmail)).thenReturn(true);
		testLogic.onCreateAccount();
		verify(mongoDBMock).createAccount(availableEmail,ValidPassword,ValidPassword);	
	}

	
	
	/*
	 * Scenario 3: User tries to create account with mismatching passwords
	 * Given I’m on the create account/login page
	 * And have never used my information to create an account
	 * When I enter my email
	 * And two different new passwords
	 * And I click “Create Account” button
	 * Then I see an error message stating that the passwords do not match
	 * And I am returned to the create account/login page
	 */
	
	 @Test
	void test_3_1_DifferentPasswords() {
		String invalidInput = "";
		String someInput = "123";
		String testPass1 = "12345";
		String testPass2 = "12346";
		String expectedErrorMessage = "Passwords do not match";

		when(lpMock.getEmail()).thenReturn(someInput);
		when(lpMock.getPass1()).thenReturn(testPass1);
		when(lpMock.getPass2()).thenReturn(testPass2);
		testLogic.onCreateAccount();
		verify(errorMessagesMock).showErrorMessage(expectedErrorMessage);

	}
	
	// /*
	//  * Scenario 4: User tries to create account without filling all necessary fields
	//  * Given I’m on the login page
	//  * And I fail to fill out my email, password (twice), or both
	//  * When I click “Create Account” button
	//  * Then an error message pops up asking me to enter both an email and password (twice) in order to create an account
	//  * And I am returned to the create account/login page
	//  */
	@Test
	void test_4_1_MissingEmail() {
		String invalidInput = "";
		String expectedErrorMessage = "Missing Email";

		when(lpMock.getEmail()).thenReturn(invalidInput);
		testLogic.onCreateAccount();
		verify(errorMessagesMock).showErrorMessage(expectedErrorMessage);

	}
	@Test
	void test_4_2_MissingPass1() {
		String invalidInput = "";
		String someInput = "123";
		String expectedErrorMessage = "Missing Pass1";

		when(lpMock.getEmail()).thenReturn(someInput);
		when(lpMock.getPass1()).thenReturn(invalidInput);
		testLogic.onCreateAccount();
		verify(errorMessagesMock).showErrorMessage(expectedErrorMessage);

	}

	@Test
	void test_4_3_MissingPass2() {
		String invalidInput = "";
		String someInput = "123";
		String expectedErrorMessage = "Missing Pass2";

		when(lpMock.getEmail()).thenReturn(someInput);
		when(lpMock.getPass1()).thenReturn(someInput);
		when(lpMock.getPass2()).thenReturn(invalidInput);
		testLogic.onCreateAccount();
		verify(errorMessagesMock).showErrorMessage(expectedErrorMessage);

	}

}
