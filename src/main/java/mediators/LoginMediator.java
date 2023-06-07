package mediators;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import interfaces.*;
import mainframe.*;
import processing.ErrorMessages;

public class LoginMediator implements LoginButtonsObserver, LoginPanelObserver, MediatorSubject{
   
    ArrayList<LoginButtonsSubject> allButtons = new ArrayList<LoginButtonsSubject>();
    LoginPanel lp;
    MongoInterface MongoSession;
    ErrorMessagesInterface ErrorMessagesSession;
    ArrayList<MediatorObserver> parentFrames;
    boolean autoLogIn;
    public static final String savedUserInfoFile = "users/savedInfo.txt";

    public LoginMediator(LoginFrame lf, MongoInterface MongoSession, ErrorMessagesInterface ErrorMessagesSession){
        this.parentFrames = new ArrayList<MediatorObserver>();
        this.lp = lf.getLoginPanel();
        this.allButtons = lf.addListeners(lp);
        this.MongoSession = MongoSession;
        this.ErrorMessagesSession = ErrorMessagesSession;
        for (LoginButtonsSubject button : allButtons){
            button.registerObserver(this);
        }
        lp.registerObserver(this);
    }

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
           //sucessful Login
           openAutoLogin();
            notifyObservers();	
        }
        
	}

	@Override
	public void onLogin() {
        String Email = lp.getEmail();
		String Pass1 = lp.getPass1();
        if (Email.isBlank()){
            ErrorMessagesSession.showErrorMessage("Missing Email");
        }
        else if (Pass1.isBlank()){
            ErrorMessagesSession.showErrorMessage("Missing Password");
        }
        if (MongoSession.checkValidLogin(Email,Pass1)){
            openAutoLogin();
            notifyObservers();
        }
        else{
            ErrorMessagesSession.showErrorMessage("Incorrect Login Details");
        }
		
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

    //Called upon any succesful login and account creation
    public void openAutoLogin(){
        //User selects save my info
        if (ErrorMessagesSession.confirmAutoLogin()){
            File file = new File(savedUserInfoFile);
            try{
                file.createNewFile();
                FileWriter writer = new FileWriter(file, false);
                writer.write(lp.getEmail() + "\n" + lp.getPass1());
                writer.close();
            }
            catch(IOException e){
                //IO exception 
                e.printStackTrace();
            }
        }
    }
    
}
