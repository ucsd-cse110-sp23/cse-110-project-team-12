package processing;

public abstract class Entry {
	private String command;
	private String prompt;
	private String result;
	
	Entry(String command, String prompt, String result) {
		this.command = command;
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
		return command + ": " + prompt;
	}
	
}
