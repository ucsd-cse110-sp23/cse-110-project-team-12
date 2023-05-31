package mainframe;
/*
 * AppFrame is the main interface of our program, contains 2 subpanels
 */

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

import listeners.*;
import listeners.interfaces.ButtonSubject;
import server.MyServer;

/*
 * Main interface for application
 * System exits if no server detected
 */
public class AppFrame extends JFrame{

    private static final String TITLE = "SayIt Assistant - Team 12";
    private ButtonPanelPresenter presenter;
        
    LayoutManager afLayout = new BorderLayout();
    
    public AppFrame() throws IOException {
        //TODO
        presenter.loadQuestions();
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
        this.presenter = new ButtonPanelPresenter(addListeners(qp,ph),qp,ph);
           
        this.add(qp, BorderLayout.CENTER); 
        this.add(ph, BorderLayout.WEST); 

        revalidate();
    }

    ArrayList<ButtonSubject> addListeners(QuestionPanel qp,PromptHistory ph){
        ArrayList<ButtonSubject> allButtons = new ArrayList<ButtonSubject>();

        JButton startButton = qp.getStartButton();
        JButton deleteButton = qp.getDeleteButton();
        JButton clearButton = ph.getClearButton(); 
        JList<String> promptList = ph.getPromptList(); 
        
        StartStopListener ssListener = new StartStopListener();
        DeleteListener dListener = new DeleteListener();
        ClearListener cListener = new ClearListener();
        QuestionListHandler lListener = new QuestionListHandler();

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
    
    public static void main (String args[]) throws IOException{
    	AppFrame app = new AppFrame();
        app.setVisible(true);
        //force exit app if server not connected
        MyServer.checkServerAvailability();
    }  
    
}
