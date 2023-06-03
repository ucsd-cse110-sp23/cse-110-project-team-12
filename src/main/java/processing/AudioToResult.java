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

    public AudioToResult(File audioFile){
        try {
            WhisperSession = new Whisper(audioFile);
          } catch (IOException e) {
              e.printStackTrace();
          }
          parseFile();
    }

    public Entry getEntry(){
        return entry;
    }
    
    // @Override
    // public String getCommand() {
    //     return command;
    // }

    // @Override
    // public String getPrompt() {
    //     return prompt;
    // }

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
                        prompt = voiceOutput.substring(COMMANDS[i].length()+1);
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
                ChatGPTSession = new ChatGPT(prompt);
            } 
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            } 

            String answerString = this.ChatGPTSession.getAnswer();
            result = answerString.substring(2);
            entry = new QuestionEntry(command, prompt, result);
        }
    }

    

   
    
}