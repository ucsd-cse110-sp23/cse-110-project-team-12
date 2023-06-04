package mock;

import interfaces.ChatGPTInterface;

public class ChatGPTMock implements ChatGPTInterface {
	private String question = null;
	private String answer = null;
	
	public ChatGPTMock(String question) {
		this.question = question;
		
		//US1&2, 10: ask question
		if (question.equals("When is Christmas?")) {
			this.answer = "Christmas is on Dec 25th.";
			
		//US11: create email
		} else {
			this.answer = "Dear Jill,\n let's get lunch. \n Best regards, \n Bob";
		}
	}

	@Override
	public String getQuestion() {
		return question;
	}

	@Override
	public String getAnswer() {
		return answer;
	}

}
