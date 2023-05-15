/*
 * ChatGPT class communicates with online ChatGPT server to get a response to the question we send.
 */
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;

public class ChatGPT {
	
	private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
	private static final String API_KEY = "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";
	private static final String MODEL = "text-davinci-003";
	private static String question = null;
	public static String answer = null;
	
	public ChatGPT(String question_text) throws IOException, InterruptedException {
		ChatGPT.question = question_text;
		
		//Set request parameters
		String prompt = question;
		int maxTokens = 100;
		
		//Create a request body which you will pass into request object
		JSONObject requestBody = new JSONObject();
		requestBody.put("model", MODEL);
		requestBody.put("prompt", prompt);
		requestBody.put("max_tokens", maxTokens);
		requestBody.put("temperature", 1.0);
		
		//create HTTP client
		HttpClient client = HttpClient.newHttpClient();
		
		//create the request object
		HttpRequest request = HttpRequest
		.newBuilder()
		.uri(URI.create(API_ENDPOINT))
		.header("Content-Type", "application/json")
		.header("Authorization", String.format("Bearer %s",  API_KEY))
		.POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
		.build();
		
		//send request and receive response
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
	
		//process the response
		String responseBody = response.body();
		
		JSONObject responseJson = new JSONObject(responseBody);
		
		JSONArray choices = responseJson.getJSONArray("choices");
		answer = choices.getJSONObject(0).getString("text");
	}

	

	public static void main(String[] args) throws IOException, InterruptedException {
		//Set request parameters
		String prompt = question;
		int maxTokens = 100;
		
		//Create a request body which you will pass into request object
		JSONObject requestBody = new JSONObject();
		requestBody.put("model", MODEL);
		requestBody.put("prompt", prompt);
		requestBody.put("max_tokens", maxTokens);
		requestBody.put("temperature", 1.0);
		
		//create HTTP client
		HttpClient client = HttpClient.newHttpClient();
		
		//create the request object
		HttpRequest request = HttpRequest
		.newBuilder()
		.uri(URI.create(API_ENDPOINT))
		.header("Content-Type", "application/json")
		.header("Authorization", String.format("Bearer %s",  API_KEY))
		.POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
		.build();
		
		//send request and receive response
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
	
		//process the response
		String responseBody = response.body();
		
		JSONObject responseJson = new JSONObject(responseBody);
		
		JSONArray choices = responseJson.getJSONArray("choices");
		answer = choices.getJSONObject(0).getString("text");
	}

}
