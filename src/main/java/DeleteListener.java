
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

public class DeleteListener implements myListener{
        
        public void actionPerformed(ActionEvent e) {

            String question = QuestionPanel.getQuestion();
            
            if (question.equals("Your Question will appear here")) {
                //do nothing
            } else {
                
                ServerCalls.deleteFromServer(question);
                
                //TODO: some server call as well delete selected question and answer from prompt history
                
                int questionIndex = PromptHistory.getIndexInPH(QuestionPanel.getQuestion());
                PromptHistory.removePH(questionIndex);
                PromptHistory.resetPH();
                
            }
        }

        public void registerObserver(listenerObserver panel) {
    
        }
    
        public void notifyObservers() {
        }
    
}
