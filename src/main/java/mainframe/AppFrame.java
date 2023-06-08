/**
 * @author CSE 110 - Team 12
 */
package mainframe;/*

 */

import java.awt.*;
import java.io.IOException;

import javax.swing.*;

import interfaces.*;

/**
 * AppFrame is the main interface of our program, contains 2 subpanels:
 * 	QuestionPanel
 * 	PromptHistory
 */
public class AppFrame extends JFrame implements MediatorObserver{

    private static final String TITLE = "SayIt Assistant - Team 12";
    private QuestionPanel qp;
    private PromptHistory ph;
        
    LayoutManager afLayout = new BorderLayout();
    
    /**
     * AppFrame constructor
     * 
     * @throws IOException
     */
    public AppFrame() throws IOException {
    	this.setTitle(TITLE);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setSize(800, 1000);
        this.setLayout(afLayout);
        
        //windowed fullscreen
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        
        this.setVisible(false);
    
        qp = new QuestionPanel();
        ph = new PromptHistory();
           
        this.add(qp, BorderLayout.CENTER); 
        this.add(ph, BorderLayout.WEST); 
    }

    /**
     * Gets frame's QuestionPanel
     * @return QuestionPanel
     */
    public QuestionPanel getQuestionPanel(){
        return qp;
    }

    /**
     * Gets frame's PromptHistory panel
     * @return PromptHistory
     */
    public PromptHistory getPromptHistory(){
        return ph;
    }

    /**
     * AppFrame opens when LoginFrame closes
     */
    @Override
    public void onLoginClosing() {
        this.setVisible(true);
        this.setAlwaysOnTop(true);
        this.setAlwaysOnTop(false);
    }

    /**
     * Does nothing
     */
    @Override
    public void onEmailSetup() {}

    /**
     * Does nothing
     */
	@Override
	public void onCancel() {}
    
}
