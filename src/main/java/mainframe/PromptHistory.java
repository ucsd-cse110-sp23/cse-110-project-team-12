/* PromptHistory is a subpanel that contains prior questions.
 * extends JPanel
 * holds:
 * 			header - JLabel
 * 			list of questions in scrolling JList sidebar
 */


package mainframe;
import java.awt.*;

// import java.io.*;
import javax.swing.*;


import processing.*;
import interfaces.PanelSubject;
import mediators.QPHPHButtonPanelPresenter;




public class PromptHistory extends JPanel implements PanelSubject {
  private QPHPHButtonPanelPresenter presenter;  
  private JLabel header;
    private static JScrollPane sideBar;
    private static DefaultListModel<String> listPH;
    private static JList<String> list;
  
    private static final String FONT = "Sans-serif";
    LayoutManager phLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
    
    /*
     * constructor
     */
    public PromptHistory(){
    	
      configBackground();
      configheader();
      configList();
      
      this.add(header);
      this.add(sideBar);
      setVisible(true);
      
  }

  public void onNewEntry(Entry entry){
    if (entry == null){
      //does nothing
  }
  //TODO Dosent check for what kind of entry
    listPH.addElement(entry.getTitle());
 }

//called on delete command
public void removePH(int index){
  listPH.remove(index);
}

//called on clear command
public void resetPH() {
  listPH.clear();
}


///////////////////////////////////////////////////////////////////////GETTER METHODS//////////////////////////////////////////////////////////////////////////////////////////

public JList<String> getPromptList(){
  return list;
}
public ListModel<String> getListModel(){
  return listPH;
}

public int getPHSize(){
  return listPH.getSize();
}

public String getTitle(int index){
  return (String)listPH.getElementAt(index);
}

public int getSelectedIndex(){
  return list.getSelectedIndex();
  }

public int getIndexInPH(String s){
return listPH.indexOf(s);
}





  
///////////////////////////////////////////////////////////////////////SUBJECT METHODS//////////////////////////////////////////////////////////////////////////////////////////


@Override
public void registerObserver(QPHPHButtonPanelPresenter presenter) {
    this.presenter = presenter; 
}


@Override
public void notifyObservers() {
    //
}

///////////////////////////////////////////////////////////////////////PRIVATE CONFIGURE METHODS//////////////////////////////////////////////////////////////////////////////////////////



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
  
}