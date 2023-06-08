/**
 * @author CSE 110 - Team 12
 */
package mainframe;

import javax.swing.*;
import java.awt.*;

import interfaces.MediatorObserver;

/**
 * Frame for EmailSetupPanel
 * Opens when Setup Email commands used
 */
public class EmailSetupFrame extends JFrame implements MediatorObserver{

	EmailSetupPanel esp;

	/**
	 * Constructor for frame for setting up email
	 */
	public EmailSetupFrame(){
		setTitle("Setup Email");

		esp = new EmailSetupPanel();

		add(esp, BorderLayout.CENTER);
		setSize(500,800);
		setVisible(false);
	}

	/**
	 * Gets frame's corresponding panel
	 * 
	 * @return EmailSetupPanel
	 */
	public EmailSetupPanel getSetupPanel(){
		return esp;
	}

	/**
	 * Frame opened when Setup Email commands used
	 */
	@Override
	public void onEmailSetup() {
		this.setVisible(true);
		this.setAlwaysOnTop(true);
		this.setAlwaysOnTop(false);
	}

	/**
	 * Frame closes when Cancel button clicked
	 */
	@Override
	public void onCancel() {
		this.setVisible(false);	
	}

	/**
	 * Does nothing
	 */
	@Override
	public void onLoginClosing() {}

}
