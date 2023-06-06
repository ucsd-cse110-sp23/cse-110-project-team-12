package api;
import org.bson.*;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;

import interfaces.ButtonSubject;
import interfaces.MongoInterface;
import mediators.QPHPHButtonPanelPresenter;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;

import java.util.ArrayList;
import processing.Entry;

public class MongoDB implements MongoInterface, ButtonSubject{

	QPHPHButtonPanelPresenter presenter;
	private final String URI = "mongodb+srv://juli:Pyys5stHYEsnDtJw@cluster0.w01dcya.mongodb.net/?retryWrites=true&w=majority";
	private final String DATABASENAME = "SayItAssistant";
	private final String COLLECTION = "Users";

	private final String EMAIL_CATEGORY = "email_address";
	private final String PASSWORD_CATEGORY = "password";
	private final String HISTORY_CATEGORY = "entries";

	private final String COMMAND_CATEGORY = "type";
	private final String PROMPT_CATEGORY = "prompt";
	private final String RESULT_CATEGORY = "result";

	private String currentLoggedInUser = null;
	
	public boolean checkEmailExists(String email) {
	    	 
        Bson filter = eq(EMAIL_CATEGORY, email);
        try (MongoClient mongoClient = MongoClients.create(URI)) {
			MongoDatabase userCluster = mongoClient.getDatabase(DATABASENAME);
            MongoCollection<Document> entries = userCluster.getCollection(COLLECTION);
	        if (entries.find(filter).first() != null) {
				currentLoggedInUser = email;
	        	return true;
	        }
        }

		return false;
	}

	public void createAccount(String email, String pass1, String pass2) {
		
            MongoCollection<Document> entries = getAllDocuments();
			Document userDocument = new Document("_id", new ObjectId());
			userDocument.append(EMAIL_CATEGORY, email)
				.append(PASSWORD_CATEGORY, pass1)
				.append(HISTORY_CATEGORY, new ArrayList<Document>());
			//insert user's email and password into database
			entries.insertOne(userDocument);
			currentLoggedInUser = email;
			
    }
	
	public boolean checkValidLogin(String email, String pass1) {
		
		Bson filter1 = eq(EMAIL_CATEGORY, email);
		Bson filter2 = Filters.and(eq(EMAIL_CATEGORY, email), eq(PASSWORD_CATEGORY, pass1));
	
		MongoCollection<Document> entries = getAllDocuments();
            
		//check if email exists
		if (entries.find(filter1).first() != null) {
			//check that password is correct for email
			if (entries.find(filter2).first() != null) {
				//account exists so user logs in
				this.currentLoggedInUser = email;
				notifyObservers();
				return true;
			} 
			return false;
		}	
		else {
				return false;
		}
	}

	@Override
	public void updateSavedPromptHistory(ArrayList<Entry> PromptHistory) {
		Bson filter = eq(EMAIL_CATEGORY, this.currentLoggedInUser);
		MongoCollection<Document> allUsers = getAllDocuments();

		ArrayList<Document> documentPromptHistory = new ArrayList<Document>();
		for (Entry entry: PromptHistory){
			Document documentEntry = new Document("entries", null);
			documentEntry.append(COMMAND_CATEGORY, entry.getCommand());
			documentEntry.append(PROMPT_CATEGORY, entry.getPrompt());
			documentEntry.append(RESULT_CATEGORY, entry.getResult());
			documentPromptHistory.add(documentEntry);
		}
		Bson updateOperation = set(HISTORY_CATEGORY, documentPromptHistory);
		//UpdateResult updateresult = 
		allUsers.updateOne(filter, updateOperation);
		
	}

	@Override
	public ArrayList<Entry> retrieveSavedPromptHistory() {
		ArrayList<Entry> promptHistory = new ArrayList<Entry>();
		MongoCollection<Document> allUsers = getAllDocuments();
		System.out.println(currentLoggedInUser);
		Bson filter = eq(EMAIL_CATEGORY,currentLoggedInUser);
		//Gets the data saved to that email
		Document LoggedInUserData = allUsers.find(filter).first();
		//Gets the prompt history saved in that data TODO NEED TO CHECK FOR NULL INSTANCES
		ArrayList<Document> savedPromptHistory = (ArrayList<Document>)LoggedInUserData.get(HISTORY_CATEGORY);
		for (Document documentEntry : savedPromptHistory) {
			String command = documentEntry.getString(COMMAND_CATEGORY);
			String prompt = documentEntry.getString(PROMPT_CATEGORY);
			String result =  documentEntry.getString(RESULT_CATEGORY);
			Entry entry = new Entry(command, prompt, result);
			promptHistory.add(entry);
		}
		return promptHistory;
	}

	

	////////////////////////////////////HELPER METHODS////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//get from Sayit user database,
	private MongoCollection<Document> getAllDocuments(){
		MongoClient mongoClient = MongoClients.create(URI);			
		MongoDatabase sayIt = mongoClient.getDatabase(DATABASENAME);
		MongoCollection<Document> users = sayIt.getCollection(COLLECTION);
		return users;
	}
	


////////////////////////////////////UNUSED METHODS////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * param - email
	 * returns email's password
	 */
	public String checkPass(String email) {
		
		Bson filter = eq(EMAIL_CATEGORY, email);

		MongoCollection<Document> entries = getAllDocuments();
		Document foundDoc = entries.find(filter).first();
		return (String) (foundDoc.get(PASSWORD_CATEGORY));
	
	
	}

	////////////////////////////////MEDIATOR SUBJECT METHODS/////////////////////////////////////////

	@Override
	public void registerObserver(QPHPHButtonPanelPresenter presenter) {
		this.presenter = presenter;
		System.out.println("Reached");
	}

	@Override
	public void notifyObservers() {
		this.presenter.onStart();
	}

	

}
