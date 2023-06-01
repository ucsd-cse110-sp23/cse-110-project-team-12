package interfaces;


public interface MongoInterface {
	public boolean checkEmail(String email);
	public String checkPass(String email);
	public void createAccount(String emailField, String passwordField, String passCheck) throws Exception;
	public boolean login(String email, String pass) throws Exception;
}
