/**
 * @author CSE 110 - Team 12
 */

package api;

import org.bson.*;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;

import interfaces.ButtonSubject;
import interfaces.MongoInterface;
import mediators.QPHPHButtonPanelPresenter;
import processing.Entry;

import java.util.ArrayList;

/**
 * Class MongoDB handles all communication with MongoDB server 
 * Database name: "SayItAssistant" 
 * Collection: "Users"
 */
public class MongoDB implements MongoInterface, ButtonSubject {

	// mediator where MongoDB methods will be called
	QPHPHButtonPanelPresenter presenter;

	// configure MongoDB
	private final String URI = "mongodb+srv://juli:Pyys5stHYEsnDtJw@cluster0.w01dcya.mongodb.net/?retryWrites=true&w=majority";
	private final String DATABASENAME = "SayItAssistant";
	private final String COLLECTION = "Users";

	// user fields
	private final String PASSWORD_CATEGORY = "password";
	private final String HISTORY_CATEGORY = "entries";
	private final String EMAILINFO_CATEGORY = "saved email info";

	// email fields
	private final String EMAIL_CATEGORY = "email_address";
	private final String FIRSTNAME_CATEGORY = "first name";
	private final String LASTNAME_CATEGORY = "last name";
	private final String DISPLAY_CATEGORY = "display name";
	private final String SMTP_HOST = "smtp host";
	private final String TLS_PORT = "tls port";
	private final String EMAIL_PASSWORD = "email password";

	// Entry fields
	private final String COMMAND_CATEGORY = "type";
	private final String PROMPT_CATEGORY = "prompt";
	private final String RESULT_CATEGORY = "result";

	private String currentLoggedInUser = null;

	/**
	 * Checks if email taken by a user in MongoDB
	 * 
	 * @param String - email to check
	 * @return boolean - true if email taken, false if available
	 */
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

	/**
	 * Creates new user account in MongoDB Sets current user's email as logged in user
	 * 
	 * @param String - user's email
	 * @param String - user's password
	 * @param String - password validation
	 */
	public void createAccount(String email, String pass1, String pass2) {

		MongoCollection<Document> entries = getAllDocuments();
		Document userDocument = new Document("_id", new ObjectId());
		userDocument.append(EMAIL_CATEGORY, email).append(PASSWORD_CATEGORY, pass1)
				.append(HISTORY_CATEGORY,
				new ArrayList<Document>());
		// insert user's email and password into database
		entries.insertOne(userDocument);
		currentLoggedInUser = email;
	}

