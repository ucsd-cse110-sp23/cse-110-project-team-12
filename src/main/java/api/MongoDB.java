package api;
import org.bson.*;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;

import interfaces.MongoInterface;
import mainframe.app;

import static com.mongodb.client.model.Filters.eq;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class MongoDB implements MongoInterface {

	private static final String URI = "mongodb+srv://juli:Pyys5stHYEsnDtJw@cluster0.w01dcya.mongodb.net/?retryWrites=true&w=majority";
	private static final String DATABASENAME = "SayItAssistant";
	private static final String COLLECTION = "Users";
	private static final String EMAILADDRESS = "email_address";
	
	public boolean checkEmailExists(String email) {
	    	 
        Bson filter = eq(EMAILADDRESS, email);
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
		
		Bson filter = eq(EMAILADDRESS, email);
		try (MongoClient mongoClient = MongoClients.create(URI)) {
			
			MongoDatabase userCluster = mongoClient.getDatabase(DATABASENAME);
            MongoCollection<Document> entries = userCluster.getCollection(COLLECTION);
            Document foundDoc = entries.find(filter).first();
            return (String) (foundDoc.get("password"));
		}
	
	}

	public void createAccount(String email, String pass1, String pass2) {
		
		// try (MongoClient mongoClient = MongoClients.create(URI)) {
			MongoClient mongoClient = MongoClients.create(URI);
			MongoDatabase userCluster = mongoClient.getDatabase(DATABASENAME);
            MongoCollection<Document> entries = userCluster.getCollection(COLLECTION);
            
			Document user = new Document("_id", new ObjectId());
			user.append(EMAILADDRESS, email)
				.append("password", pass1);
			//insert user's email and password into database
			entries.insertOne(user);
			  
    }
	
	public boolean login(String email, String pass1) throws Exception {
		// if (email.isBlank()) throw new Exception("Missing Email");
		// if (pass1.isBlank()) throw new Exception("Missing Password");
		
		// Bson filter1 = eq(EMAILADDRESS, email);
		// Bson filter2 = Filters.and(eq(EMAILADDRESS, email), eq("password", pass1));
		
		// try (MongoClient mongoClient = MongoClients.create(URI)) {
			
		// 	MongoDatabase userCluster = mongoClient.getDatabase(DATABASENAME);
        //     MongoCollection<Document> entries = userCluster.getCollection(COLLECTION);
            
		// 	//check if email exists
		// 	if (entries.find(filter1).first() != null) {
		// 		//check that password is correct for email
		// 		if (entries.find(filter2).first() != null) {
		// 			//account exists so user logs in
		// 			app.succesfullLogin();
		// 		} else {
		// 			JOptionPane.showMessageDialog(null, "Incorrect Password");
		// 			throw new Exception("Incorrect password");
		// 		}
		// 	} else {
		// 		JOptionPane.showMessageDialog(null, "Email Unrecognized");
		// 		throw new Exception("Email Unrecognized");
		// 	}
	// 	}
		//<todo>
	return true;
	}

	@Override
	public DefaultListModel<String> getPrompts(String email) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getPrompts'");
	}

	@Override
	public void mongoToServer(String email) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'mongoToServer'");
	}
	
}
