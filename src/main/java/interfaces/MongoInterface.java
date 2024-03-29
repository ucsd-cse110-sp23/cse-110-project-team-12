/**
 * @author CSE 110 - Team 12
 */
package interfaces;

import java.util.ArrayList;

import processing.Entry;

/**
 * MongoDB methods
 *
 */
public interface MongoInterface {
	boolean checkEmailExists(String email);
	void createAccount(String emailField, String passwordField, String passCheck);
	boolean checkValidLogin(String email, String pass);
	
	//US9-3,4: Retrieve User-Data: (Prompt History)
	ArrayList<Entry> retrieveSavedPromptHistory(); 
	
	//US9-3,4 Post User-Data: (Prompt History)
	void updateSavedPromptHistory(ArrayList<Entry> history);


}