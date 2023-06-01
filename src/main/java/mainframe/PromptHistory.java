/* PromptHistory is a subpanel that contains prior questions.
 * extends JPanel
 * holds:
 * 			header - JLabel
 * 			list of questions in scrolling JList sidebar
 * 			clearAll - JButton
 */


package mainframe;
import java.awt.*;
// import java.io.*;
import javax.swing.*;

import interfaces.PanelSubject;
import mediators.QPHPHButtonPanelPresenter;




public class PromptHistory extends JPanel implements PanelSubject {
  private QPHPHButtonPanelPresenter presenter;  
  private JLabel header;
    private static JScrollPane sideBar;
    private static DefaultListModel<String> listPH;
    private static JList<String> list;
    private JButton clearAll;
  
    private static final String FONT = "Sans-serif";
    LayoutManager phLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);


    
    /*
     * constructor
     */
    public PromptHistory(){
    	
      configBackground();
      configheader();
      configClearAll();
      // loadQuestions();
      configList();
      
      this.add(header);
      this.add(sideBar);
      this.add(clearAll);
      setVisible(true);
      
      
  }

  //ON NOTIFY() METHODS 

  @Override
  public void registerObserver(QPHPHButtonPanelPresenter presenter) {
      this.presenter = presenter; 
  }


  @Override
  public void notifyObservers() {
      //
  }

  //ClearButton getter 

  public JButton getClearButton(){
    return clearAll;      
}

//List Editors and getter

  public void resetPH() {
      listPH.clear();
  }

  public int getPHSize(){
      return listPH.getSize();
  }

  public String getElementInPH(int index){
      return (String)listPH.getElementAt(index);
  }

  public int getIndexInPH(String s){
    return listPH.indexOf(s);
}

  public void addPH(String s){
    listPH.addElement(s);
  }

  public void removePH(int index){
    listPH.remove(index);
  }


    public JList<String> getPromptList(){
    return list;
  }


  //Configure and Create elements in PH


  private static void configList(){
    listPH = new DefaultListModel<>();
    list = new JList<String>(listPH);
    list.setFont(new Font(FONT, Font.PLAIN, 20));
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
      header.setFont(new Font(FONT, Font.BOLD, 40));
      header.setAlignmentX(CENTER_ALIGNMENT);
  }
  
  private void configClearAll() {
    clearAll = new JButton("Clear All");
    clearAll.setFont(new Font(FONT, Font.PLAIN, 24));
      clearAll.setAlignmentX(CENTER_ALIGNMENT);
  }    





}