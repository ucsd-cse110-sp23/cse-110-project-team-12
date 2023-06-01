package mock;

import java.util.HashMap;

import interfaces.MongoInterface;

public class MongoDBMock implements MongoInterface {
	
	HashMap<String,String> entries;
	
	public MongoDBMock() {
		entries = new HashMap<String,String>();
	}

	@Override
	public boolean checkEmail(String email) {
		return entries.containsKey(email);
	}

	@Override
	public String checkPass(String email) {
		return entries.get(email);
	}

	@Override
	public void createAccount(String emailField, String passwordField) {
		entries.put(emailField, passwordField);
		
	}

}
