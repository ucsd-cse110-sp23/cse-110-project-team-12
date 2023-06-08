/**
 * @author CSE 110 - Team 12
 */
package mainframe;

import javax.swing.*;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import interfaces.*;
import listeners.*; 

/**
 * Frame for LoginPanel
 * Opens on app startup if auto login not configured
 *
 */
@SuppressWarnings("serial")
public class LoginFrame extends JFrame implements MediatorObserver{

	LoginPanel lp;

	/**
	 * LoginFrame constructor
	 */
	public LoginFrame() {
		setTitle("Login or Create Account");

		lp = new LoginPanel();
		add(lp, BorderLayout.CENTER);
		setSize(500,300);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setVisible(true);


	}

	/**
	 * Gets frame's corresponding LoginPanel
	 * 
	 * @return LoginPanel
	 */
	public LoginPanel getLoginPanel(){
		return lp;
	}

	/**
	 * Closes login frame window
	 */
	public void closeLoginFrame(){
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	/**
	 * Adds listeners to Create Account and Login buttons
	 * 
	 * @param lp - frames corresponding LoginPanel
	 * @return list of LoginButtonsSubject
	 */
	public ArrayList<LoginButtonsSubject> addListeners(LoginPanel lp){
		ArrayList<LoginButtonsSubject> allLoginButtons = new ArrayList<LoginButtonsSubject>();
		JButton loginButton = lp.getLoginButton();
		JButton createAccountButton = lp.getcreateAccountButton();
		LoginListener loginListener = new LoginListener();
		CreateAccountListener createListener = new CreateAccountListener();
		loginButton.addActionListener(loginListener);
		createAccountButton.addActionListener(createListener);

		allLoginButtons.add(loginListener);
		allLoginButtons.add(createListener);
		return allLoginButtons;
	}

	/**
	 * Closes frame upon successful login
	 */
	@Override
	public void onLoginClosing() {
		closeLoginFrame();
	}

	/**
	 * Does nothing
	 */
	@Override
	public void onEmailSetup() {}

	/**
	 * Does nothing
	 */
	@Override
	public void onCancel() {}

}
