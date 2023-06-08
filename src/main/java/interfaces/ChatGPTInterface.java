/**
 * @author CSE 110 - Team 12
 */
package interfaces;

import java.io.IOException;

/**
 * ChatGPT methods
 * 
 * askChatGPT
 * getAnswer
 */
public interface ChatGPTInterface {
    public void askChatGPT(String question_text)throws IOException, InterruptedException;
    public String getAnswer();
}
