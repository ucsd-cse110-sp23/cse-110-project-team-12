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
        String question = (String) ((JList) e.getSource()).getSelectedValue();
        //String query = question.replace(' ', '+');
        String response = ServerCalls.getFromServer(question);

        //subject: QuestionListHandler, observer: QuestionPanel
        QuestionPanel.setQuestion(question);
        QuestionPanel.setAnswer(response); 
    }
}