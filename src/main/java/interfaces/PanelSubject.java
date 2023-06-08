/**
 * @author CSE 110 - Team 12
 */
package interfaces;

import mediators.QPHPHButtonPanelPresenter;

/**
 * PanelSubject methods
 *
 */
public interface PanelSubject {
    void registerObserver(QPHPHButtonPanelPresenter presenter); 
    void notifyObservers();
}
