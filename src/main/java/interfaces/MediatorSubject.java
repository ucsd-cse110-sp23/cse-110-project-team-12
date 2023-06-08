package interfaces;

import javax.swing.JFrame;

public interface MediatorSubject {
    void registerObserver(MediatorObserver parentFrame); 
    void notifyObservers();
}
