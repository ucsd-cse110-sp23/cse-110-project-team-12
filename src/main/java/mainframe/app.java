package mainframe;

import java.io.IOException;

import javax.swing.DefaultListModel;

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

    public static void succesfulLogin(String email) {
        try {
            new AppFrame(email);
            MyServer.checkServerAvailability();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void succesfulLogin(String email, DefaultListModel<String> entries) {
        try {
            new AppFrame(email, entries);
            MyServer.checkServerAvailability();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
}
