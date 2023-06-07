package mainframe;
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

import interfaces.PanelSubject;
import mediators.QPHPHButtonPanelPresenter;
import processing.*;


 
 
 public class QuestionPanel extends JPanel implements PanelSubject{
 
     private JLabel title;
     private JTextArea question, answer;
     private static JLabel recordingLabel;
     private static JButton startButton;
     private static final String FONT = "Sans-serif";
     private QPHPHButtonPanelPresenter presenter;
 
     LayoutManager qpLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);    
 
     /*
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

     public void startedRecording(){
        setRecordingLableVisible();
        setStartButtonText("Stop Recording");
     }

     public void stoppedRecording(){
        setRecordingLableInvisible();
        setStartButtonText("Start Recording");
     }

     public void onNewEntry(Entry entry){
        if (entry == null){
            setQuestion("Invalid Input");
        }
        setQuestion(entry.getTitle());
        setAnswer(entry.getResult());

     }

     public void InvalidInputDetected(String question){
        setQuestion(question);
        setAnswer("Invalid Input Detected");
     }

     public void onListChange(String question, String answer){
        setQuestion(question);
        setAnswer(answer);
     }

     //StartButton getter setter

     public JButton getStartButton(){
        return startButton;      
    }

    private void setStartButtonText(String s){
        startButton.setText(s);
   }

   //RecordingLabel getter setter

   private void setRecordingLableVisible(){
        recordingLabel.setVisible(true);
    }

    private void setRecordingLableInvisible(){
      recordingLabel.setVisible(false);
  }
    //Q & A getter setter

    private void setQuestion(String q) {
        question.setText(q);
    }
    
    private void setAnswer(String a) {
        answer.setText(a);
    }

     public String getQuestion() {
        return question.getText();
    }

    public String getAnswer() {
        return answer.getText();
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
    



   
  
 
 

 

 


     
private void configTitle(){
        title = new JLabel("SayIt Assistant", SwingConstants.CENTER);
        title.setFont(new Font(FONT, Font.BOLD, 30));
        title.setAlignmentX(CENTER_ALIGNMENT);
        
}
    
    private void configquestion(){
        question = new JTextArea("Your Question will appear here");
        question.setEditable(false);
        question.setLineWrap(true);
        question.setAlignmentX(CENTER_ALIGNMENT);
        question.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
        question.getBorder()));
        question.setPreferredSize(new Dimension(600,200));
        question.setMaximumSize(new Dimension(600,200));
        question.setFont(new Font(FONT, Font.BOLD, 20));
    }

    private void configanswer(){
        answer = new JTextArea("Your Answer will appear here");
        answer.setEditable(false);
        answer.setLineWrap(true);
        answer.setAlignmentX(CENTER_ALIGNMENT);
        answer.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
        answer.getBorder()));
        answer.setPreferredSize(new Dimension(600,700));
        answer.setMaximumSize(new Dimension(600,700));
        answer.setFont(new Font(FONT, Font.PLAIN, 20));
    }
    private static void configstartButton(){
        startButton = new JButton("New Question"); 
        startButton.setFont(new Font(FONT, Font.PLAIN, 24));
        startButton.setAlignmentX(CENTER_ALIGNMENT);
    }

    private static void configrecordingLabel(){
        recordingLabel = new JLabel("Recording...", SwingConstants.CENTER);
        recordingLabel.setForeground(Color.RED);
        recordingLabel.setPreferredSize(new Dimension(20, 20));
        recordingLabel.setVisible(false);
        recordingLabel.setPreferredSize(new Dimension(200,40));
        recordingLabel.setAlignmentX(CENTER_ALIGNMENT);
    }


    
 }