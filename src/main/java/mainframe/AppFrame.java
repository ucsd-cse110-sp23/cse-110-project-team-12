package mainframe;
/*
 * AppFrame is the main interface of our program, contains 2 subpanels
 */

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

import interfaces.*;
import listeners.*;

/*
 * Main interface for application
 * System exits if no server detected
 */
public class AppFrame extends JFrame implements MediatorObserver{

    private static final String TITLE = "SayIt Assistant - Team 12";
    private QuestionPanel qp;
    private PromptHistory ph;
        
    LayoutManager afLayout = new BorderLayout();
    
    public AppFrame() throws IOException {
    	this.setTitle(TITLE);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(800, 1000);
        this.setLayout(afLayout);
        
        //windowed fullscreen
        // GraphicsEnvironment graph = GraphicsEnvironment.getLocalGraphicsEnvironment();
        // GraphicsDevice dev = graph.getDefaultScreenDevice();
        // dev.setFullScreenWindow(this);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        
        this.setVisible(false);
    
        qp = new QuestionPanel();
        ph = new PromptHistory();
           
        this.add(qp, BorderLayout.CENTER); 
        this.add(ph, BorderLayout.WEST); 
    }

    public QuestionPanel getQuestionPanel(){
        return qp;
    }

    public PromptHistory getPromptHistory(){
        return ph;
    }

    public ArrayList<ButtonSubject> addListeners(QuestionPanel qp,PromptHistory ph){
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

    @Override
    public void onLoginClosing() {
        this.setVisible(true);
        this.setAlwaysOnTop(true);
        this.setAlwaysOnTop(false);
    }

    @Override
    public void onEmailSetup() {
        //
    }
    
}
