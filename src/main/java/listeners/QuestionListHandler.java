/**
 * @author CSE 110 - Team 12
 */
package listeners;/*

 * implements ListSelectionListener
 */

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import interfaces.ButtonSubject;
import mediators.QPHPHButtonPanelPresenter;

/**
 * QuestionListHandler handles selections for scrolling JList sidebar of previous questions
 */
public class QuestionListHandler implements ListSelectionListener, ButtonSubject{
    QPHPHButtonPanelPresenter presenter;
    String question;
	/*
	 * gets selected question from server upon clicking
	 * displays saved answer
	 */
    @SuppressWarnings("unchecked")
    public void valueChanged(ListSelectionEvent e){
        question = (String) ((JList<String>) e.getSource()).getSelectedValue();
        if (question != null){
            notifyObservers();
        }
    }

    public void registerObserver(QPHPHButtonPanelPresenter presenter) {
        this.presenter = presenter;
    }

    public void notifyObservers(){
        presenter.onListChange(question);
    }
}