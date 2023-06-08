/**
 * @author CSE 110 - Team 12
 */
package api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;
import interfaces.ChatGPTInterface;
import org.json.JSONArray;
import java.io.IOException;

/**
 * ChatGPT class communicates with online ChatGPT server to get a generated response
 */
public class ChatGPT implements ChatGPTInterface {
	
	//configurations
	private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
	private static final String API_KEY = "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";
	private static final String MODEL = "text-davinci-003";
	
	private String input = null;
	private String answer = null;
	
	/**
	 * Passes input to ChatGPT and saves response
	 * Input can be a question or request to create email draft
	 * 
	 * @param String input
	 */
	public void askChatGPT(String input) throws IOException, InterruptedException {
		this.input = input;
		
		//Set request parameters
		String prompt = this.input;
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

	/**
	 * Getter for ChatGPT's generated response
	 * 
	 * @return String - the generated response
	 */
	public String getAnswer(){
		return answer;
	}

}
