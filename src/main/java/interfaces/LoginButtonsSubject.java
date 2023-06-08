/**
 * @author CSE 110 - Team 12
 */
package interfaces;

/**
 * LoginButtonSubject methods
 *
 */
public interface LoginButtonsSubject {
    void registerObserver(LoginButtonsObserver observer);
    void notifyObservers() throws Exception;
}
