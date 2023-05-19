/*  QuestionPanel is a subpanel that holds our q&a field. It has buttons to allow user to record questions.
 * 	extends JPanel
 *  holds:
 *  	Title JLabel
 *  	Question JTextArea
 *  	Answer JTextArea
 *  	StartButton JButton
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.sound.sampled.*;
import javax.swing.*;


class QuestionPanel extends JPanel{
    private Recorder recorder;
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private static File question_audio;
    public JLabel title;
    public static JTextArea question, answer;
    public static JLabel recordingLabel;
    public static JButton startButton;
    public static JButton deleteButton;

    LayoutManager qpLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
    
    public static void setQuestion(String q) {
    	question.setText(q);
    }

    public static void setAnswer(String a) {
    	answer.setText(a);
    }

    public static void setRecordingLableVisible(){
        recordingLabel.setVisible(true);
    }

    public static void setRecordingLableInvisible(){
      recordingLabel.setVisible(true);
  }

  public static String getButtonText(){
    return startButton.getText();
}

  public static void setButtonText(String s){
    startButton.setText(s);
  }
    
    private void configTitle(){
        title = new JLabel("SayIt Assistant", SwingConstants.CENTER);
        title.setFont(new Font("Sans-serif", Font.BOLD, 30));
        title.setAlignmentX(CENTER_ALIGNMENT);
        
    }
    
    private void configquestion(){
        QuestionPanel.question = new JTextArea("Your Question will appear here");
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
        QuestionPanel.answer = new JTextArea("Your Answer will appear here");
        answer.setEditable(false);
        answer.setLineWrap(true);
        answer.setAlignmentX(CENTER_ALIGNMENT);
        answer.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
        answer.getBorder()));
        answer.setPreferredSize(new Dimension(600,700));
        answer.setMaximumSize(new Dimension(600,700));
        answer.setFont(new Font("Sans-serif", Font.PLAIN, 20));
    }
    
    private void configdeleteButton() {
    	deleteButton = new JButton("Delete");
    	deleteButton.setFont(new Font("Sans-serif", Font.PLAIN, 24));
    	deleteButton.setAlignmentX(CENTER_ALIGNMENT);
    }

    private void configstartButton(){
        startButton = new JButton("New Question"); 
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
    
    
    /*
     * question getter
     */
    public static String getQuestion() {
    	return question.getText();
    }
    
    /*
     * answer getter
     */
    public static String getAnswer() {
    	return answer.getText();
    }
    
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
        this.add(startButton);
        this.add(Box.createRigidArea(new Dimension(100,20)));


        audioFormat = FormatAudio.getAudioFormat();
        recorder = new Recorder(audioFormat);
        addListeners();
    }

    /*
     * add functionality to delete and start buttons
     */
    public void addListeners() {
        startButton.addActionListener(new startListener());
        deleteButton.addActionListener(new stopListener());
      }
    
}