/*
 * Entry obj with command, prompt, and result
 * 
 * Supports adding to QuestionPanel, PromptHistory, and server
 */
public abstract class Entry {
	protected String command;
	protected String prompt;
	protected String result;
	protected String user;
	
	Entry(String user, String prompt, String result) {
		this.user = user;
		this.prompt = prompt;
		this.result = result;
	}
	
	public String getCommand() {
		return command;
	}
	
	public String getPrompt() {
		return prompt;
	}
	
	public String getResult() {
		return result;
	}
	
	public String getTitle() {
		return command + ":" + prompt;
	}
	
	public void updateQuestionPanel() {
		QuestionPanel.setQuestion(getTitle());
		QuestionPanel.setAnswer(result);
	}
	
	public void updatePromptHistory() {
		PromptHistory.addPH(getTitle());
	}
	
	public void addToServer() {
		ServerCalls.postToServer(getTitle(), result);
	}
	
	/*
	public void addToDatabase() {
		DataBaseCalls.addData(user,command,prompt,result);
	}*/
	
	
}
