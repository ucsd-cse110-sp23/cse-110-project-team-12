package processing;

import java.io.File;
import java.io.IOException;

import api.*;
import interfaces.*;

public class AudioToResult implements AudioToResultInterface{

    private final String[] COMMANDS = {"Question", "Send email", "Create email", "Setup email", "Delete prompt", "Clear all"};
    private WhisperInterface WhisperSession;
    private ChatGPTInterface ChatGPTSession;
    private Entry entry = null;

    public AudioToResult(WhisperInterface WhisperSession, ChatGPTInterface ChatGPTSession){
        this.WhisperSession = WhisperSession;
        this.ChatGPTSession = ChatGPTSession;
        parseFile();
    }

    public Entry getEntry(){
        return entry;
    }
    

    // @Override
    // public String getResult() {
    //     return result;
    // }

    private void parseFile(){
        
        String voiceOutput = this.WhisperSession.getQuestionText();
        String command = null;
        String prompt = null;
        String result = null;
        //No Audio detected
        if (voiceOutput == null){
            return;
        } 
        else{
            for (int i = 0; i < COMMANDS.length; i++) {
			
                if (voiceOutput.startsWith(COMMANDS[i])) {
                    command = COMMANDS[i];
                    //check if there are remaining words first
                    try {
                        prompt = voiceOutput.substring(COMMANDS[i].length()+2);
                    }
                    catch (StringIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }
            }
        }

        if (command == null){
            entry = null;
            return;
        }

        //US10: Command is a question
        if (command.equalsIgnoreCase("Question")) {            
            
            //real ask question to chatGPT    
            try {
                this.ChatGPTSession.askChatGPT(prompt);
            } 
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            } 

            result = this.ChatGPTSession.getAnswer();
            entry = new QuestionEntry(command, prompt, result);
        }
    }
    

    

   
    
}