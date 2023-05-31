package interfaces;
import mediators.QPHPHButtonPanelPresenter;

public interface ButtonSubject {
        void registerObserver(QPHPHButtonPanelPresenter presenter); 
        void notifyObservers();
}
