package interfaces;

public interface LoginButtonsSubject {
    void registerObserver(LoginButtonsObserver observer);
    void notifyObservers() throws Exception;
}
