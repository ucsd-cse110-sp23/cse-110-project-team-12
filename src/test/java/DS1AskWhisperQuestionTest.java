import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import mock.*;

/*
 * Passing Whisper a question via audio file should generate String transcript
 */
public class DS1AskWhisperQuestionTest {
	
	String questionFile = "WhenIsChristmas.wav";
	String emptyQuestionFile = "emptyQuestion.wav";
	
	/*
	 * Scenario1: Ask when is Christmas
	 */
	@Test
	void testValidQuestion() {
		File questionFile = new File(this.questionFile);
		
		WhisperMock whisperMock = new WhisperMock(questionFile);
		assertEquals(whisperMock.getQuestionText(), "When is Christmas?");
	}
	
	/*
	 * Scenario2: "Ask" empty question ie parse empty audio
	 */
	@Test
	void testEmptyQuestion() {
		File emptyFile = new File(this.emptyQuestionFile);
		
		WhisperMock whisperMock = new WhisperMock(emptyFile);
		assertEquals(whisperMock.getQuestionText(), "");
	}
	
	
	 
}