	/**
	 * Checks if user with email and password exists in MongoDB Sets current user's
	 * email as logged in user
	 * 
	 * @param String - user's email
	 * @param String - user's password
	 * @return boolean - true if login successful, false otherwise
	 */
	public boolean checkValidLogin(String email, String pass1) {

		Bson filter1 = eq(EMAIL_CATEGORY, email);
		Bson filter2 = Filters.and(eq(EMAIL_CATEGORY, email), eq(PASSWORD_CATEGORY, pass1));

		MongoCollection<Document> entries = getAllDocuments();

		// check if email exists
		if (entries.find(filter1).first() != null) {

			// check that password is correct for email
			if (entries.find(filter2).first() != null) {

				// account exists so user logs in
				this.currentLoggedInUser = email;
				notifyObservers();
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds new user entries to MongoDB
	 * 
	 * @param ArrayList<Entry> - user's
	 */
	@Override
	public void updateSavedPromptHistory(ArrayList<Entry> PromptHistory) {
		Bson filter = eq(EMAIL_CATEGORY, this.currentLoggedInUser);
		MongoCollection<Document> allUsers = getAllDocuments();

		ArrayList<Document> documentPromptHistory = new ArrayList<Document>();
		for (Entry entry : PromptHistory) {
			Document documentEntry = new Document(COMMAND_CATEGORY, entry.getCommand());
			documentEntry.append(PROMPT_CATEGORY, entry.getPrompt());
			documentEntry.append(RESULT_CATEGORY, entry.getResult());
			documentPromptHistory.add(documentEntry);
		}
		Bson updateOperation = set(HISTORY_CATEGORY, documentPromptHistory);
		allUsers.updateOne(filter, updateOperation);

	}

	/**
	 * Gets user's prompt history as from MongoDB
	 * 
	 * @return ArrayList<Entry> - user's prompt history
	 */
	@Override
	public ArrayList<Entry> retrieveSavedPromptHistory() {
		ArrayList<Entry> promptHistory = new ArrayList<Entry>();
		MongoCollection<Document> allUsers = getAllDocuments();
		Bson filter = eq(EMAIL_CATEGORY, currentLoggedInUser);
		// Gets the data saved to that email
		Document LoggedInUserData = allUsers.find(filter).first();
		// Gets the prompt history saved in that data
		@SuppressWarnings("unchecked")
		ArrayList<Document> savedPromptHistory = 
				(ArrayList<Document>) LoggedInUserData.get(HISTORY_CATEGORY);
		for (Document documentEntry : savedPromptHistory) {
			String command = documentEntry.getString(COMMAND_CATEGORY);
			String prompt = documentEntry.getString(PROMPT_CATEGORY);
			String result = documentEntry.getString(RESULT_CATEGORY);
			Entry entry = new Entry(command, prompt, result);
			promptHistory.add(entry);
		}
		return promptHistory;
	}

	/**
	 * Saves the user's email configuration settings
	 * Called when SetupEmail button is clicked and inputs are valid (not null)
	 * 
	 * @param ArrayList<String> - user's input for email configuration settings
	 */
	public void setupEmail(ArrayList<String> inputs) {
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

	/**
	 * Get user's first name from MongoDB
	 * Mediator uses this method to check if email settings have been set up
	 * 
	 * @return String - user's first name or null if no first name saved
	 */
	public String getFirstName() {
		Document emailFields;
		if ((emailFields = getEmailFields()) != null) {
			return (String) emailFields.get(FIRSTNAME_CATEGORY);
		} else {
			return null;
		}
	}

	/**
	 * Get user's last name from MongoDB
	 * 
	 * @return String - user's last name
	 */
	public String getLastName() {
		Document emailFields = getEmailFields();
		return (String) emailFields.get(LASTNAME_CATEGORY);
	}

	/**
	 * Get user's display name from MongoDB
	 * Email creation calls this method to end email with display name
	 * 
	 * @return String - user's display name
	 */
	public String getDisplayNameEmail() {
		Document emailFields;
		if ((emailFields = getEmailFields()) != null) {
			return (String) emailFields.get(DISPLAY_CATEGORY);
		} else {
			return null;
		}
	}

	/**
	 * Get user's email from MongoDB
	 * 
	 * @return String - user's email
	 */
	public String getFromEmail() {
		Document emailFields = getEmailFields();
		return (String) emailFields.get(EMAIL_CATEGORY);
	}

	/**
	 * Get user's SMTP host from MongoDB
	 * 
	 * @return String - user's SMTP host
	 */
	public String getSMTPHost() {
		Document emailFields = getEmailFields();
		return (String) emailFields.get(SMTP_HOST);
	}

	/**
	 * Get user's TLS port from MongoDB
	 * 
	 * @return String - user's TLS port
	 */
	public String getTLSPort() {
		Document emailFields = getEmailFields();
		return (String) emailFields.get(TLS_PORT);
	}

	/**
	 * Get user's password from MongoDB
	 * 
	 * @return String - user's password
	 */
	public String getPassword() {
		Document emailFields = getEmailFields();
		return (String) emailFields.get(EMAIL_PASSWORD);
	}

	//////////////////////////////////// HELPER METHODS////////////////////////////////////

	/**
	 * Gets all users from MongoDB
	 * 
	 * @return MongoCollection<Document> - all users stored
	 */
	private MongoCollection<Document> getAllDocuments() {
		MongoClient mongoClient = MongoClients.create(URI);
		MongoDatabase sayIt = mongoClient.getDatabase(DATABASENAME);
		MongoCollection<Document> users = sayIt.getCollection(COLLECTION);
		return users;
	}
	
	/**
	 * Gets all of user's email configuration settings
	 * 
	 * @return Document - user's email configuration settings
	 */
	private Document getEmailFields() {
		MongoCollection<Document> allUsers = getAllDocuments();
		Bson filter = eq(EMAIL_CATEGORY, currentLoggedInUser);
		// Gets the data saved to that email
		Document LoggedInUserData = allUsers.find(filter).first();
		Document emailFields;
		if ((emailFields = (Document) LoggedInUserData.get(EMAILINFO_CATEGORY)) != null) {
			return emailFields;
		} else {
			return null;
		}
	}

	////////////////////////////// MEDIATOR SUBJECT METHODS////////////////////////////////

	/**
	 * Sets QPPH mediator as observer
	 */
	@Override
	public void registerObserver(QPHPHButtonPanelPresenter presenter) {
		this.presenter = presenter;
	}

	/**
	 * loads in entries to PH on start
	 */
	@Override
	public void notifyObservers() {
		this.presenter.onStart();
	}

}
