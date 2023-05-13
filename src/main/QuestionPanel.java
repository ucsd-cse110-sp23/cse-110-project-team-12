package main;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.sound.sampled.*;
import javax.swing.*;

class QuestionPanel extends JPanel{
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private static File question_audio;
    private JLabel title;
    private JTextArea question, answer;
    private JLabel recordingLabel;
    private JButton startButton;

    //String ph = "Placeholder";

    LayoutManager qpLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);

    private void configTitle(){
        title = new JLabel("SayIt Assistant", SwingConstants.CENTER);
        title.setFont(new Font("Sans-serif", Font.BOLD, 30));
        title.setAlignmentX(CENTER_ALIGNMENT);
        
    }
    private void configquestion(){
        this.question = new JTextArea("Your Question will appear here");
        question.setEditable(false);
        question.setLineWrap(true);
        question.setAlignmentX(CENTER_ALIGNMENT);
        question.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
        question.getBorder()));
        question.setPreferredSize(new Dimension(600,200));
        question.setMaximumSize(new Dimension(600,200));
        question.setFont(new Font("Sans-serif", Font.BOLD, 18));
    }

    private void configanswer(){
        this.answer = new JTextArea("Your Answer will appear here");
        answer.setEditable(false);
        answer.setLineWrap(true);
        answer.setAlignmentX(CENTER_ALIGNMENT);
        answer.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
        answer.getBorder()));
        answer.setPreferredSize(new Dimension(600,700));
        answer.setMaximumSize(new Dimension(600,700));
        answer.setFont(new Font("Sans-serif", Font.PLAIN, 14));
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
    
    public String getQuestion() {
    	return question.getText();
    }
    
    public String getAnswer() {
    	return answer.getText();
    }
    
    
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


        audioFormat = getAudioFormat();
        addListeners();
    }
    
    //to POST q/a to server
    public JButton getPostButton() {
    	return startButton;
    	
    }

    public void addListeners() {
    	String URL = "http://localhost:8100/";
        startButton.addActionListener(
          new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (startButton.getText() == "New Question") {
                    startRecording();
                    startButton.setText("Stop Recording");
                } else {
                    stopRecording();
                    try {
	        			  URL url = new URL(URL);
	        			  HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        			  conn.setRequestMethod("POST");
	        			  conn.setDoOutput(true);
	        			  OutputStreamWriter out = new OutputStreamWriter(
	        				conn.getOutputStream()
	        			  );
	        			  out.write(question.getText() + "," + answer.getText());
	        			  out.flush();
	        			  out.close();
	        			  BufferedReader in = new BufferedReader(
			                new InputStreamReader(conn.getInputStream())
			              );
			              String response = in.readLine();
			              in.close();
			              JOptionPane.showMessageDialog(null, response);
			            } catch (Exception ex) {
			              ex.printStackTrace();
			              JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
	        		    }
                    PromptHistory.listPH.addElement(question.getText());
                    PromptHistory.loadPrevQuestions();
                    startButton.setText("New Question");
                }
            }
          }
        );
      }

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
    
      private void stopRecording() {
        targetDataLine.stop();
        /*
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
        answer.setText(ChatGPT.answer);*/
        question.setText("question asked");
        answer.setText("answer retrieved");
        targetDataLine.close();
      }
    
}