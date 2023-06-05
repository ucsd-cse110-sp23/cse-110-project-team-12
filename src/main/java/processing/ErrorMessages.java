package processing;

import javax.swing.JOptionPane;

import interfaces.ErrorMessagesInterface;

public class ErrorMessages implements ErrorMessagesInterface{

    @Override
    public void showErrorMessage(String s) {
       JOptionPane.showMessageDialog(null, s);
    }
    
}
