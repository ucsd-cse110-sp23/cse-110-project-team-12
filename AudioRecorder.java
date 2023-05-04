import java.awt.*;
//import org.json.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.sound.sampled.*;
import javax.swing.*;

//import org.json.JSONException;
//import org.json.JSONObject;

public class AudioRecorder extends JFrame {

  private JButton startButton;
  //private JButton stopButton;
  private AudioFormat audioFormat;
  private TargetDataLine targetDataLine;
  private JLabel recordingLabel;
  private static JTextField question;
  private static JTextField answer;
  private static File question_audio;

  public static void main(String[] args) {
    new AudioRecorder();
   /* new Whisper(question_audio);
    question.setText(Whisper.question_text);
    new ChatGPT(Whisper.question_text);
    answer.setText(ChatGPT.answer);*/
  }

  public AudioRecorder() {
    setTitle("Audio Recorder");
    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 0;

    startButton = new JButton("New Question");
    this.add(startButton, c);

  /*stopButton = new JButton("Stop");
    this.add(stopButton);*/

    recordingLabel = new JLabel("Recording...");
    recordingLabel.setForeground(Color.RED);
    recordingLabel.setPreferredSize(new Dimension(20, 20));
    recordingLabel.setVisible(false);
    
    c.gridy = 1;
    this.add(recordingLabel, c);

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(300, 100);
    setVisible(true);
    
    question = new JTextField("Question");
    question.setColumns(45);
    c.gridy = 2;
    this.add(question, c);
    
    answer = new JTextField("Answer");
    answer.setColumns(45);
    c.gridy = 3;
    this.add(answer, c);

    audioFormat = getAudioFormat();
    addListeners();
  }

  public void addListeners() {
    startButton.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        	if (startButton.getText() == "New Question") {
        		startRecording();
        		startButton.setText("Stop Recording");
        	} else {
        		stopRecording();
        		startButton.setText("New Question");
        	}
        }
      }
    );
    /*
    stopButton.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          stopRecording();
        }
      }
    );*/
  }

  private AudioFormat getAudioFormat() {
    // the number of samples of audio per second.
    // 44100 represents the typical sample rate for CD-quality audio.
    float sampleRate = 44100;

    // the number of bits in each sample of a sound that has been digitized.
    int sampleSizeInBits = 16;

    // the number of audio channels in this format (1 for mono, 2 for stereo).
    int channels = 2;

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
    answer.setText(ChatGPT.answer);
    targetDataLine.close();
  }


}
