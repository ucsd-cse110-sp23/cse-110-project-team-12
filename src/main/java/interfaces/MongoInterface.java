package interfaces;

import javax.swing.DefaultListModel;

public interface MongoInterface {
	boolean checkEmailExists(String email);
	String checkPass(String email);
	void createAccount(String emailField, String passwordField, String passCheck);
	boolean login(String email, String pass) throws Exception;
	DefaultListModel<String> getPrompts(String email);
	void mongoToServer(String email);
}