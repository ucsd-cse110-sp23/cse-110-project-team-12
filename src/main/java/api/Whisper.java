package api;
/*
 * ChatGPT class communicates with Whisper server to get a translation of the voice recording.
 */

import java.io.*;
import java.net.*;
import org.json.*;

import interfaces.WhisperInterface;

public class Whisper implements WhisperInterface{
	 private final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
	 private final String TOKEN = "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";
	 private final String MODEL = "whisper-1";
	 private String question_text = null;
	 
	 int TEMPCOUNT = 0;
	 int TEMPCOUNT = 0;

	  public void setWhisperFile(File question_audio) throws IOException {
		//  File file = question_audio;
		//  URL url = new URL(API_ENDPOINT);
        //  HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //  connection.setRequestMethod("POST");
        //  connection.setDoOutput(true);

        //  String boundary = "Boundary-" + System.currentTimeMillis();
        //  connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        //  connection.setRequestProperty("Authorization", "Bearer " + TOKEN);

        //  OutputStream outputStream = connection.getOutputStream();

        //  writeParameterToOutputStream(outputStream, "model", MODEL, boundary);
        //  writeFileToOutputStream(outputStream, file, boundary);
        //  outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
        //  outputStream.flush();
        //  outputStream.close();

        //  int responseCode = connection.getResponseCode();

        //  if(responseCode== HttpURLConnection.HTTP_OK){
        //      handleSuccessResponse(connection);
        //  }
        //  else{
        //      handleErrorResponse(connection);
        //  }
        //  connection.disconnect();
      

	}

	public String getQuestionText(){
		// TEMPCOUNT++;
		// return "Setup Email" + TEMPCOUNT;
		return question_text;

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
	 
	 private void writeFileToOutputStream(
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
	 
	 private void handleSuccessResponse(HttpURLConnection connection) throws IOException, JSONException {
		 BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		 String inputLine;
		 StringBuilder response = new StringBuilder();
		 while ((inputLine = in.readLine()) != null) {
			 response.append(inputLine);
		 }
		 in.close();
		 
		 JSONObject responseJson = new JSONObject(response.toString());
		 
		 String responseString = responseJson.getString("text");
		 if (!responseString.isBlank()) {
			question_text = responseJson.getString("text");
		 }		 
	 }
	 
	 private void handleErrorResponse(HttpURLConnection connection) throws IOException, JSONException{
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        String errorLine;
        StringBuilder errorResponse = new StringBuilder();
        while ((errorLine = errorReader.readLine()) != null){
            errorResponse.append(errorLine);
        }
        errorReader.close();
        String errorResult = errorResponse.toString();
		//TODO Deal with println as an exception instead.
        System.out.println("Error Result: " + errorResult);
    }

}