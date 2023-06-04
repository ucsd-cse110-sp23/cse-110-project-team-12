import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import mock.MockServer;

public class DS5DeletePromptTest {
	
	String question = "When is Christmas?";
	String answer = "Christmas is on Dec 25th.";
	
	/*
	 * Scenario 1: delete prompt from server
	 */
	@Test
	void testDeleteEntryFromServer() {
		MockServer server = new MockServer();
		server.handlePost(question, answer);
		assertEquals(server.size(), 1);
		assertTrue(server.exists(question));
		
		server.handleDelete(question);
		assertEquals(server.size(), 0);
		assertFalse(server.exists(question));
	}

}
