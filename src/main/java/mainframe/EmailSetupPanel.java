/**
 * @author CSE 110 - Team 12
 */
package mainframe;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * EmailSetupPanel for user to configure their email settings
 */
public class EmailSetupPanel extends JPanel{
	
    JButton setupButton;
    JButton cancelButton;

    JLabel firstNameLabel, lastNameLabel, displayNameLabel, emailLabel, SMTPLabel, TLSLabel, PasswordLabel;
    ArrayList<JLabel> allLabels = new ArrayList<JLabel>(Arrays.asList(firstNameLabel, lastNameLabel, displayNameLabel, emailLabel, SMTPLabel, TLSLabel, PasswordLabel));
    
	JTextField firstNameField, lastNameField, displayNameField, emailField, SMTPField, TLSField, PasswordField;
    ArrayList<JTextField> allFields = new ArrayList<JTextField>();
    int fieldLength = 20;

    /**
     * Constructor for EmailSetupPanel
     * Sets labels and creates fields
     */
    public EmailSetupPanel(){
        super(new GridLayout(8,1));

        String[] labelNames = {"First Name", "Last Name", "Display Name", "Email", "SMTP Host", "TLS port", "Email Password"};  
        for (int i = 0; i < 7; i++){
            JLabel label = allLabels.get(i);
            label = new JLabel();
            label.setText(labelNames[i]);
            JTextField textField = new JTextField(fieldLength);
            this.add(label);
            this.add(textField);
            allFields.add(textField);
        }

        setupButton = new JButton("Save");
        this.add(setupButton);
        
        cancelButton = new JButton("Cancel");
        this.add(cancelButton);
    }    

    /**
     * Checks if all email fields are filled
     * 
     * @return boolean - true if all fields filled, false otherwise
     */
    public boolean checkAllFieldsFilled(){
        for (JTextField field : allFields){
            if (field.getText().isBlank()){
                return false;
            }
        }
        return true;
    }

    /**
     * Gets all of user's email settings
     * 
     * @return ArrayList<String> - email settings
     */
    public ArrayList<String> getAllFields(){
        ArrayList<String> fields = new ArrayList<String>();
        for (JTextField field : allFields){
            fields.add(field.getText());
        }
        return fields;
    }
    
    /**
     * Gets user's display name
     * 
     * @return String display name
     */
    public String getDisplayName() {
    	return displayNameField.getText();
    }

    /**
     * Gets panel's setup button
     * 
     * @return JButton - setup button
     */
    public JButton getSetupButton() {
        return setupButton;
    }
    
    /**
     * Gets panel's cancel button
     * 
     * @return JButton - cancel button
     */
    public JButton getCancelButton() {
    	return cancelButton;
    }

}
