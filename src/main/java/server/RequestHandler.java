package server;
/*
 * RequestHandler - how server should handle incoming requests
 */

import com.sun.net.httpserver.*; //server create()

import java.io.*;
import java.net.*;
import java.util.*;
import java.net.URLEncoder;
import java.net.URLDecoder;


public class RequestHandler implements HttpHandler {

	private	 Map<String, String> data;
	private static final String UTF8 = "UTF-8";

    
    public RequestHandler(Map<String, String> data) {
        this.data = data;
    }
	
	/* Called by HttpServer when HTTP request receivedgigit	
	 * 
	 * @param HttpExchange httpExchange
	 */
	public void handle(HttpExchange httpExchange) throws IOException {
		String response = "Request Received";
		String method = httpExchange.getRequestMethod();
		
		//handle request method - GET, POST, PUT, DELETE
		try {
			if (method.equals("POST")) {
				response = handlePost(httpExchange);
			} 
			else if (method.equals("GET")) {
				response = handleGet(httpExchange);
			} 
			else if (method.equals("DELETE")) {
				response = handleDelete(httpExchange);
			} 
			else {
				throw new Exception("Not Valid Request Method");
			}
		} catch (Exception e) {
			System.out.println("An erroneous request");
			response = e.toString();
			e.printStackTrace();
		}
		
		//send back response to client
		httpExchange.sendResponseHeaders(200, response.length());
		OutputStream outStream = httpExchange.getResponseBody();
		outStream.write(response.getBytes());
		outStream.close();
	}

	/* Handles HTTP POST request received by server
	 * Gets data from server and sends response to client
	 * 
	 * @param HttpExchange httpExchange
	 * @return String response - parsed retrieved data
	 */
	private String handlePost(HttpExchange httpExchange) throws IOException {
		//retrieve and read input stream

		InputStream inStream = httpExchange.getRequestBody();
		Scanner scanner = new Scanner(inStream); 
		String postData = scanner.nextLine();
		String question = postData.substring(0,postData.indexOf(","));
		String answer = postData.substring(postData.indexOf(",") + 1);
		
		//store data in hashmap and local server
		data.put(question, answer);
		
		String response = question + "\n" + answer ;

		scanner.close();
		
		return response;
	}
	
	/* Handles HTTP GET request received by server
	 * Queries are names of programming languages
	 * 
	 * @param HttpExchange httpExchange
	 * @return String response - release year of programming language
	 */
	private String handleGet(HttpExchange httpExchange) throws IOException {
		String response = "Invalid GET Request";
		URI uri = httpExchange.getRequestURI();
		String encodedQuery = uri.getRawQuery();
		
		if (encodedQuery != null) {
			//Decode query to get the question which is the key for the map
			encodedQuery = encodedQuery.substring(encodedQuery.indexOf("=") + 1);
			String question = URLDecoder.decode(encodedQuery, UTF8);
			String encodedAnswer = data.get(encodedQuery); 
			
			String answer = URLDecoder.decode(encodedAnswer, UTF8);
	
			if (answer != null) {
				response = answer;
			} else {
				//value not found in hash map
				response = "No data found for " + question;
			}
		}
		return response;
	}
	
	
	
	/* Delete data from HashMap
	 * 
	 * @param HttpExchange httpExchange
	 * @return String response - parsed deleted data
	 */
	private String handleDelete(HttpExchange httpExchange) throws IOException {
		String response = "Invalid DELETE Request";
		URI uri = httpExchange.getRequestURI();
		String query = uri.getRawQuery();
		
		if (query != null) {
			
			//extract value of query param from query string
			String question = query.substring(query.indexOf("=")+1);
			String answer = data.get(question); //retrieve data from hashmap
			
			
			if (answer != null) { //valid query
				response = "Deleted entry {" + question + ", " + answer + "}";
				data.remove(question);
			} else {            //invalid query
				response = "No data found for " + question;
			}
		}
		return response;
	}
}