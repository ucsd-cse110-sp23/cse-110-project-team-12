import javax.sound.sampled.*;

public class FormatAudio {
    /*
       * format audio file for Whisper API    
       */
      public static AudioFormat getAudioFormat() {
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
