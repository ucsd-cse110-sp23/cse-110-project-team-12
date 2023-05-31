import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JOptionPane;

public class LoginListener implements myListener{
    private String email, pwd;
    private LoginScreen myScreen;

    public LoginListener(LoginScreen screen){
        myScreen = screen;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.email = myScreen.emailField.getText();
        this.pwd = myScreen.passField1.getText();

        CheckEmailDupe checkEmail = new CheckEmailDupe(email);
        CheckPassword checkpwd = new CheckPassword(email, pwd);

        if(checkEmail.emailExists)
        {
            JOptionPane.showMessageDialog(myScreen, "Email does not exist. Please try again.", "Login Failed", JOptionPane.WARNING_MESSAGE);
        }else if(checkpwd.matching)
        {
            JOptionPane.showMessageDialog(myScreen, "Incorrect password. Please try again.", "Login Failed", JOptionPane.WARNING_MESSAGE);
        }else if(email == null || pwd == null)
        {
            JOptionPane.showMessageDialog(null, "Error: Email or Password is missing!");
        }else
        {
                try {
                    AppFrame app = new AppFrame(email);
                    app.setVisible(true);
                    //force exit app if server not connected
                    MyServer.checkServerAvailability();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
        }
    }


    
    @Override
    public void registerObserver(listenerObserver panel) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registerObserver'");
    }

    @Override
    public void notifyObservers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'notifyObservers'");
    }


}

