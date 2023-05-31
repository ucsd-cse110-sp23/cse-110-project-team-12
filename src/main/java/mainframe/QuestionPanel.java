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


 
 
 public class QuestionPanel extends JPanel implements PanelSubject{
 
     private JLabel title;
     private static JTextArea question, answer;
     private static JLabel recordingLabel;
     private static JButton startButton;
     private static JButton deleteButton;
     private static final String FONT = "Sans-serif";
     private ButtonPanelPresenter presenter;
 
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
         configdeleteButton();
         configstartButton();
         configrecordingLabel();
 
         this.add(Box.createVerticalGlue());
         this.add(title);
         this.add(Box.createVerticalGlue());
         this.add(question);
         this.add(answer);
         this.add(Box.createRigidArea(new Dimension(100,40)));
         this.add(recordingLabel);
         this.add(deleteButton);
         deleteButton.setVisible(false);
         this.add(startButton);
         this.add(Box.createRigidArea(new Dimension(100,20)));
     }

     //StartButton getter setter

     public JButton getStartButton(){
        return startButton;      
    }

     public void setStartButtonText(String s){
        startButton.setText(s);
   }

   //RecordingLabel getter setter

   public void setRecordingLableVisible(){
        recordingLabel.setVisible(true);
    }

    public void setRecordingLableInvisible(){
      recordingLabel.setVisible(false);
  }

  //DeleteButton getter setter

     public void setDeleteButtonVisible(){
        deleteButton.setVisible(true);
    }

    public void setDeleteButtonInvisible(){
        deleteButton.setVisible(false);
    }

    public JButton getDeleteButton(){
        return deleteButton;
    }

    //Q & A getter setter

    public void setQuestion(String q) {
        question.setText(q);
    }
    
    public void setAnswer(String a) {
        answer.setText(a);
    }

     public String getQuestion() {
        return question.getText();
    }

    public static String getAnswer() {
        return answer.getText();
    }

    //ON NOTIFY() METHODS 

    @Override
    public void registerObserver(ButtonPanelPresenter presenter) {
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
    
    private static void configquestion(){
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

    private static void configanswer(){
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
    
    private static void configdeleteButton() {
        deleteButton = new JButton("Delete");
        deleteButton.setFont(new Font(FONT, Font.PLAIN, 24));
        deleteButton.setAlignmentX(CENTER_ALIGNMENT);
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