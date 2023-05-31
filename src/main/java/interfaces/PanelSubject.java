package interfaces;
import mainframe.ButtonPanelPresenter;

public interface PanelSubject {
    void registerObserver(ButtonPanelPresenter presenter); 
    void notifyObservers();
}
