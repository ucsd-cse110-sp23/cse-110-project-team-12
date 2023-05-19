import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/*
  * clicking CLEAR ALL button
  * 
  * Deletes every q/a from server
  * Clears and updates prompt history list
  */

public class ClearListener implements myListener{
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < PromptHistory.getPHSize(); i++) {
            try {
                String question = PromptHistory.getElementInPH(i);
                question = question.replace(' ', '+');
                URL url = new URL(URL + "?=" + question);
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
        PromptHistory.resetPH();
    }
}

