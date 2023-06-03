package server;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


import processing.*;

public class ServerCalls {
    
  private static final String  URL = "http://localhost:8100/";

    public static void postToServer(Entry entry){
      if (entry == null){
        return;
      }

      String question = entry.getPrompt();
      String answer = entry.getResult();
        try {
          URL url = new URL(URL);
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();
          conn.setRequestMethod("POST");
          conn.setDoOutput(true);
          OutputStreamWriter out = new OutputStreamWriter(
            conn.getOutputStream()
          );
          out.write(question + "\n" + answer);
          out.flush();
          out.close();
          
          //Read output from http post 
          BufferedReader in = new BufferedReader(
            new InputStreamReader(conn.getInputStream())
          );

          question = in.readLine();
          answer = in.readLine();
          in.close();
          //TODO Boolean if server is not running?
          
          
        } catch (Exception ex) {
          ex.printStackTrace();
        }
    
      }

      public static String getFromServer(String question){
    	  String response = "Getting from server...";
        
    	  try {
          String encodedQuery = URLEncoder.encode(question, "UTF-8");
          URL url = new URL(URL + "?=" + encodedQuery);
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();
          conn.setRequestMethod("GET");
          BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
          response = in.readLine();
          in.close();
          return response; 
          } catch (Exception ex) {
            ex.printStackTrace();
            return response;
        }
      }

      // public static void deleteFromServer(String question){
          
      //   try {
      //     String encodedQuery = URLEncoder.encode(question, "UTF-8");
      //       URL url = new URL(URL + "?=" + encodedQuery);
      //       HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      //       conn.setRequestMethod("DELETE");


      //       //Is this necessary
      //       // BufferedReader in = new BufferedReader(
      //       //   new InputStreamReader(conn.getInputStream())
      //       // );
      //       // in.readLine();
      //       // in.close();
          
      //     } catch (Exception ex) {
      //       ex.printStackTrace();
          
      //     }
      
      // }
}
