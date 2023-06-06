/*import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import processing.ErrorMessages;
import interfaces.ErrorMessagesInterface;
import interfaces.LoginButtonsSubject;
import interfaces.MongoInterface;
import listeners.CreateAccountListener;
import mainframe.*;
import mediators.*;
import api.MongoDB;

import java.util.ArrayList;

import javax.swing.JButton;

public class DS9LoginTest {
    private static MongoInterface mongoDBMock;
    private static ErrorMessagesInterface errorMessagesMock ;
    private static LoginPanel LoginPanelMock;
    private static AppFrame AppFrameMock ;
    private static LoginFrame LoginFrameMock ;

	private static CreateAccountListener createListener;
    private static JButton createButton;

	private static LoginMediator testLogic;

    @BeforeAll
	public static void setUp(){
		ArrayList<LoginButtonsSubject> allButtons = new ArrayList<LoginButtonsSubject>();
		
        mongoDBMock = mock(MongoDB.class);
        errorMessagesMock = mock(ErrorMessages.class);
		LoginPanelMock = mock(LoginPanel.class);
        LoginFrameMock = mock(LoginFrame.class);
        AppFrameMock = mock(AppFrame.class);

       	createButton = new JButton();
		createListener = new CreateAccountListener();
        createButton.addActionListener(createListener);
		allButtons.add(createListener);
		testLogic = new LoginMediator(allButtons, LoginPanelMock, mongoDBMock,errorMessagesMock);
        testLogic.registerObserver(AppFrameMock);
        testLogic.registerObserver(LoginFrameMock);
    }

	//     Scenario 1: User logs in with registered email and password
	//      Given that I already have an account 
	//      And I’m on the create account/login page
	// 		When I enter my registered email in the email field
	// 		And I enter my password in the password field
	// 		And press the “Log In” button
	// 		Then I’m logged into my account
	// And the app continues to the prompt/message page
	
	@Test
	void test_1_1_ValidEmail() throws Exception {
		String takenEmail = "12345";
        String ValidPassword = "password";
        when(LoginPanelMock.getEmail()).thenReturn(takenEmail);
		when(LoginPanelMock.getPass1()).thenReturn(ValidPassword);
        when(mongoDBMock.checkValidLogin(takenEmail,ValidPassword)).thenReturn(true);
        testLogic.onLogin();
        verify(AppFrameMock).onLoginClosing();
        verify(LoginFrameMock).onLoginClosing();
}
		
	// Scenario 2: User tries to login with unregistered email
	// 	Given that I already have an account
	// 	And I’m on the create account/login page
	// 	When I enter an email not linked to an existing account in the email field
	// 	And I enter a password in the password field
	// 	And I press the “Log In” button
	// 	Then an error message pops up saying the email is not recognized
	// And I am returned to the create account/login page
	
	@Test
	void test_2_1_inValidEmail() throws Exception {
		String newEmail = "12345";
        String arbitraryPassword = "password";
        String expectedErrorMessage = "Incorrect Login Details";
        when(LoginPanelMock.getEmail()).thenReturn(newEmail);
		when(LoginPanelMock.getPass1()).thenReturn(arbitraryPassword);
        when(mongoDBMock.checkValidLogin(newEmail,arbitraryPassword)).thenReturn(false);
        testLogic.onLogin();
        verify(errorMessagesMock).showErrorMessage(expectedErrorMessage);
        
}

	// Scenario 3: User tries to log in without filling out all necessary fields
	// 	Given that I already have an account
	// 	And I’m on the create account/login page
	// 	And I fail to fill out my email, password, or both
	// 	When I press the “Log In” button
	// 	Then an error message pops up
	// And I am returned to the create account/login page
	@Test
	void test_3_1_missingEmail() throws Exception {
		String emptyEmail = "";
        String arbitraryPassword = "password";
        String expectedErrorMessage = "Missing Email";
        when(LoginPanelMock.getEmail()).thenReturn(emptyEmail);
		when(LoginPanelMock.getPass1()).thenReturn(arbitraryPassword);
        testLogic.onLogin();
        verify(errorMessagesMock).showErrorMessage(expectedErrorMessage);
        
}

	//Scenario 4: User tries to log in with wrong password
	//	Given that I already have an account
	//	And I’m on the create account/login page
	//	When I enter my registered email in the email field
	//	And I enter a wrong password in the password field
	//	And I press the “Log In” button
	//	Then an error message pops up saying the password is incorrect
	//And I am returned to the create account/login page
	@Test
	void test_3_2_missingPassword() throws Exception {
		String arbitraryEmail = "Email";
        String emptyPassword = "";
        String expectedErrorMessage = "Missing Password";
        when(LoginPanelMock.getEmail()).thenReturn(arbitraryEmail);
		when(LoginPanelMock.getPass1()).thenReturn(emptyPassword);
        testLogic.onLogin();
        verify(errorMessagesMock).showErrorMessage(expectedErrorMessage);
        
	}
	
//  Scenario 5: Returning user with entries logs in and sees their prompt history
	//      Given that I already have an account 
	//      And that account has stored entries
	//      And I’m on the create account/login page
	// 		When I enter my registered email in the email field
	// 		And I enter my password in the password field
	// 		And press the “Log In” button
	// 		Then I’m logged into my account
	//      And the app continues to the prompt/message page
	//      And I see my stored entries in the prompt history
	@Test
	void test_4_1_loadPromptsOnLogin() {
		
	}



}*/
