/**
 * @author CSE 110 - Team 12
 */
package processing;

/**
 * Entry class for handling user entries
 *
 */
public class Entry {
	
	//Entry fields
	private String command;
	private String prompt;
	private String result;
	
	/**
	 * Constructor for Entry
	 * 
	 * @param command
	 * @param prompt
	 * @param result
	 */
	public Entry(String command, String prompt, String result) {
		this.command = command;
		this.prompt = prompt;
		this.result = result;
	}
	
	/**
	 * Getter for Entry's command
	 * 
	 * @return String command
	 */
	public String getCommand() {
		return command;
	}
	
	/**
	 * Getter for Entry's prompt
	 * 
	 * @return String prompt
	 */
	public String getPrompt() {
		return prompt;
	}
	
	/**
	 * Getter for Entry's result
	 * 
	 * @return String result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * Getter for Entry's title
	 * 
	 * @return String title
	 */
	public String getTitle() {
		return command + ": " + prompt;
	}
	
}
