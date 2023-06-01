
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import mock.*;
import java.util.Random;

public class CreateAccountTest {
	
	private static final Random rand = new Random();
	String takenEmail = "user@gmail.com";
	String newEmail = "newUser" + rand.nextInt() + "@gmail.com";
	String defaultPass = "test";
	String matchPass = "test";
	String mismatchPass = "wrong";
	
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
	void testUniqueEmailAndPassword() throws Exception {
		
		MongoDBMock mockMongoSession = new MongoDBMock();
		boolean emailExistsBefore = mockMongoSession.checkEmail(newEmail);
		
		//email should NOT be taken so should be FALSE
		assertFalse(emailExistsBefore);
		
		//add new user to database
		mockMongoSession.createAccount(newEmail, defaultPass, matchPass);
	    
	    //check that account associated with email created
		boolean emailExistsAfter = mockMongoSession.checkEmail(newEmail);
		assertTrue(emailExistsAfter);
		//check that correct password stored
		String storedPass = mockMongoSession.checkPass(newEmail);
		assertEquals(defaultPass, storedPass);
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
	void testExistingEmail() throws Exception {
		
		//email exists in database
		MongoDBMock mockMongoSession = new MongoDBMock(takenEmail,defaultPass);
		
		//check for email in database
		boolean emailExistsBefore = mockMongoSession.checkEmail(takenEmail);
		assertTrue(emailExistsBefore);
		
		//error message seen when attempting to reuse email
		Exception e = assertThrows(Exception.class, () -> {
			mockMongoSession.createAccount(takenEmail, defaultPass, matchPass);
		});
		assertTrue(e.getMessage().contains("Email taken"));
		
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
	void testDiffPasswords() {
		
		MongoDBMock mockMongoSession = new MongoDBMock();
		
		//error message seen when attempting to create account with diff passwords
		Exception e = assertThrows(Exception.class, () -> {
			mockMongoSession.createAccount(newEmail, defaultPass, mismatchPass);
		});
		assertTrue(e.getMessage().contains("Passwords do not match"));
		
	}
	
	/*
	 * Scenario 4: User tries to create account without filling all necessary fields
	 * Given I’m on the login page
	 * And I fail to fill out my email, password (twice), or both
	 * When I click “Create Account” button
	 * Then an error message pops up asking me to enter both an email and password (twice) in order to create an account
	 * And I am returned to the create account/login page
	 */
	@Test
	void testMissingFields() {
		
		MongoDBMock mockMongoSession = new MongoDBMock();
		
		//error message seen when attempting to create account without email
		Exception e1 = assertThrows(Exception.class, () -> {
			mockMongoSession.createAccount(new String(), defaultPass, matchPass);
		});
		assertTrue(e1.getMessage().contains("Missing Email"));
		
		//error message seen when attempting to create account without pass1
		Exception e2 = assertThrows(Exception.class, () -> {
			mockMongoSession.createAccount(newEmail, new String(), matchPass);
		});
		assertTrue(e2.getMessage().contains("Missing First Password"));
		
		//error message seen when attempting to create account without pass2
		Exception e3 = assertThrows(Exception.class, () -> {
			mockMongoSession.createAccount(newEmail, defaultPass, new String());
		});
		assertTrue(e3.getMessage().contains("Missing Password Authentication"));
	}

}
