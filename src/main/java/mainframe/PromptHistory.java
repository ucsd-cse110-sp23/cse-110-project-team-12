/**
 * @author CSE 110 - Team 12
 */
package mainframe;
import java.awt.*;

// import java.io.*;
import javax.swing.*;


import processing.*;
import interfaces.PanelSubject;
import mediators.QPHPHButtonPanelPresenter;

/** 
 * PromptHistory is a subpanel that contains prior prompts.
 * extends JPanel
 * holds:
 * 			header - JLabel
 * 			list of prompts in scrolling JList sidebar
 */
public class PromptHistory extends JPanel implements PanelSubject {
	private QPHPHButtonPanelPresenter presenter;  
	private JLabel header;
	private static JScrollPane sideBar;
	private static DefaultListModel<String> listPH;
	private static JList<String> list;
	private static final String FONT = "Sans-serif";
	
	LayoutManager phLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);

	/**
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

	/**
	 * Adds new Entry to prompt history
	 * @param entry to be added
	 */
	public void onNewEntry(Entry entry){
		if (entry == null){
			//does nothing
		}
		listPH.addElement(entry.getTitle());
	}

	/**
	 * called on delete command
	 * @param index to delete
	 */
	public void removePH(int index){
		listPH.remove(index);
	}

	/**
	 * called on clear all command
	 */
	public void resetPH() {
		listPH.clear();
	}

	///////////////////////////////////////////////////////////////////////GETTER METHODS//////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Gets list of entries as JList
	 * @return JList of entries
	 */
	public JList<String> getPromptList(){
		return list;
	}
	
	/**
	 * Gets list of entries as ListModel
	 * @return ListModel of entries
	 */
	public ListModel<String> getListModel(){
		return listPH;
	}

	/**
	 * Gets number of entries
	 * @return size of entries
	 */
	public int getPHSize(){
		return listPH.getSize();
	}

	/**
	 * Gets title of entry
	 * 
	 * @param index of entry
	 * @return entry's title
	 */
	public String getTitle(int index){
		return (String)listPH.getElementAt(index);
	}

	/**
	 * Gets selected entry
	 * 
	 * @return entry that is selected in PH
	 */
	public int getSelectedIndex(){
		return list.getSelectedIndex();
	}

	/**
	 * Gets index of selected prompt
	 * 
	 * @param s prompt
	 * @return prompt's index
	 */
	public int getIndexInPH(String s){
		return listPH.indexOf(s);
	}

	///////////////////////////////////////////////////////////////////////SUBJECT METHODS//////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Registers QPPH mediator as observer
	 */
	@Override
	public void registerObserver(QPHPHButtonPanelPresenter presenter) {
		this.presenter = presenter; 
	}

	/**
	 * Does nothing
	 */
	@Override
	public void notifyObservers() {}

	///////////////////////////////////////////////////////////////////////PRIVATE CONFIGURE METHODS//////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Configures list of prompts
	 */
	private static void configList(){
		listPH = new DefaultListModel<>();
		list = new JList<String>(listPH);
		list.setFont(new Font(FONT, Font.PLAIN, 20));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sideBar = new JScrollPane(list);
		sideBar.setPreferredSize(new Dimension(400, 800));
	}

	/**
	 * configures background
	 */
	private void configBackground(){
		this.setBackground(Color.cyan);
		this.setLayout(phLayout);
		this.setPreferredSize(new Dimension(400,1000));
		setVisible(true);
	}

	/**
	 * configures header
	 */
	private void configheader(){
		header = new JLabel("Prompt History"); 
		header.setFont(new Font(FONT, Font.BOLD, 40));
		header.setAlignmentX(CENTER_ALIGNMENT);
	}

}