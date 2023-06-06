import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import api.ChatGPT;
import api.Whisper;

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

public class DS11CreateEmailTest{

//     Scenario 1:  User successfully uses “Question” command
// Given that I click “Start”
// When I say command “Question” 
// And ask a question
// Then I should see “Question” with the question in the prompt box
// And the question’s answer in the result box 
// And the it should have copy-paste functionality

// Scenario 2: User unsuccessfully uses “Question” command
// Given that I click “Start”
// When I say command “Question”
// And do not follow it up with a question
// Then I will see an error message stating no question was detected
    private static QuestionPanel qpMock;
    private static PromptHistory phMock;
    private static ActionEvent eventMock;
    private static Recorder recorderMock;
    private static WhisperInterface WhisperMock;
    private static ChatGPTInterface ChatGPTMock;
    private static MyServer serverMock;

    private static JButton startButton;
    private static StartStopListener testListener;
    private static QPHPHButtonPanelPresenter testLogic;
    
    
    @BeforeAll
    public static void setup(){
        ArrayList<ButtonSubject> createdButtons = new ArrayList<ButtonSubject>();

        eventMock = mock(ActionEvent.class);
        qpMock = mock(QuestionPanel.class);
        phMock = mock(PromptHistory.class);
        recorderMock = mock(Recorder.class);
        serverMock = mock(MyServer.class);
        WhisperMock = mock(Whisper.class);
        ChatGPTMock = mock(ChatGPT.class);

        testListener = new StartStopListener();
        startButton = new JButton();
        startButton.addActionListener(testListener);
        createdButtons.add(testListener);
        testLogic = new QPHPHButtonPanelPresenter(createdButtons, qpMock, phMock, recorderMock, WhisperMock, ChatGPTMock, serverMock);
    }
    
    //Create email To Henry, let's get lunch.
    @Test
	void testUIupdatedStartStop() throws Exception {
        startButton.doClick();
        verify(qpMock).startedRecording();
        TimeUnit.SECONDS.sleep(1);
        startButton.doClick();
        verify(qpMock).stoppedRecording();
    }

    @Test
    void unitTestParseCommand(){
        ArrayList<String> expectedResult = new ArrayList<String>();
        expectedResult.add("Create email");
        expectedResult.add("To Henry, let's get lunch.");
        
        //Case 1: Valid command
        when(WhisperMock.getQuestionText()).thenReturn("Create email To Henry, let's get lunch.");
        String testEmail = "Create email To Henry, let's get lunch.";
        ArrayList<String> result = testLogic.parseCommand(testEmail); 
        assertTrue(result.equals(expectedResult));
    

        //Case 2: Invalid use of command
        when(WhisperMock.getQuestionText()).thenReturn("Create email");
        String testInvalidEmail = "Create email";
        result = testLogic.parseCommand(testInvalidEmail);
        assertNull(result);
        
      //Case 3: Invalid command
        when(WhisperMock.getQuestionText()).thenReturn("Blabla blabla");
        String testInvalidCommand = "Blabla blabla";
        result = testLogic.parseCommand(testInvalidCommand);
        assertNull(result);

        //Case 3: No command 

        when(WhisperMock.getQuestionText()).thenReturn("");

        String testNoEmail = "";
        result = testLogic.parseCommand(testNoEmail);
        assertNull(result);
    
    } 
}
