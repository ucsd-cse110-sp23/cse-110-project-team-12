package listeners.interfaces;

public interface ButtonObserver {
    void onStartStop(boolean startedRecording);
    void onDelete();
    void onListChange(String question, String answer);
    void onClear();
}
