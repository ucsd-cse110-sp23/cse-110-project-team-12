/**
 * @author CSE 110 - Team 12
 */
package server;

import com.sun.net.httpserver.*; //server create()
import interfaces.ServerInterface;
import processing.Entry;
import java.net.*;
import java.util.*;
import java.util.concurrent.*; //ThreadPoolExecutor
import java.io.*;
import javax.swing.JOptionPane;

/**
 * MyServer class runs a local server to link our app with data storage capabilities. 
 * basic HTTP server
 */
public class MyServer implements ServerInterface{

	//initialize server port and host name
	private static final int SERVER_PORT = 8100;
	private static final String SERVER_HOSTNAME = "localhost";
	private static final String SERVER_ADDRESS = "127.0.0.1";
	private static final String  URL = "http://localhost:8100/";
	private static final String UTF8 = "UTF-8";

	/**
	 * Check if server available
	 * App can only run if true
	 * 
	 * @return boolean - true if server available, false otherwise
	 */
	public boolean checkServerAvailability() {
		try (Socket s = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
			return true;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error: Server Connection Unavailable");
			System.exit(1);
		}
		return false;
	}

	/**
	 * Creates and starts the local server
	 */
	public void runServer() throws IOException {
		//create a thread pool to handle requests
		ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);

		//create a server
		HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT),0);

		//create map to store data in RequestHandler
		Map<String, String> data = new HashMap<>();

		//set and start server
		server.createContext("/", new RequestHandler(data));
		server.setExecutor(threadPoolExecutor);
		server.start();

		//print success response to console
		System.out.println("Server started on port " + SERVER_PORT);
	}

	/**
	 * posts Entry to the local server
	 * 
	 * @param Entry - Entry to be posted
	 */
	public void postToServer(Entry entry){
		
		//do not post entry if null
		if (entry == null){
			return;
		}

		String title = entry.getTitle();
		String answer = entry.getResult();

		//try to connect and post Entry to server
		try {
			URL url = new URL(URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			String encodedQuestion = URLEncoder.encode(title, UTF8);
			String encodedAnswer = URLEncoder.encode(answer, UTF8);
			OutputStreamWriter out = new OutputStreamWriter(
					conn.getOutputStream()
					);
			String encodedData = encodedQuestion + "," + encodedAnswer;
			out.write(encodedData);	
			out.flush();
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * Gets Entry from server
	 * 
	 * @param String - title for Entry
	 * @return String - result for Entry
	 */
	public String getFromServer(String question){
		String response = "Getting from server...";

		//try to connect and get from server
		try {

			String encodedQuery = URLEncoder.encode(question, "UTF-8");
			URL url = new URL(URL + "?=" + encodedQuery);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			//get all lines of answer
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder answer = new StringBuilder();
			String line;

			while((line = in.readLine()) != null ) {
				answer.append(line);
				answer.append(System.lineSeparator());
			}
			in.close();
			return answer.toString(); 
		} catch (Exception ex) {
			ex.printStackTrace();
			return response;
		}
	}

	/**
	 * Deletes entry from server
	 * 
	 * @param String - entry to be deleted
	 */
	public void deleteFromServer(String query){
		//try to connect and delete from server
		try {
			String encodedQuery = URLEncoder.encode(query, "UTF-8");
			URL url = new URL(URL + "?=" + encodedQuery);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("DELETE");
			BufferedReader in = new BufferedReader(
					new InputStreamReader(conn.getInputStream())
					);
			in.readLine();
			in.close();

		} catch (Exception ex) {
			ex.printStackTrace();

		}

	}

}
