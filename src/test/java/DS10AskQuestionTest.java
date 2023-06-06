/*import static org.junit.Assert.assertTrue;

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

public class DS10AskQuestionTest{

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
    
    //Question: When is Christmas?
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
        expectedResult.add("Question");
        expectedResult.add("When is Christmas");
        
        //Case 1: Valid command
        when(WhisperMock.getQuestionText()).thenReturn("Question, When is Christmas");
        String testQuestion = "Question: When is Christmas";
        ArrayList<String> result = testLogic.parseCommand(testQuestion ); 
        System.out.println(result);
        assertTrue(result.equals(expectedResult));
    

        //Case 2: Invalid command
        when(WhisperMock.getQuestionText()).thenReturn("");

        String testInvalidQuestion = "Blabla";
        result = testLogic.parseCommand(testInvalidQuestion);
        assertNull(result);

        //Case 3: No command 

        when(WhisperMock.getQuestionText()).thenReturn("");

        String testNoQuestion = "";
        result = testLogic.parseCommand(testNoQuestion);
        assertNull(result);
    
    } 
}*/