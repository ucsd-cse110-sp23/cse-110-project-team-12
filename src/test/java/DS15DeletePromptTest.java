/**
 * @author CSE 110 - Team 12
 */
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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

/**
 * Test for US15 - User uses Delete prompt command to delete a selected entry
 *
 */
public class DS15DeletePromptTest {
    
	//mock classes
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
    private static TLSEmail tlsMock;
    private static EmailUtil emailUtilMock;

    //mediator
    private static QPHPHButtonPanelPresenter testLogic;
    
    //buttons
    private static JButton startButton;
    private static JButton setupButton;
    private static JButton cancelButton;

    /**
	 * Sets up environment for mocking
	 */
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
        tlsMock = mock(TLSEmail.class);
        emailUtilMock = mock(EmailUtil.class);

        //Mock Method calls
        when(appFrameMock.getQuestionPanel()).thenReturn(qpMock);
        when(appFrameMock.getPromptHistory()).thenReturn(phMock);
        when(esFrameMock.getSetupPanel()).thenReturn(epMock);
        when(qpMock.getStartButton()).thenReturn(startButton = new JButton());
        when(phMock.getPromptList()).thenReturn(new JList<String>());
        when(epMock.getSetupButton()).thenReturn(setupButton = new JButton());
        when(epMock.getCancelButton()).thenReturn(cancelButton = new JButton());

         //Testing Classes
         testLogic = new QPHPHButtonPanelPresenter( esFrameMock,  appFrameMock,  recorderMock,  WhisperMock, 
         ChatGPTMock,  serverMock,  ErrorMessagesMock,  MongoDBMock, tlsMock, emailUtilMock);
    }

    /**
     * Unit test for parsing of "Delete" command
     */
    @Test
    void unitTest_parseCommand(){
        String testCommand = "Delete Prompt";
        ArrayList<String> expectedResult = new ArrayList<String>();
        expectedResult.add(testCommand);
        expectedResult.add(null);
        ArrayList<String> result = testLogic.parseCommand(testCommand);
        assertEquals(result,expectedResult);
    }

    /**
     * Scenario 1: User deletes selected prompt
     * Given that a specific prompt was selected from my prompt history
     * And I click “Start”
     * When I start by saying “Delete Prompt”
     * Then the selected prompt should be removed from the prompt history
     * And from the prompt/message screen
     * 
     * @throws InterruptedException
     */
    @Test
    void test_1_DeletePrompt() throws InterruptedException{
        String testCommand = "Delete Prompt";
        int selectedIndex = 1;
        String selectedPrompt = "Question: When is christmas?";

        //set expected results
        when(WhisperMock.getQuestionText()).thenReturn(testCommand);
        when(phMock.getSelectedIndex()).thenReturn(selectedIndex);
        when(phMock.getTitle(selectedIndex)).thenReturn(selectedPrompt);
        
        //Start recording
        startButton.doClick();
        TimeUnit.SECONDS.sleep(1);
        //Stop recording
        startButton.doClick();
        
       //verify that server and UI were updated
        verify(serverMock).deleteFromServer(selectedPrompt);
        verify(qpMock).onDelete();
        verify(phMock).removePH(selectedIndex);
    }

    /**
     * Scenario 2: User has no prompt selected
     * Given that no specific prompt was selected from my prompt history
     * And I click “Start”
     * When I start by saying “Delete Prompt”
     * Then the app should alert me that no prompt was selected
     * 
     * @throws InterruptedException
     */
    @Test
    void test_2_noPromptToDelete() throws InterruptedException{
        String testCommand = "Delete Prompt";
        int selectedIndex = -1;
        String selectedPrompt = "Question: When is christmas?";

        //set expected results
        when(WhisperMock.getQuestionText()).thenReturn(testCommand);
        when(phMock.getSelectedIndex()).thenReturn(selectedIndex);
        when(phMock.getTitle(selectedIndex)).thenReturn(selectedPrompt);
        
        //Start recording
        startButton.doClick();
        TimeUnit.SECONDS.sleep(1);
        //Stop recording
        startButton.doClick();
        
       //verify server and UI were updated
        verify(serverMock, times(0)).deleteFromServer(selectedPrompt);
        verify(qpMock, times(0)).onDelete();
        verify(phMock, times(0)).removePH(selectedIndex);
    }
}
