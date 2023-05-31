import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateAccount {
	private static String email; 
	private static String password; 
	
	public CreateAccount(String emailField, String passwordField, String collection) {
		email = emailField;
		password = passwordField;
		
		String databaseName = "SayItAssistant";
	    	
        String uri = "mongodb+srv://juli:Pyys5stHYEsnDtJw@cluster0.w01dcya.mongodb.net/?retryWrites=true&w=majority";
        
        //configure
        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
 	    mongoLogger.setLevel(Level.OFF);
        
        try (MongoClient mongoClient = MongoClients.create(uri)) {

            MongoDatabase userCluster = mongoClient.getDatabase(databaseName);
            MongoCollection<Document> entries = userCluster.getCollection(collection);

            Document user = new Document("_id", new ObjectId());
            user.append("email_address", email)
                .append("password", password);

            entries.insertOne(user);
        }
    }
}
