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
@SuppressWarnings("serial")
public class AppFrame extends JFrame{
	
    private QuestionPanel qp;
    private PromptHistory ph;
    //private static LoginScreen login;
    private static boolean autoLogin = false;
    private static String defaultUserEmail; //TODO: set when user accepts auto login
    //private String email;
        
    LayoutManager afLayout = new BorderLayout();
    
    public AppFrame(String email) throws IOException {
    	//this.email = email;
    	
    	this.setTitle("SayIt Assistant - Team 12");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 1000);
        this.setLayout(afLayout);
        
        //windowed fullscreen
        GraphicsEnvironment graph = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice dev = graph.getDefaultScreenDevice();
        dev.setFullScreenWindow(this);
        
        this.setVisible(true);
    
        qp = new QuestionPanel(email);
        ph = new PromptHistory(email);
           
        this.add(qp, BorderLayout.CENTER); 
        this.add(ph, BorderLayout.WEST); 

        revalidate();
    }
    
    public static void main (String args[]) throws IOException{
    	//TODO: if automatic login is true, skip login screen
    	
    	if (!autoLogin) {
	    	//login = new LoginScreen();
    	} else {

    		AppFrame app = new AppFrame(defaultUserEmail);
    		app.setVisible(true);
            //force exit app if server not connected
            MyServer.checkServerAvailability();
    	}	     
    	
    }  
    
}
