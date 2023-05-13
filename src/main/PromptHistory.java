package main;
import java.awt.*;
//import java.awt.event.*;
//import java.io.*;
//import java.util.ArrayList;
//import java.net.*;
import javax.swing.*;
import java.util.*;

public class PromptHistory extends JPanel {
    private JLabel header;
    private static JScrollPane sideBar;
    public static DefaultListModel listPH;
    private static JList list;

    LayoutManager phLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
    
    private void configheader(){
        header = new JLabel("Prompt History"); 
        header.setFont(new Font("Sans-serif", Font.BOLD, 40));
        header.setAlignmentX(CENTER_ALIGNMENT);
    }
    
    public static void loadPrevQuestions() {
    	/*LinkedHashMap<String,String> data = (LinkedHashMap<String, String>) RequestHandler.getMap();
    	for (String key : data.keySet()) {
    		listPH.addElement(key);
    	}*/
    	list = new JList(listPH);
    	sideBar = new JScrollPane(list);
    }
/*
    public static void configSideBar(){
    	
    	//String[] array = listPH.toArray(new String[listPH.size()]);
        JList list = new JList();
        //list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //list.setSelectedIndex(0);
        list.setVisibleRowCount(1);
        sideBar = new JScrollPane();
       // System.out.println("oo" + list.getModel().getSize());
        // pastResults.setMinimumSize(new Dimension(400, 800));
        sideBar.setPreferredSize(new Dimension(400, 800));
        // pastResults.setMaximumSize(new Dimension(400, 800));
        sideBar.setViewportView(list);
    	

    }*/
    
    public PromptHistory(){
    	
        this.setBackground(Color.cyan);
        this.setLayout(phLayout);
        this.setPreferredSize(new Dimension(400,1000));
        setVisible(true);
        
        configheader();
        listPH = new DefaultListModel();
        list = new JList(listPH);
        sideBar = new JScrollPane(list);
        
        this.add(header);
        this.add(sideBar);
        setVisible(true);
        
    }

}
