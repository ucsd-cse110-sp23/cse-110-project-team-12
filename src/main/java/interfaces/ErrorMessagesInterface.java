/**
 * @author CSE 110 - Team 12
 */
package interfaces;

/**
 * ErrorMessage methods
 */
public interface ErrorMessagesInterface {
	
	//displays input as error message
    public void showErrorMessage(String s);
    
    //confirms user wants to exit app
    public boolean confirmClosing();
    
    //asks user if they want auto login
    public boolean confirmAutoLogin();
    
    //password validation for account creation
    public boolean checkPassword(String pass);
}
