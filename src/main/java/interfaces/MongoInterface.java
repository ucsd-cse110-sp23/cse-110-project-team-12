package interfaces;

import javax.swing.DefaultListModel;

import api.Entry;

public interface MongoInterface {
	boolean checkEmail(String email);
	String checkPass(String email);
	void createAccount(String emailField, String passwordField, String passCheck) throws Exception;
	boolean login(String email, String pass) throws Exception;
	DefaultListModel<String> getPrompts(String email);
	void mongoToServer(String email);
}
