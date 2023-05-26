import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/*
  * clicking CLEAR ALL button
  * 
  * Deletes every q/a from server
  * Clears and updates prompt history list
  */

public class ClearListener implements myListener{
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < PromptHistory.getPHSize(); i++) {
          String question = PromptHistory.getElementInPH(i);
          ServerCalls.deleteFromServer(question);
        }
        //TODO Should be some kind of update method with ClearListener
        PromptHistory.resetPH();
    }

    public void registerObserver(listenerObserver panel) {
    
    }

    public void notifyObservers() {
    }
}

