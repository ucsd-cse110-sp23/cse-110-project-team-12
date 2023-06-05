package interfaces;

import java.io.File;
import java.io.IOException;

public interface WhisperInterface {
    public String getQuestionText();

    public void setWhisperFile(File audioFile) throws IOException;
}