import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import mock.MockServer;

public class DS6ClearHistoryTest {
	String question = "When is Christmas?";
	String answer = "Christmas is on Dec 25th.";
	
	String question2 = "When is Halloween?";
	String answer2 = "Halloween is on Oct 31st.";
	
	/*
	 * Scenario 1: clear history from server
	 */
	@Test
	void testDeleteEntryFromServer() {
		MockServer server = new MockServer();
		server.handlePost(question, answer);
		server.handlePost(question2, answer2);
		assertEquals(server.size(), 2);
		
		server.handleClearAll();
		assertEquals(server.size(), 0);
	}
}
