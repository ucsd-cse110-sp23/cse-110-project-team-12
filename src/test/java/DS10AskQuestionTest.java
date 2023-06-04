import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
    private static ActionEvent eventMock;
    private static AudioToResult audioToResultMock;
    private static Recorder recorderMock;
    private static JButton startButton;
    private static StartStopListener testListener;
    private static QPHPHButtonPanelPresenter testLogic;
    
    
    @BeforeAll
    public static void setup(){
        ArrayList<ButtonSubject> createdButtons = new ArrayList<ButtonSubject>();
        audioToResultMock = mock(AudioToResult.class);
        eventMock = mock(ActionEvent.class);
        qpMock = mock(QuestionPanel.class);
        phMock = mock(PromptHistory.class);
        recorderMock = mock(Recorder.class);
        testListener = new StartStopListener();
        startButton = new JButton();
        startButton.addActionListener(testListener);
        createdButtons.add(testListener);
        testLogic = new QPHPHButtonPanelPresenter(createdButtons, qpMock, phMock, recorderMock, audioToResultMock);
    }
    
    //Question: When is Christmas?
    @Test
	void testValidQuestion() throws Exception {
        startButton.doClick();
        verify(qpMock).startedRecording();
        TimeUnit.SECONDS.sleep(1);
        startButton.doClick();
        verify(qpMock).stoppedRecording();
        Entry entry = new QuestionEntry("Question", "When is Christmas", "25 December");
        verify(audioToResultMock).getEntry();
        
    }
}