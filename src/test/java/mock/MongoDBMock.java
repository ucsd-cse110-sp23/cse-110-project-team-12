package mock;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import org.bson.Document;
import org.bson.types.ObjectId;

import interfaces.MongoInterface;
import mainframe.app;

/*
 * mocks data base storage of emails and passwords with hashmap
 */
public class MongoDBMock implements MongoInterface {
	
	Map<String,String> entries;
	
	//create empty database
	public MongoDBMock() {
		entries = new HashMap<String,String>();
		
	}
	
	//create database with one fake user
	public MongoDBMock(String email, String pass) {
		entries = new HashMap<String,String>();
		entries.put(email, pass);
		
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
	public void createAccount(String email, String pass1, String pass2) throws Exception {
		if (email.isBlank()) throw new Exception("Missing Email");
		if (pass1.isBlank()) throw new Exception("Missing First Password");
		if (pass2.isBlank()) throw new Exception("Missing Password Authentication");
		
		if (pass1.equals(pass2)) {
			if (!checkEmail(email)) {
				entries.put(email, pass1);
			} else {
				throw new Exception("Email taken");
			}
		} else {
			throw new Exception("Passwords do not match");
		}
	}

	@Override
	public void login(String email, String pass) throws Exception {
		if (email.isBlank()) throw new Exception("Missing Email");
		if (pass.isBlank()) throw new Exception("Missing First Password");
		
		//check if email exists
		if (entries.containsKey(email)) {
			//check that password is correct for email
			if (entries.get(email).equals(pass)) {
				//account exists so user logs in
			} else {
				JOptionPane.showMessageDialog(null, "Incorrect Password");
				throw new Exception("Incorrect password");
			}
		} else {
			JOptionPane.showMessageDialog(null, "Email Unrecognized");
			throw new Exception("Email Unrecognized");
		}
		
	}

}
