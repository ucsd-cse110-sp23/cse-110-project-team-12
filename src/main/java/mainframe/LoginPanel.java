/**
 * @author CSE 110 - Team 12
 */
package mainframe;
import interfaces.LoginPanelSubject;
import mediators.LoginMediator;

import javax.swing.*;
import java.awt.*;

/**
 * LoginPanel allows the user to input an email and password to access their account
 * 		Can login or create new account
 *
 */
public class LoginPanel extends JPanel implements LoginPanelSubject {

	LoginMediator mediator;
	JButton loginButton, createAccountButton;
	JLabel email, pass1;
	JTextField emailField, passField1;
	int fieldLength = 20;
	boolean loggedIn = false;

	/**
	 * LoginPanel constructor
	 */
	LoginPanel() {

		super(new GridLayout(4,1));

		Font labelFont = new Font("Sans-serif", Font.BOLD, 25);
		Font fieldFont = new Font("Sans-serif", Font.PLAIN, 25);

		//label for email
		email = new JLabel();
		email.setText("Email (Required)");
		email.setFont(labelFont);

		//text field to get email
		emailField = new JTextField(fieldLength);
		emailField.setFont(fieldFont);

		//label for pass1
		pass1 = new JLabel();
		pass1.setText("Password (Required)");
		pass1.setFont(labelFont);

		//text field to get password
		passField1 = new JTextField(fieldLength);
		passField1.setFont(fieldFont);

		//create buttons
		loginButton = new JButton("Login");
		loginButton.setFont(labelFont);
		createAccountButton = new JButton("Create Account");
		createAccountButton.setFont(labelFont);

		this.add(email);
		this.add(emailField);
		this.add(pass1);
		this.add(passField1);
		this.add(loginButton);
		this.add(createAccountButton);        

	}

	/**
	 * Gets login button
	 * 
	 * @return JButton login button
	 */
	public JButton getLoginButton(){
		return loginButton;
	}

	/**
	 * Gets create account button
	 * 
	 * @return JButton create account button
	 */
	public JButton getcreateAccountButton(){
		return createAccountButton;
	}

	/**
	 * Gets email field
	 * 
	 * @return String email
	 */
	public String getEmail() {
		return emailField.getText();
	}

	/**
	 * Gets password field
	 * 
	 * @return String password
	 */
	public String getPass1() {
		return passField1.getText();
	}

	/**
	 * Gets user's logged in status
	 * @return true if user logged in, false otherwise
	 */
	public boolean getLoggedInStatus(){
		return loggedIn;
	}

	/**
	 * Registers QPPH mediator as observer
	 */
	@Override
	public void registerObserver(LoginMediator mediator) {
		this.mediator = mediator; 
	}

	/**
	 * Does nothing
	 */
	@Override
	public void notifyObservers() {}

}
