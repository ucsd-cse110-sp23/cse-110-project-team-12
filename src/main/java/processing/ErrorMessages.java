/**
 * @author CSE 110 - Team 12
 */
package processing;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import interfaces.ErrorMessagesInterface;

/**
 * Handles JOptionPanes
 * 
 * Implements ErrorMessagesInterface
 * 
 */
public class ErrorMessages implements ErrorMessagesInterface{

	/**
	 * Show JOptionPane with error message
	 * 
	 * @param String - error message to be shown
	 */
    @Override
    public void showErrorMessage(String s) {
       JOptionPane.showMessageDialog(null, s);
    }

    /**
     * Show JoptionPane to confirm user wants to close app
     * 
     * @return boolean - true if user agrees, false otherwise
     */
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

    /**
     * Show JOptionPane to ask user if they want auto login on their device
     * 
     * @param boolean - true if they agree, false otherwise
     */
    @Override
    public boolean confirmAutoLogin() {
        if (JOptionPane.showConfirmDialog(null,"Do you want to save your log-in details for auto-login?", 
        "Auto Login", 
        JOptionPane.YES_NO_OPTION, 
        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
            return true;
        }
        return false;
    }

    /**
     * Show JOptionPane to ask user to confirm their password when they create account
     * 
     * @param - true if password matches, false otherwise
     */
	@Override
	public boolean checkPassword(String pass1) {
		UIManager.put("OptionPane.minimumSize",new Dimension(400,400)); 
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font(  
		          "Sans-serif", Font.BOLD, 18)));
		String pass2 = JOptionPane.showInputDialog("Please confirm your password");
		
		if (pass2 != null && pass2.equals(pass1)) {
			return true;
		} else {
			return false;
		}
		
	}
    
}
