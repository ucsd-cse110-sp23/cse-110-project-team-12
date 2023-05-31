package api;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.*;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.*;
import static com.mongodb.client.model.Filters.eq;

public class MongoDB {
	static boolean emailExists = false;

	public static boolean checkEmail(String email, String collectionName) {
	
		String databaseName = "SayItAssistant";
		//String collectionName = "Users";
	    	
	    String uri = "mongodb+srv://juli:Pyys5stHYEsnDtJw@cluster0.w01dcya.mongodb.net/?retryWrites=true&w=majority";
	    
	    //configure
	    Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
		mongoLogger.setLevel(Level.OFF);
	    
	    try (MongoClient mongoClient = MongoClients.create(uri)) {
	
	        MongoDatabase userCluster = mongoClient.getDatabase(databaseName);
	        MongoCollection<Document> entries = userCluster.getCollection(collectionName);
	        
	        Bson filter = eq("email_address", email);
	        
	        if (entries.find(filter).first() != null) {
	        	emailExists = true;
	        }
	    }
		return emailExists;
	}

	public static void createAccount(String emailField, String passwordField, String collection) {
	
		String databaseName = "SayItAssistant";
	    	
        String uri = "mongodb+srv://juli:Pyys5stHYEsnDtJw@cluster0.w01dcya.mongodb.net/?retryWrites=true&w=majority";
        
        //configure
        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
 	    mongoLogger.setLevel(Level.OFF);
        
        try (MongoClient mongoClient = MongoClients.create(uri)) {

            MongoDatabase userCluster = mongoClient.getDatabase(databaseName);
            MongoCollection<Document> entries = userCluster.getCollection(collection);

            Document user = new Document("_id", new ObjectId());
            user.append("email_address", emailField)
                .append("password", passwordField);

            entries.insertOne(user);
        }
    }
}
