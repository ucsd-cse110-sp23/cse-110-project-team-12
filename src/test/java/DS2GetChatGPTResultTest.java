import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import mock.*;

/*
 * Passing ChatGPT question String should generate answer String
 */
public class DS2GetChatGPTResultTest {
	String question = "When is Christmas?";
	String answer = "Christmas is on Dec 25th.";
	
	/*
	 * Scenario 1: ask ChatGPT a question
	 */
	@Test
	void testgetChatGPTResult() {
		ChatGPTMock chatGPTMock = new ChatGPTMock(question);
		
		assertEquals(chatGPTMock.getQuestion(), question);
		assertEquals(chatGPTMock.getAnswer(), answer);
	}

}
