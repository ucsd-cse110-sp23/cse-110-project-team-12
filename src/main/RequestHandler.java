package main;
import com.sun.net.httpserver.*; //server create()
import java.io.*;
import java.net.*;
import java.util.*;

/*
 * HTTP Handler - how server should handle incoming requests
 */
public class RequestHandler implements HttpHandler {

	private static Map<String,String> data;
	
	public RequestHandler(Map<String, String> data) {
		//RequestHandler.data = data;
		this.data = data;
	}
	
	/* Get map of previous questions and answers
	 * 
	 * @return Map<String,String> of q/a pairs
	 */
	public static Map<String,String> getMap() {
		return data;
	}
	
	/* Called by HttpServer when HTTP request received
	 * 
	 * @param HttpExchange httpExchange
	 */
	public void handle(HttpExchange httpExchange) throws IOException {
		String response = "Request Received";
		String method = httpExchange.getRequestMethod();
		
		//handle request method - GET, POST, PUT, DELETE
		try {
			if (method.equals("GET")) {
				response = handleGet(httpExchange);
			} else if (method.equals("POST")) {
				response = handlePost(httpExchange);
			} else if (method.equals("PUT")) {
				response = handlePut(httpExchange);
			} else if (method.equals("DELETE")) {
				response = handleDelete(httpExchange);
			} else {
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
	
	/* Handles HTTP GET request received by server
	 * Queries are names of programming languages
	 * 
	 * @param HttpExchange httpExchange
	 * @return String response - release year of programming language
	 */
	private String handleGet(HttpExchange httpExchange) throws IOException {
		String response = "Invalid GET Request";
		URI uri = httpExchange.getRequestURI();
		String query = uri.getRawQuery();
		
		if (query != null) {
			//extract value of query param from query string
			String question = query.substring(query.indexOf("=") + 1);
			
			String answer = data.get(question); //retrieve data from hashmap
			
			if (answer != null) {
				response = answer;
				System.out.println("Queried for " + question + " and found " + answer);
			} else {
				//value not found in hash map
				response = "No data found for " + question;
			}
		}
		return response;
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
		
		//extract language and year vals
		String question = postData.substring(0,postData.indexOf(",")), answer = postData.substring(postData.indexOf(",")+1);
		
		//store data in hashmap
		data.put(question, answer);
		MyServer.allData.put(question, answer);
		
		String response = "Posted entry {" + question + ", " + answer + "}";
		System.out.println(response);
		scanner.close();
		
		return response;
	}
	/* Handles HTTP PUT request received by server
	 * Updates or creates data in HashMap
	 * 
	 * @param HttpExchange httpExchange
	 * @return String response - parsed updated data
	 */
	private String handlePut(HttpExchange httpExchange) throws IOException {
		//retrieve and read input stream
		InputStream inStream = httpExchange.getRequestBody();
		Scanner scanner = new Scanner (inStream);
		String postData = scanner.nextLine();
		
		//extract language and year vals
		String question = postData.substring(0,postData.indexOf(",")), answer = postData.substring(postData.indexOf(",")+1);
		
		String response = "Invalid PUT Request";
		
		if (data.containsKey(question)) {
			String prevYear = data.get(question);
			data.replace(question, answer);
			response = "Updated entry {" + question + ", " + answer + "} (previous year: " + prevYear + ")";
		} else {
			data.put(question, answer);
			response = "Added entry {" + question + ", " + answer + "}";
		}
		
		System.out.println(response);
		scanner.close();
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
				System.out.println(response);
				data.remove(question);
			} else {            //invalid query
				response = "No data found for " + question;
			}
		}
		return response;
	}
}
