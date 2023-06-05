package interfaces;

import java.io.IOException;

public interface ChatGPTInterface {
    public void askChatGPT(String question_text)throws IOException, InterruptedException;
    public String getQuestion();
    public String getAnswer();
}
