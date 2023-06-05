package mainframe;

import java.io.IOException;
import com.sun.net.httpserver.*; //server create()
import java.net.*;
import java.util.*;

import server.MyServer;
import interfaces.*;


public class app {
    private static ServerInterface ServerInstance;
    private static LoginFrame loginFrame;
    private static AppFrame appFrame;
    private static boolean autoLogin = false;
    private static String defaultUserEmail = null;

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
