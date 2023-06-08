package mainframe;

import javax.swing.*;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import interfaces.*;
import listeners.*; 


@SuppressWarnings("serial")
public class LoginFrame extends JFrame implements MediatorObserver{

	LoginPanel lp;
	
	public LoginFrame() {
		setTitle("Login or Create Account");
		
		lp = new LoginPanel();
        add(lp, BorderLayout.CENTER);
        setSize(500,300);
        setVisible(true);

		
	}

	public LoginPanel getLoginPanel(){
		return lp;
	}

	public void closeLoginFrame(){
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

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

	@Override
	public void onLoginClosing() {
		closeLoginFrame();
	}

	@Override
	public void onEmailSetup() {
		// Not used
	}

	@Override
	public void onCancel() {
		// Not used
		
	}
	
	
}
