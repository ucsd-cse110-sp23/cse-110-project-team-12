package interfaces;

import java.util.ArrayList;

import processing.Entry;

public interface MongoInterface {
	boolean checkEmailExists(String email);
	String checkPass(String email);
	void createAccount(String emailField, String passwordField, String passCheck);
	boolean checkValidLogin(String email, String pass);
	//US9-3,4: Retrieve User-Data: (Prompt History)
	ArrayList<Entry> retrieveSavedPromptHistory(); 
	//US9-3,4 Post User-Data: (Prompt History)
	void updateSavedPromptHistory(ArrayList<Entry> history);


}