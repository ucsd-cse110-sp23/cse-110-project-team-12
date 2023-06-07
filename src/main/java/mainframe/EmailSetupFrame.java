package mainframe;

import javax.swing.*;
import java.awt.*;

import interfaces.MediatorObserver;

public class EmailSetupFrame extends JFrame implements MediatorObserver{

    public EmailSetupFrame(){
        setTitle("Setup Email");
		
		EmailSetupPanel esp = new EmailSetupPanel();

        add(esp, BorderLayout.CENTER);
        setSize(500,800);
        setVisible(false);
    }
    

    @Override
    public void onEmailSetup() {
        this.setVisible(true);
        this.setAlwaysOnTop(true);
        this.setAlwaysOnTop(false);
    }

    @Override
    public void onLoginClosing() {
        //Does nothing
    }
    
}
