package api;
import org.bson.*;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.*;

import static com.mongodb.client.model.Filters.eq;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoDB {

	private static final String URI = "mongodb+srv://juli:Pyys5stHYEsnDtJw@cluster0.w01dcya.mongodb.net/?retryWrites=true&w=majority";
	private static final String DATABASENAME = "SayItAssistant";
	private static final String COLLECTION = "Users";

	private static MongoCollection<Document> entries;
	
	public MongoDB() {
		try( MongoClient mongoClient = MongoClients.create(URI)) {
			MongoDatabase userCluster = mongoClient.getDatabase(DATABASENAME);
			entries = userCluster.getCollection(COLLECTION);
			Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
			mongoLogger.setLevel(Level.OFF);
		}	
	}
	
	public MongoCollection<Document> getUserEntries() {
		return entries;
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

	public void createAccount(String email, String password) {	    	

        Document user = new Document("_id", new ObjectId());
        user.append("email_address", email)
            .append("password", password);

        entries.insertOne(user);
    }
}
