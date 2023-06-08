/**
 * @author CSE 110 - Team 12
 */
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import api.ChatGPT;
import api.Whisper;
import static org.junit.jupiter.api.Assertions.*;
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

/**
 * Test for US11 - Create email draft using the
 * 
 */
public class DS11CreateEmailTest{

	//Mock classes
	private static QuestionPanel qpMock;
	private static PromptHistory phMock;
	private static ActionEvent eventMock;
	private static Recorder recorderMock;
	private static WhisperInterface WhisperMock;
	private static ChatGPTInterface ChatGPTMock;
	private static MyServer serverMock;

	//button and listener
	private static JButton startButton;
	private static StartStopListener testListener;

	//mediator
	private static QPHPHButtonPanelPresenter testLogic;

	/**
	 * Sets up environment for mocking
	 */
	@BeforeAll
	public static void setup(){

		//Mock classes
		eventMock = mock(ActionEvent.class);
		qpMock = mock(QuestionPanel.class);
		phMock = mock(PromptHistory.class);
		recorderMock = mock(Recorder.class);
		serverMock = mock(MyServer.class);
		WhisperMock = mock(Whisper.class);
		ChatGPTMock = mock(ChatGPT.class);

		//buttons and listener
		testListener = new StartStopListener();
		startButton = new JButton();
		startButton.addActionListener(testListener);
		ArrayList<ButtonSubject> createdButtons = new ArrayList<ButtonSubject>();
		createdButtons.add(testListener);

		//mediator
		testLogic = new QPHPHButtonPanelPresenter(createdButtons, qpMock, phMock, recorderMock, WhisperMock, ChatGPTMock, serverMock);
	}

	/**
     * Test that UI updates when start/stop pressed
     * @throws Exception
     */
	@Test
	void testUIupdatedStartStop() throws Exception {
		startButton.doClick();
		verify(qpMock).startedRecording();
		TimeUnit.SECONDS.sleep(1);
		startButton.doClick();
		verify(qpMock).stoppedRecording();
	}

	/**
     * Test parsing of Create email command and its prompt
     */
	@Test
	void unitTestParseCommand(){
		ArrayList<String> expectedResult = new ArrayList<String>();
		expectedResult.add("Create Email");
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
		assertNull(result.get(1));

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
