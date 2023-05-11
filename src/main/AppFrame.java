package main;
// package src.main.java;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.net.*;
import javax.swing.*;

public class AppFrame extends JFrame{
	
    private QuestionPanel qp;
    private PromptHistory ph;
        
    LayoutManager afLayout = new BorderLayout();

    public AppFrame() {
    	this.setTitle("SayIt Assistant");
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
        new AppFrame();
    }  
    
}

