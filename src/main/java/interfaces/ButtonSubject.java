/**
 * @author CSE 110 - Team 12
 */
package interfaces;
import mediators.QPHPHButtonPanelPresenter;

/**
 * ButtonSubject methods
 * 
 * registerObserver
 * notifyObservers
 */
public interface ButtonSubject {
        void registerObserver(QPHPHButtonPanelPresenter presenter); 
        void notifyObservers();
}
