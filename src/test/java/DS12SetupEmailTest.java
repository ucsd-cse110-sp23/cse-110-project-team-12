import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;

import java.util.ArrayList;
import java.awt.event.ActionEvent;

import interfaces.*;
import listeners.StartStopListener;
import mainframe.*;
import mediators.QPHPHButtonPanelPresenter;
import processing.*;
import server.MyServer;


public class DS12SetupEmailTest {
    
    private static QuestionPanel qpMock;
    private static PromptHistory phMock;
    private static Recorder recorderMock;
    private static WhisperInterface WhisperMock;
    private static ChatGPTInterface ChatGPTMock;
    private static MyServer serverMock;
    private static EmailSetupFrame esFrameMock;
    private static AppFrame appFrameMock;
    private static ErrorMessagesInterface ErrorMessagesMock;
    private static MongoDB MongoDBMock;

    private static QPHPHButtonPanelPresenter testLogic;

    @BeforeAll
    public static void setup(){

        qpMock = mock(QuestionPanel.class);
        phMock = mock(PromptHistory.class);
        recorderMock = mock(Recorder.class);
        serverMock = mock(MyServer.class);
        WhisperMock = mock(Whisper.class);
        ChatGPTMock = mock(ChatGPT.class);
        esFrameMock = mock(EmailSetupFrame.class);
        appFrameMock = mock(AppFrame.class);
        ErrorMessagesMock = mock(ErrorMessages.class);
        MongoDBMock = mock(MongoDB.class);

        when(appFrameMock.getQuestionPanel()).thenReturn(new QuestionPanel());
        when(appFrameMock.getPromptHistory()).thenReturn(new PromptHistory());
        when(esFrameMock.getSetupPanel()).thenReturn(new EmailSetupPanel());
        testLogic = new QPHPHButtonPanelPresenter( esFrameMock,  appFrameMock,  recorderMock,  WhisperMock, 
        ChatGPTMock,  serverMock,  ErrorMessagesMock,  MongoDBMock);
    }

    @Test
    void unitTestParseCommandWithSetupEmail(){
        ArrayList<String> expectedResult = new ArrayList<String>();
        expectedResult.add("Setup Email");
        expectedResult.add(null);
        
        //Case 1: Valid command
        String testQuestion = "setup email";
        ArrayList<String> result = testLogic.parseCommand(testQuestion); 
        assertTrue(result.equals(expectedResult));

        //Case 1.2: Valid command
        testQuestion = "Setup Email";
        result = testLogic.parseCommand(testQuestion); 
        assertTrue(result.equals(expectedResult));
    

        // //Case 2: Invalid command
        // when(WhisperMock.getQuestionText()).thenReturn("");

        // String testInvalidQuestion = "Blabla";
        // result = testLogic.parseCommand(testInvalidQuestion);
        // assertNull(result);

        // //Case 3: No command 

        // when(WhisperMock.getQuestionText()).thenReturn("");

        // String testNoQuestion = "";
        // result = testLogic.parseCommand(testNoQuestion);
        // assertNull(result);
    
    } 
}
