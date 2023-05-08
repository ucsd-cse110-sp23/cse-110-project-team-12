package src;
import java.io.*;
import java.net.*;
import org.json.*;

public class Whisper {
	 private static final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
	 private static final String TOKEN = "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";
	 private static final String MODEL = "whisper-1";
	 private static File question_audio = null;
	 public static String question_text = null;
	 
	 public Whisper(File question_audio) throws IOException {
		 Whisper.question_audio = question_audio;
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

         System.out.println("yaaaaaaaah1");
      

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
		 
		 question_text = responseJson.getString("text");
		 
		 //System.out.println("Transcription Result: " + question_text);
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
    public static void main(String[] args) throws IOException{
    	//"C:/Users/julia/Downloads/250290.mp3"
        File file = question_audio;
        System.out.println("and here");

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

}