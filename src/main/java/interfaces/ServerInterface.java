package interfaces;
import processing.Entry;
import java.io.IOException;

public interface ServerInterface {
    public  boolean checkServerAvailability();
    public void runServer() throws IOException;
    public void postToServer(Entry entry);
    public String getFromServer(String question);

}
