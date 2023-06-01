package api;

//import java.util.logging.Level;
//import java.util.logging.Logger;

import org.bson.*;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.*;

//import interfaces.MongoInterface;

import static com.mongodb.client.model.Filters.eq;

import java.util.logging.Level;
import java.util.logging.Logger;

//MongoDB is a SUT (system under test)
//MongoInterface is a Dependency (as MongoDB is dependent on it)
public class MongoDB {
	//configure logs
    private static final Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );

	private static final String URI = "mongodb+srv://juli:Pyys5stHYEsnDtJw@cluster0.w01dcya.mongodb.net/?retryWrites=true&w=majority";
	private static final String DATABASENAME = "SayItAssistant";
	private static final String COLLECTION = "Users";
	
	private static final MongoClient mongoClient = MongoClients.create(URI);
	private static final MongoDatabase userCluster = mongoClient.getDatabase(DATABASENAME);
	private static final MongoCollection<Document> entries = userCluster.getCollection(COLLECTION);
	
	public MongoDB() {
		//configure logs
		mongoLogger.setLevel(Level.OFF);
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

	public void createAccount(String email, String password) {	    	

        Document user = new Document("_id", new ObjectId());
        user.append("email_address", email)
            .append("password", password);

        entries.insertOne(user);
    }
}
