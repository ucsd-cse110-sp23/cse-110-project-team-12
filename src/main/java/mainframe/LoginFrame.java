package mainframe;

import javax.swing.*;

import api.MongoDB;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import interfaces.*;
import listeners.*; 
import mediators.LoginMediator;
import processing.ErrorMessages;


@SuppressWarnings("serial")
public class LoginFrame extends JFrame implements MediatorObserver{

	LoginMediator mediator;
	
	public LoginFrame(MediatorObserver appFrame) {
		setTitle("Login or Create Account");
		
		LoginPanel lp = new LoginPanel();

		mediator = new LoginMediator(addListeners(lp), lp, new MongoDB(), new ErrorMessages());
		mediator.registerObserver(this);
		mediator.registerObserver(appFrame);  
        add(lp, BorderLayout.CENTER);
        setSize(500,300);
        setVisible(true);
             
		
	}

	public void closeLoginFrame(){
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	private ArrayList<LoginButtonsSubject> addListeners(LoginPanel lp){
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


	
	
	
}
