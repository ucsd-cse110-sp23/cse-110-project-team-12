import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


/*
  * clicking DELETE button
  * 
  * Deletes selected question from prompt history and server
  * Resets question and answer fields
  */

public class stopListener implements ActionListener{

    private Recorder recorder = new Recorder(FormatAudio.getAudioFormat());
    private String URL = "http://localhost:8100/" ;    
        
        public void actionPerformed(ActionEvent e) {

            String question = QuestionPanel.getQuestion();
            
            if (question == "Your Question will appear here") {
                //do nothing
            } else {
                
                try {
                    question = question.replace(' ', '+');
                    URL url = new URL(URL + "?=" + question);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("DELETE");
                    BufferedReader in = new BufferedReader(
                      new InputStreamReader(conn.getInputStream())
                    );
                   
                    in.close();
                   
                  } catch (Exception ex) {
                    ex.printStackTrace();
                    
                  }
                
                //delete selected question and answer from prompt history
                
                int questionIndex = PromptHistory.listPH.indexOf(QuestionPanel.getQuestion());
                PromptHistory.listPH.remove(questionIndex);
                PromptHistory.saveQuestions();
                
                QuestionPanel.setQuestion("Your Question will appear here");
                QuestionPanel.setAnswer("Your Answer will appear here");
                
            }
        }
}
