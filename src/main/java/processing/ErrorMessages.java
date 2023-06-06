package processing;

import javax.swing.JOptionPane;

import interfaces.ErrorMessagesInterface;

public class ErrorMessages implements ErrorMessagesInterface{

    @Override
    public void showErrorMessage(String s) {
       JOptionPane.showMessageDialog(null, s);
    }

    @Override
    public boolean confirmClosing() {
        if (JOptionPane.showConfirmDialog(null,"Are you sure you want to close this window?", 
        "Close Window?", 
        JOptionPane.YES_NO_OPTION, 
        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
            return true;
        }
        return false;
    }
    
}
