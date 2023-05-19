import java.awt.event.ActionListener;

public interface myListener extends ActionListener{
        public static final String  URL = "http://localhost:8100/";
        public static final Recorder RECORDER = new Recorder(FormatAudio.getAudioFormat());
}
