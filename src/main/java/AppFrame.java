/*
 * AppFrame is the main interface of our program, contains 2 subpanels
 */

import java.awt.*;
import java.io.IOException;

import javax.swing.*;

/*
 * Main interface for application
 * System exits if no server detected
 */
public class AppFrame extends JFrame{
	
    private QuestionPanel qp;
    private PromptHistory ph;
        
    LayoutManager afLayout = new BorderLayout();
    
    public AppFrame() throws IOException {
    	this.setTitle("SayIt Assistant - Team 12");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 1000);
        this.setLayout(afLayout);
        
        //windowed fullscreen
        GraphicsEnvironment graph = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice dev = graph.getDefaultScreenDevice();
        dev.setFullScreenWindow(this);
        
        this.setVisible(true);
    
        qp = new QuestionPanel();
        ph = new PromptHistory();
           
        this.add(qp, BorderLayout.CENTER); 
        this.add(ph, BorderLayout.WEST); 

        revalidate();
    }
    
    public static void main (String args[]) throws IOException{
    	AppFrame app = new AppFrame();
        app.setVisible(true);
        //force exit app if server not connected
        MyServer.checkServerAvailability();
    }  
    
}
