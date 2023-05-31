package mainframe;

import java.io.IOException;

import server.MyServer;

public class app {
    private static LoginScreen loginFrame;
    private static AppFrame appFrame;
    private static boolean autoLogin = false;
    private static String defaultUserEmail = null;

    public static void main (String args[]) throws IOException{
    	if (!autoLogin) {
	    	loginFrame = new LoginScreen();
            
    	} else {
    		appFrame = new AppFrame(defaultUserEmail);
            //force exit app if server not connected
            MyServer.checkServerAvailability();
    	}
    }  
}
