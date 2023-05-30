
public class ChatGPTMock extends ChatGPT {
	static int n = 0; //counter

	public ChatGPTMock(String question, String answer) {
		super(question, answer + n);
		n++;
	}
	
}
