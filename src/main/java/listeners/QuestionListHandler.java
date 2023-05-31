package listeners;
/*
 * QuestionListHandler handles selections for scrolling JList sidebar of previous questions
 * implements ListSelectionListener
 */

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import interfaces.ButtonSubject;
import mediators.QPHPHButtonPanelPresenter;
import server.ServerCalls;


public class QuestionListHandler implements ListSelectionListener, ButtonSubject{
    QPHPHButtonPanelPresenter presenter;
    String question;
    String answer;
	/*
	 * gets selected question from server upon clicking
	 * displays saved answer
	 */
    @SuppressWarnings("unchecked")
    public void valueChanged(ListSelectionEvent e){
        question = (String) ((JList<String>) e.getSource()).getSelectedValue();
        if (question != null){
            answer = ServerCalls.getFromServer(question);
            notifyObservers();
        }
    }

    public void registerObserver(QPHPHButtonPanelPresenter presenter) {
        this.presenter = presenter;
    }

    public void notifyObservers(){
        presenter.onListChange(question, answer);
    }
}