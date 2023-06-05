package interfaces;

import javax.swing.DefaultListModel;

public interface MongoInterface {
	boolean checkEmailExists(String email);
	String checkPass(String email);
	void createAccount(String emailField, String passwordField, String passCheck);
	boolean checkValidLogin(String email, String pass);
	DefaultListModel<String> getPrompts(String email);
	void mongoToServer(String email);
}