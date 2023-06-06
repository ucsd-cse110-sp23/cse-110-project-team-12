import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

//import org.junit.Assert;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

//import org.junit.Assert.assertFalse;
//import org.junit.Assert.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import processing.ErrorMessages;
import interfaces.ErrorMessagesInterface;
import interfaces.LoginButtonsSubject;
import listeners.CreateAccountListener;
import mainframe.LoginPanel;
import mediators.*;
import api.MongoDB;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import java.security.SecureRandom;

public class DS8CreateAccountTest {
    
    private static MongoDB mongoDB;
    private static ErrorMessagesInterface errorMessagesMock ;
    private static LoginPanel LoginPanelMock;
	private static CreateAccountListener createListener;
    private static JButton createButton;

	private static LoginMediator testLogic;
	
	private final Random rand = new SecureRandom();

    @BeforeAll
	public static void setUp(){
		final ArrayList<LoginButtonsSubject> allButtons = new ArrayList<LoginButtonsSubject>();
		mongoDB = new MongoDB();
		mongoDB.setCollection("TestUsers");
        errorMessagesMock = mock(ErrorMessages.class);
		LoginPanelMock = mock(LoginPanel.class);
		
       	createButton = new JButton();
		createListener = new CreateAccountListener();
        createButton.addActionListener(createListener);
		allButtons.add(createListener);
		testLogic = new LoginMediator(allButtons, LoginPanelMock, mongoDB,errorMessagesMock);
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
    	//generate random email
		final String availableEmail = Integer.toString(rand.nextInt());
		final String ValidPassword = "password";
		
		when(LoginPanelMock.getEmail()).thenReturn(availableEmail);
		when(LoginPanelMock.getPass1()).thenReturn(ValidPassword);
		when(LoginPanelMock.getPass2()).thenReturn(ValidPassword);
		
		assertFalse(mongoDB.checkEmailExists(availableEmail));
		
		testLogic.onCreateAccount();
		verify(mongoDB).createAccount(availableEmail,ValidPassword,ValidPassword);
		
		assertTrue(mongoDB.checkEmailExists(availableEmail));
	}
	
	
	/*
	 * Scenario 2: User tries to create account with an existing email
	 * Given I’m on the login page
	 * And I have already created an account with my email
	 * When I enter my email and password
	 * And I click “Create Account” button
	 * Then I see an error message letting me know that the email is taken
	 * And I am returned to the create account/login page
	 
	
	@Test
	void test_1_1_ExistingEmailError() throws Exception {
		String expectedErrorMessage = "Email taken";
		String availableEmail = "123";
		String ValidPassword = "password";
		String takenEmail = "12345";
		mongoDB.createAccount(takenEmail, ValidPassword, ValidPassword);
		when(LoginPanelMock.getEmail()).thenReturn(takenEmail);
		when(LoginPanelMock.getPass1()).thenReturn(ValidPassword);
		when(LoginPanelMock.getPass2()).thenReturn(ValidPassword);
		when(mongoDB.checkEmailExists(availableEmail)).thenReturn(false);
		when(mongoDB.checkEmailExists(takenEmail)).thenReturn(true);
		testLogic.onCreateAccount();
		verify(errorMessagesMock).showErrorMessage(expectedErrorMessage);
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
	 
	
	 @Test
	void test_3_1_DifferentPasswords() {
		String invalidInput = "";
		String someInput = "123";
		String testPass1 = "12345";
		String testPass2 = "12346";
		String expectedErrorMessage = "Passwords do not match";

		when(LoginPanelMock.getEmail()).thenReturn(someInput);
		when(LoginPanelMock.getPass1()).thenReturn(testPass1);
		when(LoginPanelMock.getPass2()).thenReturn(testPass2);
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
	//  
	@Test
	void test_4_1_MissingEmail() {
		String invalidInput = "";
		String expectedErrorMessage = "Missing Email";

		when(LoginPanelMock.getEmail()).thenReturn(invalidInput);
		testLogic.onCreateAccount();
		verify(errorMessagesMock).showErrorMessage(expectedErrorMessage);

	}
	@Test
	void test_4_2_MissingPass1() {
		String invalidInput = "";
		String someInput = "123";
		String expectedErrorMessage = "Missing Pass1";

		when(LoginPanelMock.getEmail()).thenReturn(someInput);
		when(LoginPanelMock.getPass1()).thenReturn(invalidInput);
		testLogic.onCreateAccount();
		verify(errorMessagesMock).showErrorMessage(expectedErrorMessage);

	}

	@Test
	void test_4_3_MissingPass2() {
		String invalidInput = "";
		String someInput = "123";
		String expectedErrorMessage = "Missing Pass2";

		when(LoginPanelMock.getEmail()).thenReturn(someInput);
		when(LoginPanelMock.getPass1()).thenReturn(someInput);
		when(LoginPanelMock.getPass2()).thenReturn(invalidInput);
		testLogic.onCreateAccount();
		verify(errorMessagesMock).showErrorMessage(expectedErrorMessage);

	}*/

}
