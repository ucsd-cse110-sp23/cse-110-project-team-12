/*  QuestionPanel is a subpanel that holds our q&a field. It has buttons to allow user to record questions.
 * 	extends JPanel
 *  holds:
 *  	Title JLabel
 *  	Question JTextArea
 *  	Answer JTextArea
 *  	StartButton JButton
 */

 import java.awt.*;
 import javax.swing.*;
 
 
 class QuestionPanel extends JPanel implements listenerObserver{
 
     private JLabel title;
     private static JTextArea question, answer;
     private static JLabel recordingLabel;
     private static JButton startButton;
     private final static String defaultQuestion = "Your query will appear here.";
     private final static String defaultAnswer = "Your result will appear here.";
     private String username;
 
     LayoutManager qpLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);    
 
     /*
      * QuestionPanel constructor
      */
     public QuestionPanel(String username){
    	 this.username = username;
       
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
         //this.add(deleteButton);
         this.add(startButton);
         this.add(Box.createRigidArea(new Dimension(100,20)));
 
         startButton.addActionListener(new StartStopListener(this));
     }
     
     public void onListenerChange(){
    	 // some sort of server check?? to update. Maybe yes for listhandler
	 }
     
     public static void reset() {
    	 question.setText(defaultQuestion);
    	 answer.setText(defaultAnswer);
     }
     
     public void setButtonText(String s){
    	 startButton.setText(s);
     }
   
     public static void setQuestion(String q) {
    	 question.setText(q);
     }

     public static void setAnswer(String a) {
         answer.setText(a);
     }
     
     public String getUser() {
         return username;
     }

     public static String getQuestion() {
         return question.getText();
     }

     public static String getAnswer() {
         return answer.getText();
     }
     
     public static String getButtonText(){
         return startButton.getText();
     }

     public static void setRecordingLableVisible(){
         recordingLabel.setVisible(true);
     }

     public static void setRecordingLableInvisible(){
         recordingLabel.setVisible(false);
     }
     
     private void configTitle(){
         title = new JLabel("SayIt Assistant", SwingConstants.CENTER);
         title.setFont(new Font("Sans-serif", Font.BOLD, 30));
         title.setAlignmentX(CENTER_ALIGNMENT);
         
     }
     
     private void configquestion(){
         QuestionPanel.question = new JTextArea(defaultQuestion);
         question.setEditable(false);
         question.setLineWrap(true);
         question.setAlignmentX(CENTER_ALIGNMENT);
         question.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
         question.getBorder()));
         question.setPreferredSize(new Dimension(600,200));
         question.setMaximumSize(new Dimension(600,200));
         question.setFont(new Font("Sans-serif", Font.BOLD, 20));
     }
 
     private void configanswer(){
         QuestionPanel.answer = new JTextArea(defaultAnswer);
         answer.setEditable(false);
         answer.setLineWrap(true);
         answer.setAlignmentX(CENTER_ALIGNMENT);
         answer.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
         answer.getBorder()));
         answer.setPreferredSize(new Dimension(600,700));
         answer.setMaximumSize(new Dimension(600,700));
         answer.setFont(new Font("Sans-serif", Font.PLAIN, 20));
     }
 
     private void configstartButton(){
         startButton = new JButton("Start"); 
         startButton.setFont(new Font("Sans-serif", Font.PLAIN, 24));
         startButton.setAlignmentX(CENTER_ALIGNMENT);
     }
 
     private void configrecordingLabel(){
         recordingLabel = new JLabel("Recording...", SwingConstants.CENTER);
         recordingLabel.setForeground(Color.RED);
         recordingLabel.setPreferredSize(new Dimension(20, 20));
         recordingLabel.setVisible(false);
         recordingLabel.setPreferredSize(new Dimension(200,40));
         recordingLabel.setAlignmentX(CENTER_ALIGNMENT);
     }
 }