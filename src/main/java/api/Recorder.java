package api;


import java.io.*;
import java.util.ArrayList;
import javax.sound.sampled.*;

import interfaces.ChatGPTInterface;
import interfaces.WhisperInterface;

public class Recorder {
   
    private  AudioFormat audioFormat = getAudioFormat();
    private TargetDataLine targetDataLine;
    // the file that will contain the audio data
    private final String AUDIOFILENAME = "question_audio.wav";
    private File audioFile = new File(AUDIOFILENAME);
    private ChatGPTInterface ChatGPTSession = null;
    private WhisperInterface WhisperSession = null;

    private int temp_count = 0;

    public Recorder(ChatGPTInterface ChatGPTSession, WhisperInterface WhisperSession){
      this.ChatGPTSession = ChatGPTSession;
      this.WhisperSession = WhisperSession;
  }
    

    public void startRecording() {
        Thread t = new Thread(
                new Runnable() {
                    @override
                    public void run() {
                          try {
                            
                            // the TargetDataLine used to capture audio data from the microphone
                            DataLine.Info dataLineInfo = new DataLine.Info(
                              TargetDataLine.class,
                              audioFormat
                            );
                            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                            targetDataLine.open(audioFormat);
                            targetDataLine.start();
                      
                            // the AudioInputStream that will be used to write the audio data to a file
                            AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);
                      
                            // the file that will contain the audio data
                            // question_audio = audioFile;
                            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, audioFile);

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
      public ArrayList<String> stopRecording() {
        ArrayList<String> qanda = new ArrayList<String>();
        targetDataLine.stop();
        
        try {
          WhisperSession = new Whisper(audioFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
            qanda.add(this.WhisperSession.getQuestionText());
        try {
          ChatGPTSession = new ChatGPT(this.WhisperSession.getQuestionText());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String answerString = this.ChatGPTSession.getAnswer();
        answerString = answerString.substring(2);
        qanda.add(answerString);
        targetDataLine.close();
        
        //todo
        qanda.add("question" + temp_count);
        qanda.add("answer" + temp_count);
        temp_count ++;

        return qanda;
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

      private @interface override {

      }
}
