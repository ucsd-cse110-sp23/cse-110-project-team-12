/**
 * @author CSE 110 - Team 12
 */
package interfaces;

/**
 * MediatorSubject methods
 *
 */
public interface MediatorSubject {
    void registerObserver(MediatorObserver parentFrame); 
    void notifyObservers();
}
