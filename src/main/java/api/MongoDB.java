package api;
import org.bson.*;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;

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
	private final String EMAILINFO_CATEGORY = "saved email info";

	private final String FIRSTNAME_CATEGORY = "first name";
	private final String LASTNAME_CATEGORY = "last name";
	private final String DISPLAY_CATEGORY = "display name";
	private final String SMTP_HOST = "smpt host";
	private final String TLS_PORT = "tls port";
	private final String EMAIL_PASSWORD = "email password";


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
			Document documentEntry = new Document(COMMAND_CATEGORY, entry.getCommand());
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

	//Called when SetupEmail button is clicked and inputs are valid (not null)
	public void setupEmail(ArrayList<String> inputs) {
		System.out.println("reached");
		Bson filter = eq(EMAIL_CATEGORY, this.currentLoggedInUser);
		MongoCollection<Document> allUsers = getAllDocuments();
		Document documentEmailInfo = new Document(FIRSTNAME_CATEGORY, inputs.get(0));
		documentEmailInfo.append(LASTNAME_CATEGORY, inputs.get(1));
		documentEmailInfo.append(DISPLAY_CATEGORY, inputs.get(2));
		documentEmailInfo.append(EMAIL_CATEGORY, inputs.get(3));
		documentEmailInfo.append(SMTP_HOST, inputs.get(4));
		documentEmailInfo.append(TLS_PORT, inputs.get(5));
		documentEmailInfo.append(EMAIL_PASSWORD, inputs.get(6));
		UpdateOptions options = new UpdateOptions().upsert(true);
		Bson updateOperation = set(EMAILINFO_CATEGORY, documentEmailInfo);
		allUsers.updateOne(filter, updateOperation, options);
		
	}
	
	//used by TLSEmail
	private Document getEmailFields() {
		MongoCollection<Document> allUsers = getAllDocuments();
		Bson filter = eq(EMAIL_CATEGORY,currentLoggedInUser);
		//Gets the data saved to that email
		Document LoggedInUserData = allUsers.find(filter).first();
		Document emailFields;
		if ((emailFields = (Document) LoggedInUserData.get(EMAILINFO_CATEGORY)) != null) {
			return emailFields;
		} else {
			return null;
		}
	}
	
	public String getFirstName() {
		Document emailFields;
		if ((emailFields = getEmailFields()) != null) {
			return (String) emailFields.get(FIRSTNAME_CATEGORY);
		} else {
			return null;
		}
	}
	
	public String getLastName() {
		Document emailFields = getEmailFields();
		return (String) emailFields.get(LASTNAME_CATEGORY);
	}
	
	public String getDisplayNameEmail() {
		Document emailFields;
		if ((emailFields = getEmailFields()) != null) {
			return (String) emailFields.get(DISPLAY_CATEGORY);
		} else {
			return null;
		}
	}
	
	public String getFromEmail() {
		Document emailFields = getEmailFields();
		return (String) emailFields.get(EMAIL_CATEGORY);
	}
	
	public String getSMTPHost() {
		Document emailFields = getEmailFields();
		return (String) emailFields.get(SMTP_HOST);
	}
	
	public String getTLSPort() {
		Document emailFields = getEmailFields();
		return (String) emailFields.get(TLS_PORT);
	}
	
	public String getPassword() {
		Document emailFields = getEmailFields();
		return (String) emailFields.get(EMAIL_PASSWORD);
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
	}

	@Override
	public void notifyObservers() {
		this.presenter.onStart();
	}

	

}
