package interfaces;
import mediators.LoginMediator;

public interface LoginPanelSubject {
    void registerObserver(LoginMediator mediator); 
    void notifyObservers();
}
