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


public class DS12SetupEmailTest {
    

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
    
    } 

//     Scenario 1: User prompted to configure their email settings
        // Given I click “Start” 
        // When I start by saying “Setup email”
        // Then a screen pops up to ask for my first name, last name, display name, email address, SMTP host, TLS port, and my email password
        // And there is also a “Save” and a “Cancel” button.
      // When the User saves their email settings
        // And I click the “Save” button
        // Then my information is saved
    @Test
    void test_1_SetupEmailWithValidInput() throws InterruptedException{
        ArrayList<String> testInput = new ArrayList<String>(Arrays.asList("First Name", "Last Name", "Display Name", "Email", "SMTP Host", "TLS port", "Email Password"));
        when(WhisperMock.getQuestionText()).thenReturn("Setup Email");
        startButton.doClick();
        TimeUnit.SECONDS.sleep(1);
        startButton.doClick();
        
        //Test Valid Setup Email command
        verify(esFrameMock).onEmailSetup();

        //Simulate filling all fields
        when(epMock.checkAllFieldsFilled()).thenReturn(true);
        when(epMock.getAllFields()).thenReturn(testInput);

        //Check whether MongoDB is updated
        setupButton.doClick();
        verify(MongoDBMock).setupEmail(testInput);
    }

//     Scenario 2: User cancels changing their email settings
        // Given I am editing my email configurations
        // When I click the “Cancel” button
        // Then my information is not saved

        @Test
        void test_2_SetupEmailWithInvalidInput() throws InterruptedException{
            ArrayList<String> testInput = new ArrayList<String>(Arrays.asList("First Name", "Last Name", "Display Name", "Email", "SMTP Host", "TLS port", "Email Password"));
            when(WhisperMock.getQuestionText()).thenReturn("Setup Email");
            startButton.doClick();
            TimeUnit.SECONDS.sleep(1);
            startButton.doClick();
            
            //Test Valid Setup Email command
            verify(esFrameMock).onEmailSetup();
    
            //Simulate filling all fields
            when(epMock.checkAllFieldsFilled()).thenReturn(true);
            when(epMock.getAllFields()).thenReturn(testInput);
    
            //Check whether MongoDB is updated
            verify(MongoDBMock, times(0)).setupEmail(testInput);
        }

        //     Scenario 3: User finishes changing their email settings 
        // Given I am editing my email configurations 
        // And I forget one field
        // THen when I click setup 
        // Then my information is not saved

        @Test
        void test_3_SetupEmailWithNoInput() throws InterruptedException{
            ArrayList<String> testInput = new ArrayList<String>(Arrays.asList("First Name", "Last Name", null, "Email", "SMTP Host", "TLS port", "Email Password"));
            String expectedErrorMessage = "Fill up all fields";
            when(WhisperMock.getQuestionText()).thenReturn("Setup Email");
            startButton.doClick();
            verify(qpMock).startedRecording();
            TimeUnit.SECONDS.sleep(1);
            startButton.doClick();
            verify(qpMock).stoppedRecording();
            
            //Test Valid Setup Email command
            verify(esFrameMock).onEmailSetup();
    
            //Simulate filling all fields
            when(epMock.checkAllFieldsFilled()).thenReturn(false);
    
            //Check whether MongoDB is NOT updated and error message
            setupButton.doClick();
            verify(ErrorMessagesMock).showErrorMessage(expectedErrorMessage);
            verify(MongoDBMock, times(0)).setupEmail(testInput);
        }
}
