import java.io.*;
import javax.sound.sampled.*;

public class Recorder {
   
    private static AudioFormat audioFormat = getAudioFormat();
    private static TargetDataLine targetDataLine;
    // the file that will contain the audio data
    private static final String AUDIOFILENAME = "question_audio.wav";
    private static File audioFile = new File(AUDIOFILENAME);
    

    public static void startRecording() {
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
                            QuestionPanel.setRecordingLableVisible();
                      
                            // the AudioInputStream that will be used to write the audio data to a file
                            AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);
                      
                            // the file that will contain the audio data
                            // question_audio = audioFile;
                            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, audioFile);
                            QuestionPanel.setRecordingLableInvisible();
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
      public static void stopRecording() {
        targetDataLine.stop();
        
        try {
            new Whisper(audioFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
            QuestionPanel.setQuestion(Whisper.question_text);
        try {
            new ChatGPT(Whisper.question_text);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String answerString = ChatGPT.answer;
        answerString = answerString.substring(2);
            QuestionPanel.setAnswer(answerString);
        targetDataLine.close();
      }

      private static AudioFormat getAudioFormat() {
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
}
