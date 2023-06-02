package interfaces;

import javax.swing.DefaultListModel;

public interface MongoInterface {
	public boolean checkEmail(String email);
	public String checkPass(String email);
	public void createAccount(String emailField, String passwordField, String passCheck) throws Exception;
	public DefaultListModel<String> login(String email, String pass) throws Exception;
}
