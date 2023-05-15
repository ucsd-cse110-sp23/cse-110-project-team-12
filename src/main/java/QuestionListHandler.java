/*
 * QuestionListHandler handles selections for scrolling JList sidebar of previous questions
 * implements ListSelectionListener
 */

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


class QuestionListHandler implements ListSelectionListener{
    
	/*
	 * gets selected question from server upon clicking
	 * displays saved answer
	 */
    public void valueChanged(ListSelectionEvent e){

            try {
                
        		JList selectedQuestion = (JList) e.getSource();
                String question = (String) selectedQuestion.getSelectedValue();
                String query = question.replace(' ', '+');
                String URL = "http://localhost:8100/";
                URL url = new URL(URL + "?=" + query);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
            

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String response = in.readLine();
                in.close();
                
                QuestionPanel.setQuestion(question.replace('+', ' '));
                QuestionPanel.setAnswer(response);    
            }   
            catch (Exception ex) {
                //do nothing
            } 
       
    }
}