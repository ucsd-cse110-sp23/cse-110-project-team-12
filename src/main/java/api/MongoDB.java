package api;
import org.bson.*;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;

import interfaces.MongoInterface;
import mainframe.app;
import server.ServerCalls;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.descending;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class MongoDB implements MongoInterface {

	private static final String URI = "mongodb+srv://juli:Pyys5stHYEsnDtJw@cluster0.w01dcya.mongodb.net/?retryWrites=true&w=majority";
	private static final String DATABASENAME = "SayItAssistant";
	private static final String COLLECTION = "Users";
	
	public boolean checkEmail(String email) {
	    	 
        Bson filter = eq("email_address", email);
        try (MongoClient mongoClient = MongoClients.create(URI)) {
			
			MongoDatabase userCluster = mongoClient.getDatabase(DATABASENAME);
            MongoCollection<Document> entries = userCluster.getCollection(COLLECTION);
	        if (entries.find(filter).first() != null) {
	        	return true;
	        }
        }

		return false;
	}
	
	/*
	 * param - email
	 * returns email's password
	 */
	public String checkPass(String email) {
		
		Bson filter = eq("email_address", email);
		try (MongoClient mongoClient = MongoClients.create(URI)) {
			
			MongoDatabase userCluster = mongoClient.getDatabase(DATABASENAME);
            MongoCollection<Document> entries = userCluster.getCollection(COLLECTION);
            Document foundDoc = entries.find(filter).first();
            return (String) (foundDoc.get("password"));
		}
	
	}

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
	
	public DefaultListModel<String> login(String email, String pass1) throws Exception {
		if (email.isBlank()) throw new Exception("Missing Email");
		if (pass1.isBlank()) throw new Exception("Missing Password");
		
		Bson filter1 = eq("email_address", email);
		Bson filter2 = Filters.and(eq("email_address", email), eq("password", pass1));
		
		try (MongoClient mongoClient = MongoClients.create(URI)) {
			
			MongoDatabase userCluster = mongoClient.getDatabase(DATABASENAME);
            MongoCollection<Document> entries = userCluster.getCollection(COLLECTION);
            
			//check if email exists
			if (entries.find(filter1).first() != null) {
				//check that password is correct for email
				if (entries.find(filter2).first() != null) {
					//account exists so user logs in
					mongoToServer(email);
					return entriesToPrompts(email);
					
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
	 * assumes valid login attempt ie existing email
	 */
	private ArrayList<Document> getUserEntries(String email) {
		//DefaultListModel<String> list = new DefaultListModel<String>();		
		try (MongoClient mongoClient = MongoClients.create(URI)) {
			
			MongoDatabase sayIt = mongoClient.getDatabase(DATABASENAME);
            MongoCollection<Document> users = sayIt.getCollection(COLLECTION);
            ArrayList<Document> userEntries = (ArrayList<Document>)users.find(eq("email_address", email)).first().get("entries"); 
            return userEntries;
		}	
	}
	
	public DefaultListModel<String> entriesToPrompts(String email) {
		ArrayList<Document> entries = getUserEntries(email);
		DefaultListModel<String> list = new DefaultListModel<String>();
		for (Document entry : entries) {
			String command = entry.getString("command");
			String prompt = entry.getString("prompt");
			String listEntry = command + ": " + prompt;
			list.addElement(listEntry);
		}
		return list;
	}
	
	public DefaultListModel<Entry> mongoToEntryList(String email) {
		ArrayList<Document> entries = getUserEntries(email);
		DefaultListModel<Entry> list = new DefaultListModel<Entry>();
		for (Document entry : entries) {
			String command = entry.getString("command");
			String prompt = entry.getString("prompt");
			String result = entry.getString("result");
			Entry listEntry = new Entry(command, prompt, result);
			list.addElement(listEntry);
		}
		return list;
	}
	
	public void mongoToServer(String email) {
		DefaultListModel<Entry> list = mongoToEntryList(email);
		for(int i = 0; i < list.size(); i++) {
			Entry entry = list.get(i);
			String title = entry.getTitle();
			String result = entry.getResult();
			ServerCalls.postToServer(title, result);
		}
	}
	
	
	
/*	private String getResult(String prompt) {
		
	}*/
	
	
}
