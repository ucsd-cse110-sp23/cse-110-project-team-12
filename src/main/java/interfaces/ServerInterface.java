/**
 * @author CSE 110 - Team 12
 */
package interfaces;

import processing.Entry;
import java.io.IOException;

/**
 * MyServer methods
 *
 */
public interface ServerInterface {
    public  boolean checkServerAvailability();
    public void runServer() throws IOException;
    public void postToServer(Entry entry);
    public String getFromServer(String question);
    public void deleteFromServer(String question);
}
