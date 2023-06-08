package mainframe;

import javax.swing.*;
import java.awt.*;

import interfaces.MediatorObserver;

public class EmailSetupFrame extends JFrame implements MediatorObserver{

    EmailSetupPanel esp;

    public EmailSetupFrame(){
        setTitle("Setup Email");
		
		esp = new EmailSetupPanel();

        add(esp, BorderLayout.CENTER);
        setSize(500,800);
        setVisible(false);
    }

    

    public EmailSetupPanel getSetupPanel(){
        return esp;
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



	@Override
	public void onCancel() {
		this.setVisible(false);
		
	}
    
}
