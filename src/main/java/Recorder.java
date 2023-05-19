import java.io.*;
import javax.sound.sampled.*;

public class Recorder {

    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    // the file that will contain the audio data
    private static File audioFile;
    private static final String AUDIOFILENAME = "question_audio.wav";

    Recorder(AudioFormat af) {
        audioFormat = af;
        audioFile = new File(AUDIOFILENAME);
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
      public void stopRecording() {
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
}
