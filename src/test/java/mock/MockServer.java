package mock;

import java.util.HashMap;

public class MockServer {
	HashMap<String,String> server;
	
	/*
	 * Entries stored as <question,answer>
	 */
	public MockServer() {
		server = new HashMap<>();
	}
	
	public int size() {
		return server.size();
	}
	
	public boolean exists(String question) {
		if (server.containsKey(question)) {
			return true;
		} else {
			return false;
		}
	}
	
	public String handleGet(String question) {
		return server.get(question);
	}
	
	public void handlePost(String question, String answer) {
		server.put(question, answer);
	}
	
	public void handleDelete(String question) {
		server.remove(question);
	}
	
	public void handleClearAll() {
		server.clear();
	}
}
