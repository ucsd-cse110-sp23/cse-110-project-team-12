/**
 * @author CSE 110 - Team 12
 */
package interfaces;

import java.io.File;
import java.io.IOException;

/**
 * Whisper methods
 *
 */
public interface WhisperInterface {
    public String getQuestionText();
    public void setWhisperFile(File audioFile) throws IOException;
}
