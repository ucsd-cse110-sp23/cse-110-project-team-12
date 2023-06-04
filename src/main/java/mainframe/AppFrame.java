package mainframe;
/*
 * AppFrame is the main interface of our program, contains 2 subpanels
 */

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.LayoutManager;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.WindowConstants;

import interfaces.ButtonSubject;
import mediators.QPHPHButtonPanelPresenter;
import processing.Recorder;
import listeners.*;

/*
 * Main interface for application
 * System exits if no server detected
 */
public class AppFrame extends JFrame{

    private static final String TITLE = "SayIt Assistant - Team 12";

    private QPHPHButtonPanelPresenter presenter;
        
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
        GraphicsEnvironment graph = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice dev = graph.getDefaultScreenDevice();
        dev.setFullScreenWindow(this);
        
        this.setVisible(true);
    
        QuestionPanel qp = new QuestionPanel();
        PromptHistory ph = new PromptHistory();
        this.presenter = new QPHPHButtonPanelPresenter(addListeners(qp,ph),qp,ph, new Recorder(), null);
           
        this.add(qp, BorderLayout.CENTER); 
        this.add(ph, BorderLayout.WEST); 

        revalidate();
    }

    ArrayList<ButtonSubject> addListeners(QuestionPanel qp,PromptHistory ph){
        ArrayList<ButtonSubject> allButtons = new ArrayList<ButtonSubject>();

        JButton startButton = qp.getStartButton();
        // JButton clearButton = ph.getClearButton(); 
        JList<String> promptList = ph.getPromptList(); 
        
        StartStopListener ssListener = new StartStopListener();
        // ClearListener cListener = new ClearListener();
        QuestionListHandler lListener = new QuestionListHandler();

        startButton.addActionListener(ssListener);
        // clearButton.addActionListener(cListener);
        promptList.addListSelectionListener(lListener);

        allButtons.add(ssListener);
        // allButtons.add(cListener);
        allButtons.add(lListener);
        return allButtons;
    }
    
}
