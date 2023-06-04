import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import mock.*;

/*
 * New questions and their results should be added to server
 */
public class DS3AddEntryToServerTest {
	
	String question = "When is Christmas?";
	String answer = "Christmas is on Dec 25th.";
	
	/*
	 * Scenario 1: add question and result entry to server
	 */
	@Test
	void testAddEntryToServer() {
		MockServer server = new MockServer();
		assertEquals(server.size(), 0); 
		
		server.handlePost(question, answer);
		assertEquals(server.size(), 1); 
		assertTrue(server.exists(question));
	}

}
