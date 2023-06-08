/**
 * @author CSE 110 - Team 12
 */
package api;

import java.io.*;
import java.net.*;
import org.json.*;
import interfaces.WhisperInterface;

/**
 * Class Whisper uses API to take in audio file and translate it to text
 */
public class Whisper implements WhisperInterface{

	//API configurations
	private final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
	private final String TOKEN = "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";
	private final String MODEL = "whisper-1";
	
	private String question_text = null;

	/**
	 * Takes audio file as input and transcripts it with API
	 * 
	 * @param File - audio file
	 */
	public void setWhisperFile(File question_audio) throws IOException {
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

	/**
	 * Gets Whisper's response
	 * 
	 * @return String - Whisper's transcript of audio file
	 */
	public String getQuestionText(){
		return question_text;
	}

	/**
	 * Used by Whisper API to translate audio file
	 * 
	 * @param outputStream
	 * @param parameterName
	 * @param parameterValue
	 * @param boundary
	 * @throws IOException
	 */
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

	/**
	 * Used by Whisper to translate audio file
	 * 
	 * @param outputStream
	 * @param file
	 * @param boundary
	 * @throws IOException
	 */
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

	/**
	 * Called by Whisper if connection succeeded
	 * Sets audio file transcript as question text
	 * 
	 * @param connection
	 * @throws IOException
	 * @throws JSONException
	 */
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

	/**
	 * Called by Whisper if connection failed
	 * 
	 * @param connection
	 * @throws IOException
	 * @throws JSONException
	 */
	private void handleErrorResponse(HttpURLConnection connection) throws IOException, JSONException{
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

}