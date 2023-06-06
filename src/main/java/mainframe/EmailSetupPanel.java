package mainframe;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class EmailSetupPanel extends JPanel{
    JButton setupButton;

    JLabel firstNameLabel, lastNameLabel, displayNameLabel, emailLabel, SMTPLabel, TLSLabel, PasswordLabel;
    ArrayList<JLabel> allLabels = new ArrayList<JLabel>(Arrays.asList(firstNameLabel, lastNameLabel, displayNameLabel, emailLabel, SMTPLabel, TLSLabel, PasswordLabel));
    
	JTextField firstNameField, lastNameField, displayNameField, emailField, SMTPField, TLSField, PasswordField;
    ArrayList<JTextField> allFields = new ArrayList<JTextField>(Arrays.asList(firstNameField, lastNameField, displayNameField, emailField, SMTPField, TLSField, PasswordField));
    int fieldLength = 20;

    EmailSetupPanel(){
        super(new GridLayout(8,1));

        String[] labelNames = {"First Name", "Last Name", "Display Name", "Email", "SMTP Host", "TLS port", "Email Password"};  
        for (int i = 0; i < 7; i++){
            JLabel label = allLabels.get(i);
            JTextField textField = allFields.get(i);
            label = new JLabel();
            label.setText(labelNames[i]);
            textField = new JTextField(fieldLength);
            this.add(label);
            this.add(textField);
        }

        setupButton = new JButton("Setup");
    }


}
