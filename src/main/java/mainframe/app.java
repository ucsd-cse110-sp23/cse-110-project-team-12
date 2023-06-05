package mainframe;

import java.io.IOException;

import server.MyServer;
import interfaces.*;


public class app {
    private static ServerInterface ServerInstance;
    private static LoginFrame loginFrame;
    private static AppFrame appFrame;

    public static void main (String args[]) throws IOException{
        ServerInstance = new MyServer();
        ServerInstance.runServer();
        appFrame = new AppFrame(ServerInstance);
        loginFrame = new LoginFrame(appFrame);
    }

    public static void succesfullLogin() {
        try {
            new AppFrame(null);
            ServerInstance.checkServerAvailability();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }  
}
