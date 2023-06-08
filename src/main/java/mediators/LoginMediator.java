package mediators;
import java.util.ArrayList;

import javax.swing.JButton;

import interfaces.*;
import listeners.*;
import mainframe.*;
import processing.SavefileWriter;

public class LoginMediator implements LoginButtonsObserver, LoginPanelObserver, MediatorSubject{
   
    ArrayList<LoginButtonsSubject> allButtons = new ArrayList<LoginButtonsSubject>();
    AppFrame af;
    LoginPanel lp;
    MongoInterface MongoSession;
    ErrorMessagesInterface ErrorMessagesSession;
    ArrayList<MediatorObserver> parentFrames;
    SavefileWriter sfWriter;



    //USED BY APP
    public LoginMediator(LoginFrame lf, AppFrame af, MongoInterface MongoSession, ErrorMessagesInterface ErrorMessagesSession, SavefileWriter savefileWriter){
        
        this.sfWriter = savefileWriter;
        this.parentFrames = new ArrayList<MediatorObserver>();
        this.af = af;
        this.lp = lf.getLoginPanel();
        this.allButtons = getListeners();
        this.MongoSession = MongoSession;
        this.ErrorMessagesSession = ErrorMessagesSession;
        for (LoginButtonsSubject button : allButtons){
            button.registerObserver(this);
        }
        lp.registerObserver(this);
        this.registerObserver(lf);
		this.registerObserver(af); 
    }

    // public LoginMediator(ArrayList<LoginButtonsSubject> allButtons, LoginPanel lp, MongoInterface MongoSession, ErrorMessagesInterface ErrorMessagesSession){
    //     this.parentFrames = new ArrayList<MediatorObserver>();
    //     this.allButtons = allButtons;
    //     this.lp = lp;
    //     this.MongoSession = MongoSession;
    //     this.ErrorMessagesSession = ErrorMessagesSession;
    //     for (LoginButtonsSubject button : allButtons){
    //         button.registerObserver(this);
    //     }
    //     lp.registerObserver(this);
    // }



    @Override
	public void onCreateAccount() {
		String Email = lp.getEmail();
		String Pass1 = lp.getPass1();

		if (Email.isBlank()){
            ErrorMessagesSession.showErrorMessage("Missing Email");
        }
        else if (Pass1.isBlank()){
            ErrorMessagesSession.showErrorMessage("Missing Pass1");
        } 
        else if (MongoSession.checkEmailExists(Email)){
            ErrorMessagesSession.showErrorMessage("Email taken");
        } else {
	        if (validatePassword(Pass1)) {
	           MongoSession.createAccount(Email, Pass1, Pass1); 
	           //sucessful Login
	           queryAutoLogin();
	           notifyObservers();	
	        } else {
	        	ErrorMessagesSession.showErrorMessage("Passwords do not match");
	        }
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
            queryAutoLogin();
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
    public void queryAutoLogin(){
        //User selects save my info
        boolean saveInfo = ErrorMessagesSession.confirmAutoLogin();
        ArrayList<String> info = null;
        if (saveInfo){
            System.out.println("here");
            info = new ArrayList<String>();
            info.add(lp.getEmail());
            info.add(lp.getPass1());
        }
        this.sfWriter.setLoginInfo(info);
        
    }

    //Check whether auto-login has been set
    public void checkAutoLogin(){
        ArrayList<String> result = this.sfWriter.getLoginInfo();
        if (result != null){
            String savedEmail = result.get(0);
            String savedPassword = result.get(1);
            if (MongoSession.checkValidLogin(savedEmail,savedPassword)){
                notifyObservers();
            }
            else{
                ErrorMessagesSession.showErrorMessage("Incorrect Login Details");
            }
        }
    
            
    }
    
    //validate password
    public boolean validatePassword(String pass) {
    	return ErrorMessagesSession.checkPassword(pass);
    }

    public void setListeners(LoginListener loginListener, CreateAccountListener createListener){
        JButton loginButton = lp.getLoginButton();
		JButton createAccountButton = lp.getcreateAccountButton();
		loginButton.addActionListener(loginListener);
		createAccountButton.addActionListener(createListener);
    }

    public ArrayList<LoginButtonsSubject> getListeners(){
		ArrayList<LoginButtonsSubject> allLoginButtons = new ArrayList<LoginButtonsSubject>();
        LoginListener loginListener = new LoginListener();
		CreateAccountListener createListener = new CreateAccountListener();
        setListeners(loginListener, createListener);
		allLoginButtons.add(loginListener);
		allLoginButtons.add(createListener);
        return allLoginButtons;
	}
}
