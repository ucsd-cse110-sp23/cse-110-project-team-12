package main;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
//import java.util.ArrayList;
//import java.net.*;
import javax.swing.*;
import java.util.*;
import java.net.HttpURLConnection;
import java.net.URL;

/* PromptHistory
 * extends JPanel
 * holds:
 * 			header - JLabel
 * 			list of questions in scrolling JList sidebar
 * 			clearAll - JButton
 */
public class PromptHistory extends JPanel {
    private JLabel header;
    private static JScrollPane sideBar;
    public static DefaultListModel listPH;
    private static JList list;
    private static String filePath = "src/main/questionFile.txt";
    private JButton clearAll;

    LayoutManager phLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);

    private void configheader(){
        header = new JLabel("Prompt History"); 
        header.setFont(new Font("Sans-serif", Font.BOLD, 40));
        header.setAlignmentX(CENTER_ALIGNMENT);
    }
    
    private void configClearAll() {
    	clearAll = new JButton("Clear All");
    	clearAll.setFont(new Font("Sans-serif", Font.PLAIN, 24));
        clearAll.setAlignmentX(CENTER_ALIGNMENT);
    }
    
    /*
     * load asked questions from text file
     */
    public void loadQuestions() {
    	
        try {
        	BufferedReader questionFile = new BufferedReader(new FileReader(filePath));
        	String currLine;
  
        	while ((currLine = questionFile.readLine()) != null) {      		
        		listPH.addElement(currLine);
        	}
        	
        	questionFile.close();
        	revalidate();
        	
        } catch (IOException e){
        	System.out.println("loadQuestions() failed");
        }
        
      }
    
    /*
     * save asked questions to text file
     */
    public static void saveQuestions() {
    	try {
    		FileWriter questionFile = new FileWriter(filePath);
    		
    		for (int i = 0; i < listPH.getSize(); i++) {
    			
				questionFile.write((String)listPH.elementAt(i));
				questionFile.write("\n");
    			
    		}
    		questionFile.close();
    	} catch (IOException e) {
    	    System.out.println("saveQuestions() failed");
    	}	  
      }
    
    /*
     * constructor
     */
    public PromptHistory(){
    	
        this.setBackground(Color.cyan);
        this.setLayout(phLayout);
        this.setPreferredSize(new Dimension(400,1000));
        setVisible(true);
        
        configheader();
        configClearAll();
        listPH = new DefaultListModel();
        loadQuestions();
        list = new JList(listPH);
        list.setFont(new Font("Sans-serif", Font.PLAIN, 20));

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(new QuestionListHandler());
        sideBar = new JScrollPane(list);
        sideBar.setPreferredSize(new Dimension(400, 800));
        
        this.add(header);
        this.add(sideBar);
        this.add(clearAll);
        setVisible(true);
        
        addListeners();
        
    }
    
    /*
     * add functionality to clear all button
     */
    public void addListeners() {
    	
    	/*
    	 * clicking CLEAR ALL button
    	 * 
    	 * Deletes every q/a from server
    	 * Clears and updates prompt history list
    	 */
    	clearAll.addActionListener(
    			new ActionListener() {
    				@Override
    				public void actionPerformed(ActionEvent e) {
    					
    					String URL = "http://localhost:8100/";
    					for (int i = 0; i < listPH.getSize(); i++) {
	    					try {
	    			            String question = (String)listPH.getElementAt(i);
	    			            question = question.replace(' ', '+');
	    			            URL url = new URL(URL + "?=" + question);
	    			            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    			            conn.setRequestMethod("DELETE");
	    			            BufferedReader in = new BufferedReader(
	    			              new InputStreamReader(conn.getInputStream())
	    			            );
	    			            String response = in.readLine();
	    			            in.close();
	    			           
	    			          } catch (Exception ex) {
	    			            ex.printStackTrace();
	    			           
	    			          }
    					}
    					listPH.clear();
    					saveQuestions();
    				}
    			}
    	);
    }

}