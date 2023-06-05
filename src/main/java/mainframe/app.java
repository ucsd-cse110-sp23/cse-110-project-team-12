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

    public static void main (String args[]) throws IOException{
        ServerInstance = new MyServer();
        ServerInstance.runServer();

        appFrame = new AppFrame();
        loginFrame = new LoginFrame();
        emailSetupFrame = new EmailSetupFrame();

        postloginMediator = new QPHPHButtonPanelPresenter(appFrame, new Recorder(), new Whisper(), new ChatGPT(), ServerInstance);
        postloginMediator.registerObserver(appFrame);
        postloginMediator.registerObserver(emailSetupFrame);
        loginMediator = new LoginMediator(loginFrame, new MongoDB(), new ErrorMessages());
		loginMediator.registerObserver(loginFrame);
		loginMediator.registerObserver(appFrame);  
    }
}
