package interfaces;
import mediators.*;

public interface PanelSubject {
    void registerObserver(QPHPHButtonPanelPresenter presenter); 
    void notifyObservers();
}
