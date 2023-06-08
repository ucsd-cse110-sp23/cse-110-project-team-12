package mainframe;

import java.io.IOException;

import server.MyServer;
import interfaces.*;
import mediators.*;
import processing.*;
import api.*;


public class app {
    private static ServerInterface ServerInstance;
    private static LoginFrame loginFrame;
    private static AppFrame appFrame;
    private static EmailSetupFrame emailSetupFrame;
    private static QPHPHButtonPanelPresenter postloginMediator;
    private static LoginMediator loginMediator;
    private static SavefileWriter sfWriter;

    public static void main (String args[]) throws IOException{
        ServerInstance = new MyServer();
        ServerInstance.runServer();

        appFrame = new AppFrame();
        loginFrame = new LoginFrame();
        emailSetupFrame = new EmailSetupFrame();
        MongoDB MongoSession = new MongoDB();
        sfWriter = new SavefileWriter();

        postloginMediator = new QPHPHButtonPanelPresenter(emailSetupFrame, appFrame, new Recorder(), new Whisper(), new ChatGPT(), ServerInstance, new ErrorMessages(), MongoSession); 
        loginMediator = new LoginMediator(loginFrame, appFrame, MongoSession, new ErrorMessages(), sfWriter);
        loginMediator.checkAutoLogin();

        
    }
}
