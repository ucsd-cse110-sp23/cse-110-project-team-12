import static org.mockito.Mockito.mock;
// import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JList;

import java.util.ArrayList;

import interfaces.*;
import mainframe.*;
import mediators.QPHPHButtonPanelPresenter;
import processing.*;
import server.MyServer;


public class DS16ClearAllTest {
    

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

    //Unit test for parsing of "Clear All" command
    @Test
    void unitTest_parseClearAll(){
        String testCommand = "Clear All";
        ArrayList<String> expectedResult = new ArrayList<String>();
        expectedResult.add(testCommand);
        expectedResult.add(null);
        ArrayList<String> result = testLogic.parseCommand(testCommand);
        assertEquals(result,expectedResult);

        testCommand = "ClEar all";
        result = testLogic.parseCommand(testCommand);
        assertEquals(result,expectedResult);
        
    }

    //  Scenario 1:  User successfully clears prompt history
    // 	Given that I have clicked “Start”
    // 	And there are entries in the prompt history
    // 	When I start by saying “Clear All”
    // Then the app should remove all command and prompt pair entries from the prompt history
    // And the prompt/message screen should be reset.
    
    @Test
    void test_1_SuccesfullClear() throws InterruptedException{
        String testCommand = "Clear All";
        int test5Entries = 5;
        String testTitle = "Title ";

        when(WhisperMock.getQuestionText()).thenReturn(testCommand);
        when(phMock.getPHSize()).thenReturn(test5Entries);
        for (int i = 0; i < test5Entries; i++){
            when(phMock.getTitle(i)).thenReturn(testTitle + i);
        }
        //Start recording
        startButton.doClick();
        TimeUnit.SECONDS.sleep(1);
        //Stop recording
        startButton.doClick();
        
       
        for (int i = 0; i < test5Entries; i++){
            verify(serverMock).deleteFromServer(testTitle + i);
        }
        verify(phMock).resetPH();
        verify(qpMock).onDelete();
    }

}
