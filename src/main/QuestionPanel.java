package main;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.sound.sampled.*;
import javax.swing.*;

/*  QuestionPanel
 * 	extends JPanel
 *  holds:
 *  	Title JLabel
 *  	Question JTextArea
 *  	Answer JTextArea
 *  	StartButton JButton
 */
class QuestionPanel extends JPanel{
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private static File question_audio;
    private JLabel title;
    private static JTextArea question, answer;
    private JLabel recordingLabel;
    private JButton startButton;
    private JButton deleteButton;
    
    private int num = 0;

    LayoutManager qpLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
    
    public static void setQuestion(String q) {
    	question.setText(q);
    }

    public static void setAnswer(String a) {
    	answer.setText(a);
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
    public String getQuestion() {
    	return question.getText();
    }
    
    /*
     * answer getter
     */
    public String getAnswer() {
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


        audioFormat = getAudioFormat();
        addListeners();
    }

    /*
     * add functionality to delete and start buttons
     */
    public void addListeners() {
    	
    	String URL = "http://localhost:8100/";
    	
    	/*
         * clicking START button
         * 
         * Starts and stops recording, changes labels accordingly
         * 
         * After recording stopped:
         *       add <question,answer> to server
         *       add "question" to prompt history list
         */
        startButton.addActionListener(
          new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (startButton.getText() == "New Question") {
                    startRecording();
                    startButton.setText("Stop Recording");
                } else {
                    stopRecording();
                    //add question to server
                    try {
	        			  URL url = new URL(URL);
	        			  HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        			  conn.setRequestMethod("POST");
	        			  conn.setDoOutput(true);
	        			  OutputStreamWriter out = new OutputStreamWriter(
	        				conn.getOutputStream()
	        			  );
	        			  System.out.println(question);
	        			  System.out.println(answer);
	        			  out.write(question.getText() + "," + answer.getText());
	        			  out.flush();
	        			  out.close();
	        			  BufferedReader in = new BufferedReader(
			                new InputStreamReader(conn.getInputStream())
			              );
			              
			              in.close();
			              
			            } catch (Exception ex) {
			              ex.printStackTrace();
			              
	        		    }
                    
                    //add question to prompt history list
                    PromptHistory.listPH.addElement(question.getText());
                    //to show old q's on start up
                    PromptHistory.saveQuestions();
                    startButton.setText("New Question");
                }
            }
          }
        );
        
        /*
         * clicking DELETE button
         * 
         * Deletes selected question from prompt history and server
         * Resets question and answer fields
         */
        deleteButton.addActionListener(
        		new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    	
                    	if (QuestionPanel.question.getText().equals("Your Question will appear here")) {
                    		//do nothing
                    	} else {
                    		
                    		//delete selected question and answer from server
                    		String URL = "http://localhost:8100/";
        					
	    					try {
	    			            String questionString = question.getText();
	    			            questionString = questionString.replace(' ', '+');
	    			            URL url = new URL(URL + "?=" + questionString);
	    			            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    			            conn.setRequestMethod("DELETE");
	    			            BufferedReader in = new BufferedReader(
	    			              new InputStreamReader(conn.getInputStream())
	    			            );
	    			           
	    			            in.close();
	    			           
	    			          } catch (Exception ex) {
	    			            ex.printStackTrace();
	    			            
	    			          }
        					
	    					//delete selected question and answer from prompt history
        					
	    					int questionIndex = PromptHistory.listPH.indexOf(question.getText());
	    					PromptHistory.listPH.remove(questionIndex);
        					PromptHistory.saveQuestions();
        					
        					question.setText("Your Question will appear here");
        					answer.setText("Your Answer will appear here");
                    		
                    	}
                    }
        			
        		}
        	
        	);
      }

      /*
       * format audio file for Whisper API    
       */
      private AudioFormat getAudioFormat() {
        // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;
    
        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;
    
        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;
    
        // whether the data is signed or unsigned.
        boolean signed = true;
    
        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;
    
        return new AudioFormat(
          sampleRate,
          sampleSizeInBits,
          channels,
          signed,
          bigEndian
        );
      }
      
      /*
       * method for recording voice, writing to audio file
       */
      private void startRecording() {
          Thread t = new Thread(
                  new Runnable() {
                      @override
                      public void run() {
                            try {
                              // the format of the TargetDataLine
                              DataLine.Info dataLineInfo = new DataLine.Info(
                                TargetDataLine.class,
                                audioFormat
                              );
                              // the TargetDataLine used to capture audio data from the microphone
                              targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                              targetDataLine.open(audioFormat);
                              targetDataLine.start();
                              recordingLabel.setVisible(true);
                        
                              // the AudioInputStream that will be used to write the audio data to a file
                              AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);
                        
                              // the file that will contain the audio data
                              File audioFile = new File("question_audio.wav");
                              question_audio = audioFile;
                              AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, audioFile);
                              recordingLabel.setVisible(false);
                            } catch (Exception ex) {
                              ex.printStackTrace();
                            }
                      }
                  }
                );
          t.start();	
      }
    
      /* stopRecording()
       * 
       * stops audio recorder
       * creates new Whisper obj with audio file argument to get question
       * creates new ChatGPT obj with question text to get answer
       * 
       */
      private void stopRecording() {
        targetDataLine.stop();
        
        try {
            new Whisper(question_audio);
        } catch (IOException e) {
            e.printStackTrace();
        }
        question.setText(Whisper.question_text);
        try {
            new ChatGPT(Whisper.question_text);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String answerString = ChatGPT.answer;
        answerString = answerString.substring(2);
        answer.setText(answerString);
        targetDataLine.close();
      }
    
}