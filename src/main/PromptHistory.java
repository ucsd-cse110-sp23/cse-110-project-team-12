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
<<<<<<< HEAD
    private static String filePath = "src/main/questionFile.txt";
=======
    private static String filePath = "C:\\Users\\julia\\eclipse-work\\cse-110-project-team-12\\src\\main\\questionFile.txt";
>>>>>>> f2cdc2371df219b10a5eab577c13a0e110dcb998
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
<<<<<<< HEAD
=======
    
    public void loadQuestions() {
    	
        try {
        	
        	BufferedReader questionFile = new BufferedReader(new FileReader(filePath));
        	
        	String currLine;
        	//ArrayList<String> list = new ArrayList<>();
        	
        	while ((currLine = questionFile.readLine()) != null) {
        		//System.out.println(currLine);
        		//String question = new Task();
        		listPH.addElement(currLine);
        		//task.taskName.setText(currLine);
        		//this.add(currLine);
        		//list.add(currLine);
        	}
        	
        	
        	
        	questionFile.close();
        	//updateNumbers();
        	revalidate();
        	//return list;
        } catch (IOException e){
        	System.out.println("loadQuestions() not implemented");
            //return null;
        }
        
      }
    
    public static void saveQuestions() {
        // 1: use try-catch block
        // 2: use FileWriter
        // 3 get list of Tasks using this.getComponents()
    	// 4: to iterate over list of tasks, look at updateNumbers() method
    	try {
    		FileWriter questionFile = new FileWriter(filePath);
    		
    		//Component[] listItems = this.getComponents();
    		for (int i = 0; i < listPH.getSize(); i++) {
    			
				questionFile.write((String)listPH.elementAt(i));
				questionFile.write("\n");
    			
    		}
    		questionFile.close();
    	} catch (IOException e) {
    	    System.out.println("saveQuestions() not implemented");
    	}	  
      }
    /*
    public static void loadPrevQuestions() {
    	/*LinkedHashMap<String,String> data = (LinkedHashMap<String, String>) RequestHandler.getMap();
    	for (String key : data.keySet()) {
    		listPH.addElement(key);
    	}
    	list = new JList(listPH);
    	sideBar = new JScrollPane(list);
    } */
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
>>>>>>> f2cdc2371df219b10a5eab577c13a0e110dcb998
    
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
    
<<<<<<< HEAD
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
=======
    public void addListeners() {
>>>>>>> f2cdc2371df219b10a5eab577c13a0e110dcb998
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
<<<<<<< HEAD
	    			           
	    			          } catch (Exception ex) {
	    			            ex.printStackTrace();
	    			           
=======
	    			            JOptionPane.showMessageDialog(null, response);
	    			          } catch (Exception ex) {
	    			            ex.printStackTrace();
	    			            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
>>>>>>> f2cdc2371df219b10a5eab577c13a0e110dcb998
	    			          }
    					}
    					listPH.clear();
    					saveQuestions();
    				}
    			}
    	);
    }

}