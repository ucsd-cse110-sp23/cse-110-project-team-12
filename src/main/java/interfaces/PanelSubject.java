package interfaces;
import mediators.QPHPHButtonPanelPresenter;

public interface PanelSubject {
    void registerObserver(QPHPHButtonPanelPresenter presenter); 
    void notifyObservers();
}
