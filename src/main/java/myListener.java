import java.awt.event.ActionListener;

public interface myListener extends ActionListener{
        void registerObserver(listenerObserver panel); 
        void notifyObservers();
}
