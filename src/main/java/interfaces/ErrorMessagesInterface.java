package interfaces;

import javax.swing.JFrame;

public interface ErrorMessagesInterface {
    public void showErrorMessage(String s);
    public boolean confirmClosing();
    public boolean confirmAutoLogin();
    public boolean checkPassword(String pass);
}
