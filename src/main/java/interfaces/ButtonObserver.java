package interfaces;

public interface ButtonObserver {
    void onStartStop(boolean startedRecording);
    // void onDelete();
    void onListChange(String question);
    // void onClear();
    void onClose();
    void onStart();
}
