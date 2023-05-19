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

public class startListener implements ActionListener{
    private Recorder recorder = new Recorder(FormatAudio.getAudioFormat());
    private String URL = "http://localhost:8100/";
    public void actionPerformed(ActionEvent e) {
        if (QuestionPanel.getButtonText() == "New Question") {
            recorder.startRecording();
            QuestionPanel.setButtonText("Stop Recording");
        } else {
          recorder.stopRecording();
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
            PromptHistory.listPH.addElement(QuestionPanel.getQuestion());
            //to show old q's on start up
            PromptHistory.saveQuestions();
            QuestionPanel.setButtonText("New Question");
        }
    }
}