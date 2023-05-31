package server;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerCalls {
    
  public static final String  URL = "http://localhost:8100/";

    public static void postToServer(String question, String answer){
      
        try {
          URL url = new URL(URL);
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();
          conn.setRequestMethod("POST");
          conn.setDoOutput(true);
          OutputStreamWriter out = new OutputStreamWriter(
            conn.getOutputStream()
          );
          out.write(question + "," + answer);
          out.flush();
          out.close();
          BufferedReader in = new BufferedReader(
            new InputStreamReader(conn.getInputStream())
          );
          
          in.close();
          
        } catch (Exception ex) {
          ex.printStackTrace();
        }
    
      }

      public static String getFromServer(String query){
        query = query.replace(' ', '+');
        try {
          URL url = new URL(URL + "?=" + query);
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();
          conn.setRequestMethod("GET");
          BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
          String response = in.readLine();
          in.close();
          
         return response; 
      }   
      catch (Exception ex) {
          return null;
      }
      }

      public static void deleteFromServer(String question){
          
        try {
            String query = question.replace(' ', '+');
            URL url = new URL(URL + "?=" + query);
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
