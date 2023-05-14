package main;

import java.awt.*;
import javax.swing.*;

/*
 * main app
 * 
 *  extends JFrame
 *  holds:
 *  	PromptHistory - left
 *  	QuestionPanel - right
 * 
 */
public class AppFrame extends JFrame{
	
    private QuestionPanel qp;
    private PromptHistory ph;
        
    LayoutManager afLayout = new BorderLayout();
    
    public AppFrame() {
    	this.setTitle("SayIt Assistant - Team 12");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 1000); 
        this.setLayout(afLayout);
        this.setVisible(true);
    
        qp = new QuestionPanel();
        ph = new PromptHistory();
           
        this.add(qp, BorderLayout.CENTER); 
        this.add(ph, BorderLayout.WEST); 

        revalidate();
    }
    
    public static void main (String args[]){
        AppFrame app = new AppFrame();
        app.setVisible(true);
        
    }  
    
}
