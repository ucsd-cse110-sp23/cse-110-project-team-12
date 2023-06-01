package interfaces;


public interface MongoInterface {
	public boolean checkEmail(String email);
	public String checkPass(String email);
	public void createAccount(String emailField, String passwordField);
}
