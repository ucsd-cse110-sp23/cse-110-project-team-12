package main;
import java.awt.*;
//import java.awt.event.*;
//import java.io.*;
//import java.util.ArrayList;
//import java.net.*;
import javax.swing.*;

public class PromptHistory extends JPanel {
    private JLabel header;
    private JScrollPane pastResults;
    String[] listPH = new String[200];


    LayoutManager phLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
    
    private void configheader(){
        header = new JLabel("Prompt History"); 
        header.setFont(new Font("Sans-serif", Font.BOLD, 40));
        header.setAlignmentX(CENTER_ALIGNMENT);
    }

    private void configpastResults(){
        JList<String> list;
        for (int i = 0; i < 199; i++){
            listPH[i] = "placeholder";
        }
        
        list = new JList<String>(listPH);
        pastResults = new JScrollPane(list);
        // pastResults.setMinimumSize(new Dimension(400, 800));
        pastResults.setPreferredSize(new Dimension(400, 800));
        // pastResults.setMaximumSize(new Dimension(400, 800));

    }
    
    PromptHistory(){
        this.setBackground(Color.cyan);
        this.setLayout(phLayout);
        this.setPreferredSize(new Dimension(400,1000));

        configheader();
        configpastResults();

        this.add(header);
        this.add(pastResults);
    }

}
