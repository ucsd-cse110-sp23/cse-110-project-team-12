/**
 * @author CSE 110 - Team 12
 */
package mainframe;

import java.io.IOException;

import server.MyServer;
import interfaces.*;
import mediators.*;
import processing.*;
import api.*;

/**
 * Main SayItAssistant2 application
 * 	Creates instances of necessary classes
 *
 */
public class app {
	
    private static ServerInterface ServerInstance;
    private static LoginFrame loginFrame;
    private static AppFrame appFrame;
    private static EmailSetupFrame emailSetupFrame;
    private static QPHPHButtonPanelPresenter postloginMediator;
    private static LoginMediator loginMediator;
    private static TLSEmail tlsEmail;
    private static SavefileWriter sfWriter;

    /**
     * Main application
     * Opens on login screen if auto login disabled
     * Opens on AppFrame if auto login enabled
     * 
     * @param args
     * @throws IOException
     */
    public static void main (String args[]) throws IOException{
        ServerInstance = new MyServer();
        ServerInstance.runServer();

        appFrame = new AppFrame();
        loginFrame = new LoginFrame();
        emailSetupFrame = new EmailSetupFrame();
        MongoDB MongoSession = new MongoDB();
        sfWriter = new SavefileWriter();
        tlsEmail = new TLSEmail();
        EmailUtil emailUtil = new EmailUtil();
        
        //mediator for processes after logging in
        postloginMediator = new QPHPHButtonPanelPresenter(emailSetupFrame, appFrame, new Recorder(), new Whisper(), new ChatGPT(), ServerInstance, new ErrorMessages(), MongoSession, tlsEmail, emailUtil);
        
        //mediator for processes while logging in
        loginMediator = new LoginMediator(loginFrame, appFrame, MongoSession, new ErrorMessages(), sfWriter);
        loginMediator.checkAutoLogin();
        
    }
}
