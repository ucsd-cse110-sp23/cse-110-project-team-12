

import java.io.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WhisperTest {
    /*
    /*  unit test to check if Whisper constructor returns correct transcription given a mock audio file
    *   that says "1 plus 1"
    
    @Test
    void testWhisper() {
        // audio that says "1 plus 1"
        File mockAudio = new File("mock_audio.m4a");
        try {
            new Whisper(mockAudio);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(Whisper.question_text, "1 plus 1");
    }*/
	
	@Test
	void sampleTest() {
		assertEquals(3,3);
	}

    @Test
    void simpleTest() {
        
        assertEquals(2, 5-3);
    }
    
}