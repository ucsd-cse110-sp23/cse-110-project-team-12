package interfaces;

public interface LoginUISubject {
    void registerObserver(LoginUIObserver observer);
    void notifyObservers() throws Exception;
}
