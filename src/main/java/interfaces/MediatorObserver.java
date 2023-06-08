/**
 * @author CSE 110 - Team 12
 */
package interfaces;

/**
 * MediatorObserver methods
 *
 */
public interface MediatorObserver {
	//after successful login
    void onLoginClosing();
    
    //after clicking Save on email setup panel
    void onEmailSetup();
    
    //after clicking cancel on email setup panel
    void onCancel();
}
