package mainframe;
import interfaces.LoginPanelSubject;
import mediators.LoginMediator;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel implements LoginPanelSubject {

    LoginMediator mediator;
    JButton loginButton, createAccountButton;
    JLabel email, pass1, pass2;
	JTextField emailField, passField1, passField2;
	int fieldLength = 20;
	boolean loggedIn = false;

    LoginPanel() {
		
        super(new GridLayout(4,1));
        
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
		loginButton = new JButton("Login");
		createAccountButton = new JButton("Create Account");
		
		this.add(email);
		this.add(emailField);
		this.add(pass1);
		this.add(passField1);
		this.add(pass2);
		this.add(passField2);
		this.add(loginButton);
		this.add(createAccountButton);        
               
	}

    public JButton getLoginButton(){
        return loginButton;
    }

    public JButton getcreateAccountButton(){
        return createAccountButton;
    }

    public String getEmail() {
		return emailField.getText();
	}

    public void setEmail(String s) {
		emailField.setText(s);
	}

    public String getPass1() {
		return passField1.getText();
	}
	
	public void setPass1(String s) {
		passField1.setText(s);
	}

    public String getPass2() {
		return passField2.getText();
	}
	
	public void setPass2(String s) {
		passField2.setText(s);
	}

	public boolean getLoggedInStatus(){
		return loggedIn;
	}


    @Override
    public void registerObserver(LoginMediator mediator) {
        this.mediator = mediator; 
    }

    @Override
    public void notifyObservers() {
        //
    }
    
}
