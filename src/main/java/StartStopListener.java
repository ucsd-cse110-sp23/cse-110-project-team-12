import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
/*
         * clicking START button
         * 
         * Starts and stops recording, changes labels accordingly
         * 
         * After recording stopped:
         *       add <question,answer> to server
         *       add "question" to prompt history list
         */

public class StartStopListener implements myListener{
    
    public void actionPerformed(ActionEvent e) {
        if (QuestionPanel.getButtonText().equals("New Question")) {
            RECORDER.startRecording();
            QuestionPanel.setButtonText("Stop Recording");
        } else {
          RECORDER.stopRecording();
            //add question to server
            try {
                  URL url = new URL(URL);
                  HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                  conn.setRequestMethod("POST");
                  conn.setDoOutput(true);
                  OutputStreamWriter out = new OutputStreamWriter(
                    conn.getOutputStream()
                  );
                  out.write(QuestionPanel.getQuestion() + "," + QuestionPanel.getAnswer());
                  out.flush();
                  out.close();
                  BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
                  );
                  
                  in.close();
                  
                } catch (Exception ex) {
                  ex.printStackTrace();
                  
                }
            
            //add question to prompt history list
            PromptHistory.addPH(QuestionPanel.getQuestion());
            //to show old q's on start up
            QuestionLoader.saveQuestions();
            QuestionPanel.setButtonText("New Question");
        }
    }
}