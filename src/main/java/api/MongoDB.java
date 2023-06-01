package api;

//import java.util.logging.Level;
//import java.util.logging.Logger;

import org.bson.*;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.*;

//import interfaces.MongoInterface;

import static com.mongodb.client.model.Filters.eq;

//MongoDB is a SUT (system under test)
//MongoInterface is a Dependency (as MongoDB is dependent on it)
public class MongoDB {
	/*
	public MongoInterface interFace;
	
	public MongoDB(MongoInterface interFace) {
		this.interFace = interFace;
	}*/
	private static final String URI = "mongodb+srv://juli:Pyys5stHYEsnDtJw@cluster0.w01dcya.mongodb.net/?retryWrites=true&w=majority";
	private static final String DATABASENAME = "SayItAssistant";
	
	private String email, collectionName, password;
	private static final MongoClient mongoClient = MongoClients.create(URI);
	
	public MongoDB(String email, String password, String collectionName) {
		this.email = email;
		this.collectionName = collectionName;
		this.password = password;
	}
	
	public MongoClient getMongoClient() {
		return mongoClient;
	}
	
	public boolean checkEmail() {
		//String collectionName = "Users";
	    	    
	    //configure
	    //Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
		//mongoLogger.setLevel(Level.OFF);
		boolean emailExists = false;
	    try {
	
	        MongoDatabase userCluster = mongoClient.getDatabase(DATABASENAME);
	        MongoCollection<Document> entries = userCluster.getCollection(collectionName);
	        
	        Bson filter = eq("email_address", email);
	        
	        if (entries.find(filter).first() != null) {
	        	emailExists = true;
	        }
	    } catch (Exception e) {
	    	//
	    }
		return emailExists;
	}

	public void createAccount() {	    	
        
        //configure
        //Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
 	    //mongoLogger.setLevel(Level.OFF);
        
        try {

            MongoDatabase userCluster = mongoClient.getDatabase(DATABASENAME);
            MongoCollection<Document> entries = userCluster.getCollection(collectionName);

            Document user = new Document("_id", new ObjectId());
            user.append("email_address", email)
                .append("password", password);

            entries.insertOne(user);
        } catch (Exception e){
        	//
        }
    }
}
