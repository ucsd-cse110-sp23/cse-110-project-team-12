package mainframe;
/*
 * AppFrame is the main interface of our program, contains 2 subpanels
 */

import java.awt.*;
import java.io.IOException;

import javax.swing.*;

import interfaces.*;

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
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
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
