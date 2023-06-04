package mainframe;

import java.io.IOException;

import javax.swing.DefaultListModel;

import api.*;
import server.MyServer;

public class app {
    private static boolean autoLogin = false;
    private static String defaultUserEmail = null;

   
    public static void main(String[] args) throws IOException{
    	
    	if (!autoLogin) {
    		LoginScreen login = new LoginScreen();
    		
    	} else {
    		final MongoDB mongoSession = new MongoDB();
    		new AppFrame(defaultUserEmail, mongoSession.getPrompts(defaultUserEmail));
            //force exit app if server not connected
            MyServer.checkServerAvailability();
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
