/**
 * @author CSE 110 - Team 12
 */
package mainframe;

import java.awt.*;
import javax.swing.*;

import interfaces.PanelSubject;
import mediators.QPHPHButtonPanelPresenter;
import processing.*;

/**  
 *  QuestionPanel is a subpanel that holds our input/message field
 *  Has recording functionality
 *  
 *  holds:
 *  	Title JLabel
 *  	Question JTextArea
 *  	Answer JTextArea
 *  	StartButton JButton
 */
public class QuestionPanel extends JPanel implements PanelSubject{

	private JLabel title;
	private JTextArea question, answer;
	private static JLabel recordingLabel;
	private static JButton startButton;
	private static final String FONT = "Sans-serif";
	private QPHPHButtonPanelPresenter presenter;
	private static final String DEFAULT_QUESTION = "Your query will appear here";
	private static final String DEFAULT_ANSWER = "Your result will appear here";

	LayoutManager qpLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);    

	/**
	 * QuestionPanel constructor
	 */
	public QuestionPanel(){

		this.setBackground(Color.green); // set background color of task
		this.setLayout(qpLayout);

		configTitle();
		configquestion();
		configanswer();
		configstartButton();
		configrecordingLabel();

		this.add(Box.createVerticalGlue());
		this.add(title);
		this.add(Box.createVerticalGlue());
		this.add(question);
		this.add(answer);
		this.add(Box.createRigidArea(new Dimension(100,40)));
		this.add(recordingLabel);
		this.add(startButton);
		this.add(Box.createRigidArea(new Dimension(100,20)));
	}

	/**
	 * Changes button label to "Stop" when starting to record
	 */
	public void startedRecording(){
		setRecordingLableVisible();
		setStartButtonText("Stop");
	}

	/**
	 * Changes button label to "Start" when stopping recording
	 */
	public void stoppedRecording(){
		setRecordingLableInvisible();
		setStartButtonText("Start");
	}

	/**
	 * Adds new Entry to panel
	 * Sets command/prompt and response
	 * 
	 * @param Entry - Entry to be added
	 */
	public void onNewEntry(Entry entry){
		if (entry == null){
			setQuestion("Invalid Input");
		}
		if (entry.getCommand() != null){
			setQuestion(entry.getTitle());
		}
		if (entry.getPrompt() != null){
			setAnswer(entry.getResult());
		}
	}

	/**
	 * Resets fields when prompt deleted
	 */
	public void onDelete(){
		setQuestion(DEFAULT_QUESTION);
		setAnswer(DEFAULT_ANSWER);
	}

	/**
	 * Sets response for invalid input
	 * 
	 * @param String - invalid input
	 */
	public void InvalidInputDetected(String question){
		setQuestion(question);
		setAnswer("Invalid Input Detected");
	}

	/**
	 * Sets panel's fields when new Entry selected in PH
	 * 
	 * @param title - to be set
	 * @param answer - to be set
	 */
	public void onListChange(String title, String answer){
		setQuestion(title);
		setAnswer(answer);
	}

	/**
	 * Gets panel's start button
	 * 
	 * @return JButton start button
	 */
	public JButton getStartButton(){
		return startButton;      
	}

	/**
	 * Sets text for panel's start button
	 * 
	 * @param s - to be set
	 */
	private void setStartButtonText(String s){
		startButton.setText(s);
	}

	/**
	 * Makes recording label visible when recording
	 */
	private void setRecordingLableVisible(){
		recordingLabel.setVisible(true);
	}

	/**
	 * Makes recording label invisible when not recording
	 */
	private void setRecordingLableInvisible(){
		recordingLabel.setVisible(false);
	}

	/**
	 * Set panel's command/prompt
	 * 
	 * @param String - command/prompt to be set
	 */
	private void setQuestion(String q) {
		question.setText(q);
	}

	/**
	 * Set panel's response
	 * 
	 * @param String - response to be set
	 */
	private void setAnswer(String a) {
		answer.setText(a);
	}

	/**
	 * Get question from panel
	 * 
	 * @return String - question
	 */
	public String getQuestion() {
		return question.getText();
	}

	/**
	 * Get result from panel
	 * 
	 * @return String result
	 */
	public String getAnswer() {
		return answer.getText();
	}

	/**
	 * onNotify method
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

	/**
	 * configure panel's title
	 */
	private void configTitle(){
		title = new JLabel("SayIt Assistant", SwingConstants.CENTER);
		title.setFont(new Font(FONT, Font.BOLD, 30));
		title.setAlignmentX(CENTER_ALIGNMENT);
	}

	/**
	 * configure panel's command/prompt field
	 */
	private void configquestion(){
		question = new JTextArea(DEFAULT_QUESTION);
		question.setEditable(false);
		question.setLineWrap(true);
		question.setAlignmentX(CENTER_ALIGNMENT);
		question.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
				question.getBorder()));
		question.setPreferredSize(new Dimension(600,200));
		question.setMaximumSize(new Dimension(600,200));
		question.setFont(new Font(FONT, Font.BOLD, 20));
	}

	/**
	 * configure panel's message field
	 */
	private void configanswer(){
		answer = new JTextArea(DEFAULT_ANSWER);
		answer.setEditable(false);
		answer.setLineWrap(true);
		answer.setAlignmentX(CENTER_ALIGNMENT);
		answer.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
				answer.getBorder()));
		answer.setPreferredSize(new Dimension(600,700));
		answer.setMaximumSize(new Dimension(600,700));
		answer.setFont(new Font(FONT, Font.PLAIN, 20));
	}
	
	/**
	 * configure panel's start button
	 */
	private static void configstartButton(){
		startButton = new JButton("Start"); 
		startButton.setFont(new Font(FONT, Font.PLAIN, 24));
		startButton.setAlignmentX(CENTER_ALIGNMENT);
	}

	/**
	 * configure panel's recording label
	 */
	private static void configrecordingLabel(){
		recordingLabel = new JLabel("Recording...", SwingConstants.CENTER);
		recordingLabel.setForeground(Color.RED);
		recordingLabel.setPreferredSize(new Dimension(20, 20));
		recordingLabel.setVisible(false);
		recordingLabel.setPreferredSize(new Dimension(200,40));
		recordingLabel.setAlignmentX(CENTER_ALIGNMENT);
	}
	
}