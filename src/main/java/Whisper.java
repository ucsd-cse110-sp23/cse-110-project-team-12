/*
 * ChatGPT class communicates with Whisper server to get a translation of the voice recording.
 */

import java.io.*;
import java.net.*;

import javax.swing.JOptionPane;

import org.json.*;


/*
 * Parses audio file into command and prompt
 * command and question instance vars only set if audio started with valid command
 * 
 * Returns error if audio file is blank
 */
public class Whisper {
	 private static final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
	 private static final String TOKEN = "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";
	 private static final String MODEL = "whisper-1";
	 //private static File question_audio = null;
	 private static String command_prompt;
	 private static String command_text = null;
	 private static String question_text = null;
	 private static String[] commands = {"Question", "Send email", "Create email", "Setup email", "Delete prompt", "Clear all"};

	 public Whisper(File question_audio) throws IOException {
		 //Whisper.question_audio = question_audio;
		 File file = question_audio;
		 URL url = new URL(API_ENDPOINT);
         HttpURLConnection connection = (HttpURLConnection) url.openConnection();
         connection.setRequestMethod("POST");
         connection.setDoOutput(true);

         String boundary = "Boundary-" + System.currentTimeMillis();
         connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
         connection.setRequestProperty("Authorization", "Bearer " + TOKEN);

         OutputStream outputStream = connection.getOutputStream();

         writeParameterToOutputStream(outputStream, "model", MODEL, boundary);
         writeFileToOutputStream(outputStream, file, boundary);
         outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
         outputStream.flush();
         outputStream.close();

         int responseCode = connection.getResponseCode();

         if(responseCode== HttpURLConnection.HTTP_OK){
             handleSuccessResponse(connection);
         }
         else{
             handleErrorResponse(connection);
         }
         connection.disconnect();
      

	}
	 
	Whisper(String command, String prompt) {
		this.command_prompt = command + " " + prompt;
		this.command_text = command;
		this.question_text = prompt;
	}
	 
	public String getCommand() {
		return command_text;
	}
	
	public String getPrompt() {
		return question_text;
	}

	public static String getCommandPrompt() {
		return command_prompt;
	}
	
	public void setCommand(String s) {
		command_text = s;
	}
	
	public void setPrompt(String s) {
		question_text = s;
	}
	
	private static void writeParameterToOutputStream(
			 OutputStream outputStream,
			 String parameterName,
			 String parameterValue,
			 String boundary
	) throws IOException {
		 outputStream.write(("--" + boundary + "\r\n").getBytes());
		 outputStream.write(("Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n\r\n").getBytes());
		 outputStream.write((parameterValue + "\r\n").getBytes());
	 }
	 
	 private static void writeFileToOutputStream(
			 OutputStream outputStream,
			 File file,
			 String boundary
	) throws IOException {
		 outputStream.write(("--" + boundary + "\r\n").getBytes());
		 outputStream.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"\r\n").getBytes());
		 outputStream.write(("Content-Type: audio/mpeg\r\n\r\n").getBytes());
		 
		 FileInputStream fileInputStream = new FileInputStream(file);
		 byte[] buffer = new byte[1024];
		 int bytesRead;
		 while ((bytesRead = fileInputStream.read(buffer)) != -1) {
			 outputStream.write(buffer,0, bytesRead);
		 }
		 fileInputStream.close();
	 }
	 
	 private static void handleSuccessResponse(HttpURLConnection connection) throws IOException, JSONException {
		 BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		 String inputLine;
		 StringBuilder response = new StringBuilder();
		 while ((inputLine = in.readLine()) != null) {
			 response.append(inputLine);
		 }
		 in.close();
		 
		 JSONObject responseJson = new JSONObject(response.toString());
		 
		 command_prompt = responseJson.getString("text");
		 
		 if (command_prompt.isBlank()) {
			 JOptionPane.showMessageDialog(null, "No Audio Detected");		 
		 } else {
			 parseCommandPrompt(command_prompt);
		 }
	 }
	 
	 private static void handleErrorResponse(HttpURLConnection connection) throws IOException, JSONException{
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        String errorLine;
        StringBuilder errorResponse = new StringBuilder();
        while ((errorLine = errorReader.readLine()) != null){
            errorResponse.append(errorLine);
        }
        errorReader.close();
        String errorResult = errorResponse.toString();
        System.out.println("Error Result: " + errorResult);
    }
	 
	 
	//	probably breaks srp !!!!!!!!!!??????
	private static void parseCommandPrompt(String s) {
		//command_text and question_text only set if valid command used
		for (int i = 0; i < commands.length; i++) {
			
			if (s.startsWith(commands[i])) {
				command_text = commands[i];
				question_text = s.substring(commands[i].length()+1);
				break;
			}
		}
		
		
	}

}