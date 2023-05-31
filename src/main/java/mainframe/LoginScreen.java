package mainframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

import api.MongoDB;
import interfaces.LoginUIObserver;
import listeners.*; 


@SuppressWarnings("serial")
public class LoginScreen extends JFrame implements LoginUIObserver{
	JButton login, createAccount;
	JPanel panel;
	JLabel email, pass1, pass2;
	JTextField emailField, passField1, passField2;
	int fieldLength = 20;
	boolean loggedIn = false;
	
	public LoginScreen() {
		setTitle("Login or Create Account");
		//label for email
		email = new JLabel();
		email.setText("Email");
		
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
               
	}

	private void addActionListeners(){
		CreateAccountListener createListener = new CreateAccountListener();
		createListener.registerObserver(this);
		if (!GraphicsEnvironment.isHeadless()) {
			createAccount.addActionListener(createListener);
		}
	}
	
	protected void setEmail(String s) {
		emailField.setText(s);
	}
	
	protected void setPass1(String s) {
		passField1.setText(s);
	}
	
	protected void setPass2(String s) {
		passField2.setText(s);
	}

	public boolean loggedInStatus(){
		return loggedIn;
	}

	@Override
	public void onLogin() {
		String sEmail = this.emailField.getText();
		String sPass1 = this.passField1.getText();
		String sPass2 = this.passField2.getText();
		
		if (sPass1.equals(sPass2) && !MongoDB.checkEmail(sEmail, "Users")) {
			MongoDB.createAccount(sEmail, sPass1, "Users"); 	
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			app.succesfullLogin();
            
    		
		} else {
			JOptionPane.showMessageDialog(null, "Error: Email or Password Invalid!");
		}
	}
}
