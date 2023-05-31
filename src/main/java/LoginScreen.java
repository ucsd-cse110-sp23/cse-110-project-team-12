import javax.swing.*;  
import java.awt.*;  
import java.awt.event.*;  
import java.lang.Exception; 

@SuppressWarnings("serial")
public class LoginScreen extends JFrame {
	JButton login, createAccount;
	JPanel panel;
	JLabel email, pass1, pass2;
	JTextField emailField, passField1, passField2;
	int fieldLength = 20;
	boolean loggedIn;
	
	public LoginScreen() {
		if (!GraphicsEnvironment.isHeadless()) {
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
			  
	        add(panel, BorderLayout.CENTER);
	        setSize(500,300);
	        setVisible(true);
        
	        //login.addActionListener(new LoginListener());
        
        	createAccount.addActionListener(new CreateAccountListener(this));
        } 
        /*
        else {
        	createAccount.addActionListener(new CreateAccountListenerMock());
        }*/
		//addListener(new CreateAccountListener(this));
	}
	
	LoginScreen(String email, String pass1, String pass2) {
		emailField = new JTextField(email);
		passField1 = new JTextField(pass1);
		passField2 = new JTextField(pass2);
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
}
