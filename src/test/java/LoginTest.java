
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import mock.*;
import java.util.Random;

public class LoginTest {
	
	private static final Random rand = new Random();
	String takenEmail = "user@gmail.com";
	String newEmail = "newUser" + rand.nextInt() + "@gmail.com";
	String defaultPass = "test";
	String matchPass = "test";
	String mismatchPass = "wrong";
	
	/*
	 * Scenario 1: User logs in with registered email and password
	 * Given that I already have an account 
	 * And I’m on the create account/login page
	 * When I enter my registered email in the email field
	 * And I enter my password in the password field
	 * And press the “Log In” button
	 * Then I’m logged into my account
	 * And the app continues to the prompt/message page
	 */
	@Test
	void testRegisteredEmailAndPass() throws Exception {
		//email exists in database
		MongoDBMock mockMongoSession = new MongoDBMock(takenEmail,defaultPass);
		
		//check for email in database
		boolean emailExistsBefore = mockMongoSession.checkEmail(takenEmail);
		assertTrue(emailExistsBefore);
		
		//check that login returns true
		assertTrue(mockMongoSession.login(takenEmail,defaultPass));
	}
	/*
	 * Scenario 2: User tries to login with unregistered email
	 * Given that I already have an account
	 * And I’m on the create account/login page
	 * When I enter an email not linked to an existing account in the email field
	 * And I enter a password in the password field
	 * And I press the “Log In” button
	 * Then an error message pops up saying the email is not recognized
	 * And I am returned to the create account/login page
	 */
	@Test
	void testUnregisteredEmail() throws Exception {
		MongoDBMock mockMongoSession = new MongoDBMock();
		
		//check for email in database
		boolean emailExistsBefore = mockMongoSession.checkEmail(newEmail);
		assertFalse(emailExistsBefore);
	
		//check that login returns error
		Exception e = assertThrows(Exception.class, () -> {
			mockMongoSession.login(newEmail,defaultPass);
		});
		assertTrue(e.getMessage().contains("Email Unrecognized"));
		
	}
	/*
	 * Scenario 3: User tries to log in with wrong password
	 * Given that I already have an account
	 * And I’m on the create account/login page
	 * When I enter my registered email in the email field
	 * And I enter a wrong password in the password field
	 * And I press the “Log In” button
	 * Then an error message pops up saying the password is incorrect
	 * And I am returned to the create account/login page
	 */
	@Test
	void testWrongPassword() throws Exception {
		//email exists in database
		MongoDBMock mockMongoSession = new MongoDBMock(takenEmail,defaultPass);
		
		//check for email in database
		boolean emailExistsBefore = mockMongoSession.checkEmail(takenEmail);
		assertTrue(emailExistsBefore);
		
		//check that login returns error
		Exception e = assertThrows(Exception.class, () -> {
			mockMongoSession.login(takenEmail,mismatchPass);
		});
		assertTrue(e.getMessage().contains("Incorrect password"));
		
	}
	
	/*
	 * Scenario 4: User tries to log in without filling out all necessary fields
	 * Given that I already have an account
	 * And I’m on the create account/login page
	 * And I fail to fill out my email, password, or both
	 * When I press the “Log In” button
	 * Then an error message pops up
	 */
	@Test
	void testMissingFields() {
		
		MongoDBMock mockMongoSession = new MongoDBMock(takenEmail,defaultPass);
		
		//error message seen when attempting to create account without email
		Exception e1 = assertThrows(Exception.class, () -> {
			mockMongoSession.login(new String(), defaultPass);
		});
		assertTrue(e1.getMessage().contains("Missing Email"));
		
		//error message seen when attempting to create account without pass1
		Exception e2 = assertThrows(Exception.class, () -> {
			mockMongoSession.login(newEmail, new String());
		});
		assertTrue(e2.getMessage().contains("Missing Password"));
		
	}
}