import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerCalls {
    public static final String  URL = "http://localhost:8100/";

    public static void postToServer(String query, String value){
      
        try {
        	
          //format input for URL
          query = query.replace(" ", "%20");
          query = query.replace("\n", "%0A");
          query = query.replace(",", "%2C");
          value = value.replace(" ", "%20");
          value = value.replace("\n", "%0A");
          value = value.replace(",", "%2C");
          
          URL url = new URL(URL);
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();
          conn.setRequestMethod("POST");
          conn.setDoOutput(true);
          OutputStreamWriter out = new OutputStreamWriter(
            conn.getOutputStream()
          );
          out.write(query + "," + value);
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
    	  String response = "Getting from server...";
    	  try {
    		  query = query.replace(" ", "%20");
              query = query.replace("\n", "%0A");
              query = query.replace(",", "%2C");

              
    		  URL url = new URL(URL + "?=" + query);
			  HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			  conn.setRequestMethod("GET");
			  
			
			  BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			  response = in.readLine();
			  
			  //format response back
			  response = response.replace("%20", " ");
		      response = response.replace("%0A", "\n");
		      response = response.replace("%2C", ",");
		      
			  in.close();
			  
			  return response; 
			  
    	  } catch (Exception ex) {
    		  return response;
		  }
      }

      public static void deleteFromServer(String query){
          
        try {
        	query = query.replace(" ", "%20");
            query = query.replace("\n", "%0A");
            query = query.replace(",", "%2C");
            
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
