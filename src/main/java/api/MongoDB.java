package api;
import org.bson.*;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.*;

import interfaces.MongoInterface;

import static com.mongodb.client.model.Filters.eq;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoDB implements MongoInterface {

	private static final String URI = "mongodb+srv://juli:Pyys5stHYEsnDtJw@cluster0.w01dcya.mongodb.net/?retryWrites=true&w=majority";
	private static final String URI2 = "mongodb://juli:<password>@ac-lc7ua27-shard-00-00.w01dcya.mongodb.net:27017,ac-lc7ua27-shard-00-01.w01dcya.mongodb.net:27017,ac-lc7ua27-shard-00-02.w01dcya.mongodb.net:27017/?ssl=true&replicaSet=atlas-692u2t-shard-0&authSource=admin&retryWrites=true&w=majority";
	private static final String DATABASENAME = "SayItAssistant";
	private static final String COLLECTION = "Users";

	private MongoClient mongoClient;
	private MongoDatabase userCluster;
	private MongoCollection<Document> entries;
	
	public MongoDB() {
		mongoClient = MongoClients.create(URI);
		userCluster = mongoClient.getDatabase(DATABASENAME);
		entries = userCluster.getCollection(COLLECTION);
		//Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
		//mongoLogger.setLevel(Level.OFF);	
	}
	
	public boolean checkEmail(String email) {
	    	 
        Bson filter = eq("email_address", email);
        
        if (entries.find(filter).first() != null) {
        	return true;
        }

		return false;
	}
	
	/*
	 * param - email
	 * returns email's password
	 */
	public String checkPass(String email) {
		
		Bson filter = eq("email_address", email);
		Document foundDoc = entries.find(filter).first();
		return (String) (foundDoc.get("password"));
	}

	public void createAccount(String email, String pass1, String pass2) throws Exception {
		if (email.isBlank()) throw new Exception("Missing Email");
		if (pass1.isBlank()) throw new Exception("Missing First Password");
		if (pass2.isBlank()) throw new Exception("Missing Password Authentication");
		
		if (!pass1.equals(pass2)) {
			if (!checkEmail(email)) {
				Document user = new Document("_id", new ObjectId());
		        user.append("email_address", email)
		            .append("password", pass1);
		
		        entries.insertOne(user);
			} else {
				throw new Exception("Email taken");
			}
		} else {
			throw new Exception("Passwords do not match");
		}   
    }
}
