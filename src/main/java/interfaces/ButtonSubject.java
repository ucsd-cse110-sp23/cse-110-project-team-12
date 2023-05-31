package interfaces;
import mainframe.ButtonPanelPresenter;

public interface ButtonSubject {
        void registerObserver(ButtonPanelPresenter presenter); 
        void notifyObservers();
}
