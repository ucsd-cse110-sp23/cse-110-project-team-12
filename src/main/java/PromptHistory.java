/* PromptHistory is a subpanel that contains prior questions.
 * extends JPanel
 * holds:
 * 			header - JLabel
 * 			list of questions in scrolling JList sidebar
 * 			clearAll - JButton
 */

import java.awt.*;
import java.io.*;
import javax.swing.*;


@SuppressWarnings("serial")
public class PromptHistory extends JPanel implements listenerObserver {
    private JLabel header;
    private static JScrollPane sideBar;
    private static DefaultListModel<String> listPH;
    private static JList<String> list;
    private static String filePath = "bin/main/questionFile.txt";
    //private String username;
    LayoutManager phLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
    
    /*
     * constructor
     */
    public PromptHistory(String username){
    //this.username = username;
    	
      configBackground();
      configheader();
      loadEntries();
      configList();
      
      this.add(header);
      this.add(sideBar);
      //this.add(clearAll);
      setVisible(true);
      
      addListeners();
      
  }

  public void onListenerChange(){
// some sort of server check to update the prompt history
  }

  public static void resetPH() {
      listPH.clear();
      QuestionPanel.setQuestion("Your Question will appear here");
      QuestionPanel.setAnswer("Your Answer will appear here");
      //saveQuestions();
  }

  public static int getPHSize(){
      return listPH.getSize();
  }

  public static String getElementInPH(int index){
      return (String)listPH.getElementAt(index);
  }

  public static int getIndexInPH(String s){
    return listPH.indexOf(s);
}

  public static void addPH(String s){
    listPH.addElement(s);
  }

  public static void removePH(int index){
    listPH.remove(index);
  }

  
   /* makes prompt history clickable
    * add functionality to clear all button
    */
    private void addListeners() {
      list.addListSelectionListener(new QuestionListHandler());
      //clearAll.addActionListener(new ClearListener());
    }

  private void configList(){

	
    listPH = new DefaultListModel<>();
    list = new JList<String>(listPH);
    list.setFont(new Font("Sans-serif", Font.PLAIN, 20));
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    sideBar = new JScrollPane(list);
    sideBar.setPreferredSize(new Dimension(400, 800));
  }

  private void configBackground(){
    this.setBackground(Color.cyan);
    this.setLayout(phLayout);
    this.setPreferredSize(new Dimension(400,1000));
    setVisible(true);
  }
  private void configheader(){
      header = new JLabel("Prompt History"); 
      header.setFont(new Font("Sans-serif", Font.BOLD, 40));
      header.setAlignmentX(CENTER_ALIGNMENT);
  }   

  /*
* save asked questions to text file

public static void saveQuestions() {
  try {
    FileWriter questionFile = new FileWriter(filePath);
    
    for (int i = 0; i < PromptHistory.getPHSize(); i++) {
      
    questionFile.write(PromptHistory.getElementInPH(i));
    questionFile.write("\n");
      
    }
    questionFile.close();
  } catch (IOException e) {
      System.out.println("saveQuestions() failed");
  }	  
}*/


 /*
* load asked questions from text file
*/
public static void loadEntries() {

  try {
    BufferedReader questionFile = new BufferedReader(new FileReader(filePath));
    String currLine;

    while ((currLine = questionFile.readLine()) != null) {      		
      PromptHistory.addPH(currLine);
    }
    
    questionFile.close();
    
  } catch (IOException e){
    //System.out.println("loadQuestions() failed");
  }
  
}

}