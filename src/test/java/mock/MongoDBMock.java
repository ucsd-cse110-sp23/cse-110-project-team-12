package mock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import org.bson.Document;

import api.Entry;
import interfaces.MongoInterface;
import server.ServerCalls;

/*
 * mocks data base storage of emails and passwords with hashmap
 */
public class MongoDBMock implements MongoInterface {
	
	Map<String,ArrayList<Object>> entries;
	ArrayList<Entry> userQueries = new ArrayList<>();
	
	//create database with one fake user
	public MongoDBMock(String email, String pass) throws Exception {
		
		entries = new HashMap<>();
		createAccount(email,pass,pass);
		
	}
	
	public MongoDBMock() {
		entries = new HashMap<>();
	}

	@Override
	public boolean checkEmail(String email) {
		
		return entries.containsKey(email);
	}

	@Override
	public String checkPass(String email) {
		return (String) entries.get(email).get(0);
	}

	@Override
	public void createAccount(String email, String pass1, String pass2) throws Exception {
		if (email.isBlank()) {
			throw new Exception("Missing Email");
		}
		if (pass1.isBlank()) {
			throw new Exception("Missing First Password");
		}
		if (pass2.isBlank()) {
			throw new Exception("Missing Password Authentication");
		}
		
		if (pass1.equals(pass2)) {
			if (!checkEmail(email)) {
				List<Serializable> passAndEntries = Arrays.asList(pass1,userQueries);
				entries.put(email, new ArrayList<>(passAndEntries));
			} else {
				throw new Exception("Email taken");
			}
		} else {
			throw new Exception("Passwords do not match");
		}
	}

	@Override
	public boolean login(String email, String pass) throws Exception {
		if (email.isBlank()) {
			throw new Exception("Missing Email");
		}
		if (pass.isBlank()) {
			throw new Exception("Missing First Password");
		}
		
		//check if email exists
		if (entries.containsKey(email)) {
			//check that password is correct for email
			if (entries.get(email).get(0).equals(pass)) {
				return true;
			} else {
				JOptionPane.showMessageDialog(null, "Incorrect Password");
				throw new Exception("Incorrect password");
			}
		} else {
			JOptionPane.showMessageDialog(null, "Email Unrecognized");
			throw new Exception("Email Unrecognized");
		}
		
	}

	@Override
	public DefaultListModel<String> getPrompts(String email) {
		final ArrayList<Entry> userEntries = mongoToEntryList(email);
		final DefaultListModel<String> list = new DefaultListModel<>();
		userEntries.forEach(entry -> {
			final String command = entry.getCommand();
			final String prompt = entry.getPrompt();
			final String listEntry = command + ": " + prompt;
			list.addElement(listEntry);
		});
		return list;
	}
	

	@SuppressWarnings("unchecked")
	private ArrayList<Entry> mongoToEntryList(String email) {
		return (ArrayList<Entry>) entries.get(email).get(1);
	}

	@Override
	public void mongoToServer(String email) {
		final ArrayList<Entry> list = mongoToEntryList(email);
		for(int i = 0; i < list.size(); i++) {
			final Entry entry = list.get(i);
			final String title = entry.getTitle();
			final String result = entry.getResult();
			ServerCalls.postToServer(title, result);
		}
		
	}

}
