/**
 * @author CSE 110 - Team 12
 */
package interfaces;

import mediators.LoginMediator;

/**
 * LoginPanelSubject methods
 *
 */
public interface LoginPanelSubject {
    void registerObserver(LoginMediator mediator); 
    void notifyObservers();
}
