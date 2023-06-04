package mock;

import java.io.File;

import interfaces.WhisperInterface;

public class WhisperMock implements WhisperInterface {
	
	private String question_text = null;
	
	public WhisperMock(File mockAudio) {
		File file = mockAudio;
		String fileName = file.getName();
		
		//US1&2: User asks/finishes asking question
		if (fileName.equals("WhenIsChristmas.wav")) {
			question_text = "When is Christmas?";
			
		//US10: user asks question with vc
		} else if (fileName.equals("QuestionWhenIsChristmas.wav")) {
			question_text = "Question when is Christmas?";
			
		//US11: user creates email with vc
		} else if (fileName.equals("CreateEmailToJillLetsGetLunch.wav")) {
			question_text = "Create email to Jill let's get lunch";
			
		//US12: user sets up email with vc
		} else if (fileName.equals("SetupEmail.wav")) {
			question_text = "Setup email";
			
		//US13: user sends email with vc
		} else if (fileName.equals("SendEmailToJillAtGmailDotCom.wav")) {
			question_text = "Setup email";
			
		//US15: user deletes prompt with vc
		} else if (fileName.equals("DeletePrompt.wav")) {
			question_text = "Delete prompt";
			
		//US16: user clears history with vc
		} else if (fileName.equals("ClearAll.wav")) {
			question_text = "Clear all";
		//empty audio 
		} else {
			question_text = "";
		}
	}

	@Override
	public String getQuestionText() {
		return question_text;
	}

}
