package api;
import org.bson.*;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;

import interfaces.MongoInterface;
import server.ServerCalls;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class MongoDB implements MongoInterface {

	private static final String URI = "mongodb+srv://juli:Pyys5stHYEsnDtJw@cluster0.w01dcya.mongodb.net/?retryWrites=true&w=majority";
	private static final String DATABASENAME = "SayItAssistant";
	private static final String COLLECTION = "Users";
	
	@Override
	public boolean checkEmail(String email) {
	    	 
        final Bson filter = eq("email_address", email);
        try (MongoClient mongoClient = MongoClients.create(URI)) {
			
			final MongoDatabase userCluster = mongoClient.getDatabase(DATABASENAME);
            final MongoCollection<Document> entries = userCluster.getCollection(COLLECTION);
	        if (entries.find(filter).first() != null) {
	        	return true;
	        }
        }

		return false;
	}
	
	/*
	 * @param - user email
	 * @return - String user password
	 */
	@Override
	public String checkPass(String email) {
		
		final Bson filter = eq("email_address", email);
		try (MongoClient mongoClient = MongoClients.create(URI)) {
			
			final MongoDatabase userCluster = mongoClient.getDatabase(DATABASENAME);
            final MongoCollection<Document> entries = userCluster.getCollection(COLLECTION);
            final Document foundDoc = entries.find(filter).first();
            return (String) (foundDoc.get("password"));
		}
	
	}

	/*
	 * stores user account in mongo
	 */
	@Override
	public void createAccount(String email, String pass1, String pass2) throws Exception {
		if (email.isBlank()) throw new Exception("Missing Email");
		if (pass1.isBlank()) throw new Exception("Missing First Password");
		if (pass2.isBlank()) throw new Exception("Missing Password Authentication");
		
		try (MongoClient mongoClient = MongoClients.create(URI)) {
			
			MongoDatabase userCluster = mongoClient.getDatabase(DATABASENAME);
            MongoCollection<Document> entries = userCluster.getCollection(COLLECTION);
            if (pass1.equals(pass2)) {
    			if (!checkEmail(email)) {
    				Document user = new Document("_id", new ObjectId());
    		        user.append("email_address", email)
    		            .append("password", pass1);
    		        //insert user's email and password into database
    		        entries.insertOne(user);
    		        
    			} else {
    				JOptionPane.showMessageDialog(null, "Email taken");
    				throw new Exception("Email taken");
    			}
    		} else {
    			JOptionPane.showMessageDialog(null, "Passwords do not match");
    			throw new Exception("Passwords do not match");
    		} 
		}
			  
    }
	
	/*
	 * checks for valid info
	 * @param email - user email
	 * @param pass1 - user password
	 * @return boolean - if login was successful, return true
	 */
	@Override
	public boolean login(String email, String pass1) throws Exception {
		if (email.isBlank()) {
			throw new Exception("Missing Email");
		}
		if (pass1.isBlank()) {
			throw new Exception("Missing Password");
		}
		
		final Bson filter1 = eq("email_address", email);
		final Bson filter2 = Filters.and(eq("email_address", email), eq("password", pass1));
		
		try (MongoClient mongoClient = MongoClients.create(URI)) {
			
			final MongoDatabase userCluster = mongoClient.getDatabase(DATABASENAME);
            final MongoCollection<Document> entries = userCluster.getCollection(COLLECTION);
            
			//check if email exists
			if (entries.find(filter1).first() != null) {
				//check that password is correct for email
				if (entries.find(filter2).first() != null) {
					//account exists so user logs in
					return true;
					//mongoToServer(email);
					//return entriesToPrompts(email);
					
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
	
	/* 
	 * Helper for entriesToPrompts(String email)
	 * assumes valid login attempt ie existing email
	 */
	private ArrayList<Document> getUserEntries(String email) {
		//DefaultListModel<String> list = new DefaultListModel<String>();		
		try (MongoClient mongoClient = MongoClients.create(URI)) {
			
			final MongoDatabase sayIt = mongoClient.getDatabase(DATABASENAME);
            final MongoCollection<Document> users = sayIt.getCollection(COLLECTION);
            @SuppressWarnings("unchecked")
			final ArrayList<Document> userEntries = (ArrayList<Document>)users.find(eq("email_address", email)).first().get("entries"); 
            return userEntries;
		}	
	}
	
	/*
	 * returns list of "command: prompt" stored in mongo
	 */
	@Override
	public DefaultListModel<String> getPrompts(String email) {
		final ArrayList<Document> entries = getUserEntries(email);
		final DefaultListModel<String> list = new DefaultListModel<>();
		entries.forEach(entry -> {
			final String command = entry.getString("command");
			final String prompt = entry.getString("prompt");
			final String listEntry = command + ": " + prompt;
			list.addElement(listEntry);
		});
		return list;
	}
	
	/*
	 * returns list Documents of Entry class from mongo
	 */
	private DefaultListModel<Entry> mongoToEntryList(String email) {
		final ArrayList<Document> entries = getUserEntries(email);
		final DefaultListModel<Entry> list = new DefaultListModel<>();
		entries.forEach(entry -> {
			final String command = entry.getString("command");
			final String prompt = entry.getString("prompt");
			final String result = entry.getString("result");
			final Entry listEntry = new Entry(command, prompt, result);
			list.addElement(listEntry);
		});
		return list;
	}
	
	/*
	 * pushes documents from mongo to server
	 */
	@Override
	public void mongoToServer(String email) {
		final DefaultListModel<Entry> list = mongoToEntryList(email);
		for(int i = 0; i < list.size(); i++) {
			final Entry entry = list.get(i);
			final String title = entry.getTitle();
			final String result = entry.getResult();
			ServerCalls.postToServer(title, result);
		}
	}
	
}