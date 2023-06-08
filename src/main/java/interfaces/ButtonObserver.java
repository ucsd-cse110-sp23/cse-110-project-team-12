/**
 * @author CSE 110 - Team 12
 */
package interfaces;

/**
 * Interface ButtonObserver observes the buttons on AppFrame
 */
public interface ButtonObserver {
	
	//when Start/Stop button pressed for recording
    void onStartStop(boolean startedRecording);
    
    //when new Entry selected in PH
    void onListChange(String question);
    
    //when user exits app
    void onClose();
    
    //when user opens app
    void onStart();
}
