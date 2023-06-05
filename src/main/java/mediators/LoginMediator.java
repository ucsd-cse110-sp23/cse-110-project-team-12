package mediators;
import java.io.*;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import api.*;
import interfaces.*;
import mainframe.*;
import processing.*;

public class LoginMediator implements LoginButtonsObserver, LoginPanelObserver, MediatorSubject{
   
    ArrayList<LoginButtonsSubject> allButtons = new ArrayList<LoginButtonsSubject>();
    LoginPanel lp;
    MongoInterface MongoSession;
    ErrorMessagesInterface ErrorMessagesSession;
    ArrayList<MediatorObserver> parentFrames;

    public LoginMediator(ArrayList<LoginButtonsSubject> allButtons, LoginPanel lp, MongoInterface MongoSession, ErrorMessagesInterface ErrorMessagesSession){
        this.parentFrames = new ArrayList<MediatorObserver>();
        this.allButtons = allButtons;
        this.lp = lp;
        this.MongoSession = MongoSession;
        this.ErrorMessagesSession = ErrorMessagesSession;
        for (LoginButtonsSubject button : allButtons){
            button.registerObserver(this);
        }
        lp.registerObserver(this);
    }


    @Override
	public void onCreateAccount() {
		String Email = lp.getEmail();
		String Pass1 = lp.getPass1();
		String Pass2 = lp.getPass2();

		//TODO Verify sPass2 = sPass1
		if (Email.isBlank()){
            ErrorMessagesSession.showErrorMessage("Missing Email");
        }
        else if (Pass1.isBlank()){
            ErrorMessagesSession.showErrorMessage("Missing Pass1");
        }
        else if (Pass2.isBlank()){
            ErrorMessagesSession.showErrorMessage("Missing Pass2");
        }
        else if (!Pass1.equals(Pass2)){
           ErrorMessagesSession.showErrorMessage("Passwords do not match");
        }
        else if (MongoSession.checkEmailExists(Email)){
            ErrorMessagesSession.showErrorMessage("Email taken");
        }
        else{
           MongoSession.createAccount(Email, Pass1, Pass2); 	
        }
        //sucessful Login
        notifyObservers();
	}

	@Override
	public void onLogin() {
		// String sEmail = this.emailField.getText();
		// String sPass1 = this.passField1.getText();
		
		// MongoDB mongoSession = new MongoDB();
		
		// mongoSession.login(sEmail, sPass1);
		// this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		
		// app.succesfullLogin();
		
	}


    @Override
    public void registerObserver(MediatorObserver parentFrame) {
        this.parentFrames.add(parentFrame);
    }


    @Override
    public void notifyObservers() {
        for (MediatorObserver panel : parentFrames){
            panel.onLoginClosing();
        }  
    }
    
}
