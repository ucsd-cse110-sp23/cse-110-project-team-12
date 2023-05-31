import org.junit.jupiter.api.Test;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.BeforeEach;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;


//Following imports are necessary for MongoDB
/*
import java.net.UnknownHostException;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
*/
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.io.*;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateAccountTest {
	private static final Random rand = new Random();
	String takenEmail = "user@gmail.com";
	String newEmail = "newUser" + rand.nextInt() + "@gmail.com";
	String defaultPass = "defaultPass";
	String matchPass = "defaultPass";
	String mismatchPass = "wrong";
	
	/*
	 * Scenario 1: User creates account with unique email and valid password
	 * Given I’m on the create account/login page
	 * And have never used my information to create an account
	 * When I enter my email
	 * And the same new password twice
	 * And I click “Create Account” button
	 * Then an account associated with that email and password is created
	 * And the app continues to the prompt/message page
	 */
	@Test
	void testUniqueEmailAndPassword() {
		//when i enter my email and password twice
		final LoginScreenMock screen = new LoginScreenMock(newEmail, defaultPass, matchPass);
		//and i click "Create Account" button
		CreateAccountListenerMock createMock = new CreateAccountListenerMock(screen);
		
		//check my account was added to the database
		String databaseName = "SayItAssistant";
		String collectionName = "TestUsers";
	    	
	    String uri = "mongodb+srv://juli:Pyys5stHYEsnDtJw@cluster0.w01dcya.mongodb.net/?retryWrites=true&w=majority";
	    
	    //configure
	    Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
		mongoLogger.setLevel(Level.OFF);
		try (MongoClient mongoClient = MongoClients.create(uri)) {
			
	        MongoDatabase userCluster = mongoClient.getDatabase(databaseName);
	        MongoCollection<Document> entries = userCluster.getCollection(collectionName);
	        
	        Bson filter = eq("email_address", newEmail);
	    }
		//check switch to main app
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
	void testExistingEmail() {
		
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
		
	}

}
