import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

/*
 * Takes audio from recorder and does appropriate action using command
 */
public class AudioToResult {
	String emailUsername = "default";
	int n = 0;
	
	public AudioToResult(String databaseUser, File question_audio) throws IOException {

		/*
		
		
		WhisperMock voiceToText = null;
		ChatGPT textToResult = null;
		
		/*
		 * test Whisper
		 */
		//voiceToText = new WhisperMock("Question", "When is Christmas?");
		//voiceToText = new WhisperMock("Create email", "To Henry, let's meet. Tomorrow.");
		
		/*
		 * actual Whisper
		 
        
        try {                                                                              //Whisper parses audio into command + prompt
            voiceToText = new Whisper(question_audio);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        /*
         * 3 scenarios
         * "Question", "Create email", "Send email" create entries
         * "Clear all", "Setup email", "Delete prompt" just need to be detected first
         
        
        if (voiceToText.getCommand() != null) {                                            //if valid command detected from Whisper
        	
        	
        	if (voiceToText.getCommand().equalsIgnoreCase("Question")) {                   //handle Question command
        		/*
        		 * test ask question to chatGPT
        		 
        		textToResult = new ChatGPTMock(voiceToText.getPrompt(), "Christmas is on Dec 25th.");
        		
        		/*
        		 * real ask question to chatGPT
        		 
        		/*
        		try {
    	            textToResult = new ChatGPT(Whisper.getPrompt());
    	        } catch (IOException e) {
    	            e.printStackTrace();
    	        } catch (InterruptedException e) {
    	            e.printStackTrace();
    	        }

        		
        		/*
        		 * create Entry - updates QuestionPanel, PromptHistory, and adds to server
        		 
        		new QuestionEntry(databaseUser, voiceToText.getPrompt(), textToResult.getResult());
        		
        	} else if (voiceToText.getCommand().equalsIgnoreCase("Create email")) {        //handle Create Email command
        		/*
        		 * test create email with chatGPT
        		 
        		textToResult = new ChatGPTMock(voiceToText.getPrompt(), "Dear Henry," + "\n" + "Let's meet. Tomorrow." + "\n" + "Best regards," + "\n" + "Juli");
        		
        		/*
        		 * real create email with chatGPT
        		 */
        		/*
        		try {
    	            textToResult = new ChatGPT("Create an email draft that ends in Best regards, " + emailUsername + "and the email's body is" + voiceToText.getPrompt());
    	        } catch (IOException e) {
    	            e.printStackTrace();
    	        } catch (InterruptedException e) {
    	            e.printStackTrace();
    	        }*/
        		
        		/*
        		 * create Entry - updates QuestionPanel, PromptHistory, and adds to server
        		 
        		new EmailEntry(databaseUser, voiceToText.getPrompt(), textToResult.getResult());
        		
        		
        	} else if (voiceToText.getCommand().equalsIgnoreCase("Send email")) {          //handle Send Email command
        		System.out.println("send email");
        		
        		
        	} else if (voiceToText.getCommand().equalsIgnoreCase("Clear all")) {           //handle Clear All command
        		System.out.println("clear all");
        		
        	} else if (voiceToText.getCommand().equalsIgnoreCase("Setup email")) {         //handle Setup Email command
        		System.out.println("setup email");
        		
        		
        	} else if (voiceToText.getCommand().equalsIgnoreCase("Delete prompt")) {       //handle Delete Prompt command
        		System.out.println("delete prompt");
        	}
	        
        } else {                                                                           //no valid command detected from Whisper
        	 JOptionPane.showMessageDialog(null, "Invalid input: " + Whisper.getCommandPrompt());
        }*/
	}
	

}
