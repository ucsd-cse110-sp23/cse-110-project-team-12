package mainframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.IOException;

import api.MongoDB;
import interfaces.LoginUIObserver;
import listeners.*;

import server.MyServer; 


@SuppressWarnings("serial")
public class LoginScreen extends JFrame implements LoginUIObserver {
	JButton login;
	JButton createAccount;
	JPanel panel;
	JLabel email;
	JLabel pass1;
	JLabel pass2;
	JTextField emailField;
	JTextField passField1;
	JTextField passField2;
	int fieldLength = 20;
	boolean loggedIn = false;
	
	private DefaultListModel<String> entries = new DefaultListModel<>();
	
	public LoginScreen() {
		setTitle("Login or Create Account");
		//label for email
		email = new JLabel();
		email.setText("Email (Required)");
		
		//text field to get email
		emailField = new JTextField(fieldLength);
		
		//label for pass1
		pass1 = new JLabel();
		pass1.setText("Password (Required)");
		
		//text field to get password
		passField1 = new JTextField(fieldLength);
		
		//label for pass1
		pass2 = new JLabel();
		pass2.setText("Password (Create Account only)");
		
		//text field to get password
		passField2 = new JTextField(fieldLength);
		
		//create buttons
		login = new JButton("Login");
		createAccount = new JButton("Create Account");
		
		//panel for fields and buttons
		panel = new JPanel(new GridLayout(4,1));
		panel.add(email);
		panel.add(emailField);
		panel.add(pass1);
		panel.add(passField1);
		panel.add(pass2);
		panel.add(passField2);
		panel.add(login);
		panel.add(createAccount);
		addActionListeners(); 
		  
        add(panel, BorderLayout.CENTER);
        setSize(500,300);
        setVisible(true);
        
        this.addWindowListener(new java.awt.event.WindowAdapter() {
        	@Override
        	public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        		if (!loggedIn) {
        			return;
        		} else { 
	        		try {
						new AppFrame(emailField.getText(), entries);
					} catch (IOException e) {
						e.printStackTrace();
					}
	        		MyServer.checkServerAvailability();
        		}
        	
        	}
        });
               
	}

	private void addActionListeners(){
		final LoginListener loginListener = new LoginListener();
		loginListener.registerObserver(this);
		final CreateAccountListener createListener = new CreateAccountListener();
		createListener.registerObserver(this);
		login.addActionListener(loginListener);
		createAccount.addActionListener(createListener);
		
	}
	
	public String getEmail() {
		return emailField.getText();
	}
	
	public String getPassword() {
		return passField1.getText();
	}
	
	public String getPass2() {
		return passField2.getText();
	}

	public boolean loggedInStatus(){
		return loggedIn;
	}

	@Override
	public void onCreateAccount() throws Exception {
		final String sEmail = emailField.getText();
		final String sPass1 = passField1.getText();
		final String sPass2 = passField2.getText();

		final MongoDB mongo = new MongoDB();
		mongo.createAccount(sEmail, sPass1, sPass2); 
		
		loggedIn = true;
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		
        
	}

	@Override
	public void onLogin() throws Exception {
		final String sEmail = emailField.getText();
		final String sPass1 = passField1.getText();

		final MongoDB mongo = new MongoDB();
		
		if (!mongo.login(sEmail, sPass1)) {
			return;
		}
		entries = mongo.getPrompts(sEmail);
		mongo.mongoToServer(sEmail);
		loggedIn = true;
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	
}
