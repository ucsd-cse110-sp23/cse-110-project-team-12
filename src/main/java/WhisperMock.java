import java.io.File;
import java.io.IOException;

public class WhisperMock extends Whisper {
	static int n = 0; //counter

	public WhisperMock(String command, String prompt) throws IOException {
		super(command, prompt+n);
		n++;
	}

}
