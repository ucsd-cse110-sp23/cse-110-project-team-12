package server;
/*
 * MyServer class runs a local server to link our app with data storage capabilities. 
 */

// import com.mongodb.internal.connection.Server;
import com.sun.net.httpserver.*; //server create()

import interfaces.ServerInterface;
import processing.Entry;

import java.net.*;
import java.util.*;
import java.util.concurrent.*; //ThreadPoolExecutor
import java.io.*;

import javax.swing.JOptionPane;

/*
 * basic HTTP server
 */
public class MyServer implements ServerInterface{
	
	//initialize server port and hostname
	private static final int SERVER_PORT = 8100;
	private static final String SERVER_HOSTNAME = "localhost";
	private static final String SERVER_ADDRESS = "127.0.0.1";
	private static final String  URL = "http://localhost:8100/";
	private static final String UTF8 = "UTF-8";

	public  boolean checkServerAvailability() {
		try (Socket s = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
			return true;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error: Server Connection Unavailable");
			System.exit(1);
		}
		return false;
	}
	
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
		
		System.out.println("Server started on port " + SERVER_PORT);
	}

	public void postToServer(Entry entry){
		if (entry == null){
		  return;
		}
  
		String title = entry.getTitle();
		String answer = entry.getResult();
	
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
			System.out.println(encodedData);
			out.write(encodedData);	
			out.flush();
			out.close();
			BufferedReader in = new BufferedReader(
              new InputStreamReader(conn.getInputStream())
            );
            String response = in.readLine();
            in.close();
		  } catch (Exception ex) {
			ex.printStackTrace();
		  }
	  
		}
  
		public String getFromServer(String question){
			String response = "Getting from server...";
		  
			try {
				
			String encodedQuery = URLEncoder.encode(question, "UTF-8");
			URL url = new URL(URL + "?=" + encodedQuery);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			response = in.readLine();
			in.close();
			return response; 
			} catch (Exception ex) {
			  ex.printStackTrace();
			  return response;
		  }
		}

		public void deleteFromServer(String query){
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
