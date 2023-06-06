import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JList;

import java.util.ArrayList;
import java.util.Arrays;

import interfaces.*;
import mainframe.*;
import mediators.QPHPHButtonPanelPresenter;
import processing.*;
import server.MyServer;


public class DS14UpdatedPromptHistory {
    

    private static Recorder recorderMock;
    private static WhisperInterface WhisperMock;
    private static ChatGPTInterface ChatGPTMock;
    private static MyServer serverMock;
    private static EmailSetupFrame esFrameMock;
    private static AppFrame appFrameMock;
    private static ErrorMessagesInterface ErrorMessagesMock;
    private static MongoDB MongoDBMock;
    private static QuestionPanel qpMock;
    private static PromptHistory phMock;
    private static EmailSetupPanel epMock;

    private static QPHPHButtonPanelPresenter testLogic;
    private static JButton startButton;
    private static JButton setupButton;

    @BeforeEach
    public void setup(){

        //Mock classes
        qpMock = mock(QuestionPanel.class);
        phMock = mock(PromptHistory.class);
        epMock = mock(EmailSetupPanel.class);
        recorderMock = mock(Recorder.class);
        serverMock = mock(MyServer.class);
        WhisperMock = mock(Whisper.class);
        ChatGPTMock = mock(ChatGPT.class);
        esFrameMock = mock(EmailSetupFrame.class);
        appFrameMock = mock(AppFrame.class);
        ErrorMessagesMock = mock(ErrorMessages.class);
        MongoDBMock = mock(MongoDB.class);

        //Mock Method calls
        when(appFrameMock.getQuestionPanel()).thenReturn(qpMock);
        when(appFrameMock.getPromptHistory()).thenReturn(phMock);
        when(esFrameMock.getSetupPanel()).thenReturn(epMock);
        when(qpMock.getStartButton()).thenReturn(startButton = new JButton());
        when(phMock.getPromptList()).thenReturn(new JList<String>());
        when(epMock.getSetupButton()).thenReturn(setupButton = new JButton());

         //Testing Classes
         testLogic = new QPHPHButtonPanelPresenter( esFrameMock,  appFrameMock,  recorderMock,  WhisperMock, 
         ChatGPTMock,  serverMock,  ErrorMessagesMock,  MongoDBMock);
    }

    // Scenario 1: User sees updated prompt history after asking question
    // Given I have clicked the “Start” button
    // And said the “Question” command
    // And asked my question
    // When the app displays the answer
    // Then “Question” followed by my question is added to my prompt history
    
    @Test
    void test_1_askQuestionCommand(){

    }

    // Scenario 2: User sees updated prompt history after creating email
	// Given I have clicked the “Start” button
    // And said the “Create email” command
    // And said the content of my email
    // When the app displays the drafted email 
    // Then “Create email” followed by the content of my email is added to my prompt history

    @Test
    void test_2_askSetupEmailCommand(){

    }

    // Scenario 3: User sees updated prompt history after sending email
    // Given I have clicked the “Start” button
    // And said the “Send email to <EMAIL ADDRESS>” command
    // When the app displays the results of sending the email
    // Then “Send email to <EMAIL ADDRESS>” followed by the result of sending the email is added to my prompt history

    @Test
    void test_3_askSendEmailCommand(){
        
    }
}
