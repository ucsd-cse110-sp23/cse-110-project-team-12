import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import mock.*;

public class DS4GetEntryFromServerTest {
	
	String question = "When is Christmas?";
	String answer = "Christmas is on Dec 25th.";
	
	/*
	 * Scenario 1: get entry from server
	 */
	@Test
	void testGetEntryFromServer() {
		MockServer server = new MockServer();
		server.handlePost(question, answer);
		
		assertEquals(server.handleGet(question), answer);
	}

}
