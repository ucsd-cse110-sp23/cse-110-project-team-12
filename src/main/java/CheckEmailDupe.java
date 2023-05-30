import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;

import static com.mongodb.client.model.Filters.*;

import static com.mongodb.client.model.Updates.*;

import com.mongodb.Block;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class CheckEmailDupe {
	boolean emailExists = false;
	int count;

	public CheckEmailDupe(String email) {
	
		String databaseName = "SayItAssistant";
		String collectionName = "Users";
	    	
	    String uri = "mongodb+srv://juli:Pyys5stHYEsnDtJw@cluster0.w01dcya.mongodb.net/?retryWrites=true&w=majority";
	    
	    //configure
	    Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
		mongoLogger.setLevel(Level.OFF);
	    
	    try (MongoClient mongoClient = MongoClients.create(uri)) {
	
	        MongoDatabase userCluster = mongoClient.getDatabase(databaseName);
	        MongoCollection<Document> entries = userCluster.getCollection(collectionName);
	        
	        Bson filter = eq("email_address", email);
	        count = 0;
	        entries.find(filter).forEach((Block<? super Document>) (doc -> count++));
	        
	        if (count > 0) {
	        	emailExists = true;
	        }
	    }
	}
}
