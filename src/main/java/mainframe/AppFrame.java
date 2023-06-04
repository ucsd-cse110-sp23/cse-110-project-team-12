package mainframe;
/*
 * AppFrame is the main interface of our program, contains 2 subpanels
 */

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

import interfaces.ButtonSubject;
import listeners.*;
import mediators.QPHPHButtonPanelPresenter;

/*
 * Main interface for application
 * System exits if no server detected
 */
@SuppressWarnings("serial")
public class AppFrame extends JFrame {

    private static final String TITLE = "SayIt Assistant - Team 12";

    LayoutManager afLayout = new BorderLayout();
    
    public AppFrame(String email) throws IOException {
        //TODO
        //this.email = email;

        //TODO
        // presenter.loadQuestions();
    	this.setTitle(TITLE);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(800, 1000);
        this.setLayout(afLayout);
        
        //windowed fullscreen
        final GraphicsEnvironment graph = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice dev = graph.getDefaultScreenDevice();
        dev.setFullScreenWindow(this);
        
        this.setVisible(true);
    
        final QuestionPanel qp = new QuestionPanel();
        final PromptHistory ph = new PromptHistory();
        new QPHPHButtonPanelPresenter(addListeners(qp,ph),qp,ph);
           
        this.add(qp, BorderLayout.CENTER); 
        this.add(ph, BorderLayout.WEST); 

        revalidate();
    }
    
    public AppFrame(String email, DefaultListModel<String> entries) throws IOException {
        //TODO
        //this.email = email;

        //TODO
        // presenter.loadQuestions();
    	this.setTitle(TITLE);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(800, 1000);
        this.setLayout(afLayout);
        
        //windowed fullscreen
        final GraphicsEnvironment graph = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice dev = graph.getDefaultScreenDevice();
        dev.setFullScreenWindow(this);
        
        this.setVisible(true);
    
        final QuestionPanel qp = new QuestionPanel();
        final PromptHistory ph = new PromptHistory(entries);
        new QPHPHButtonPanelPresenter(addListeners(qp,ph),qp,ph);
           
        this.add(qp, BorderLayout.CENTER); 
        this.add(ph, BorderLayout.WEST); 

        revalidate();
    }

    ArrayList<ButtonSubject> addListeners(QuestionPanel qp,PromptHistory ph){
        final ArrayList<ButtonSubject> allButtons = new ArrayList<>();

        final JButton startButton = qp.getStartButton();
        final JButton deleteButton = qp.getDeleteButton();
        final JButton clearButton = ph.getClearButton(); 
        final JList<String> promptList = ph.getPromptList(); 
        
        final StartStopListener ssListener = new StartStopListener();
        final DeleteListener dListener = new DeleteListener();
        final ClearListener cListener = new ClearListener();
        final QuestionListHandler lListener = new QuestionListHandler();

        startButton.addActionListener(ssListener);
        deleteButton.addActionListener(dListener);
        clearButton.addActionListener(cListener);
        promptList.addListSelectionListener(lListener);

        allButtons.add(ssListener);
        allButtons.add(dListener);
        allButtons.add(cListener);
        allButtons.add(lListener);
        return allButtons;
    }
    
}
