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


public class DS13SendEmailTest {
    

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

    private static QPHPHButtonPanelPresenter testLogic;
    private static JButton startButton;
    private static JButton setupButton;
    private static JButton cancelButton;

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

    //Unit test for parsing of "Send Email" command
    @Test
    void unitTest_parseSendEmail(){
    	ArrayList<String> expectedResult = new ArrayList<String>();
        expectedResult.add("Send Email");
        expectedResult.add("to jill at ucsd dot edu");
        
        String valid = "Send email to jill at ucsd dot edu";
        String invalid = "Send email";
        String empty = "";

        //Case 1: valid command
        when(WhisperMock.getQuestionText()).thenReturn(valid);
        ArrayList<String> result = testLogic.parseCommand(valid);
        assertEquals(result,expectedResult);

        expectedResult.remove(1);
        expectedResult.add(null);
        //Case 2: invalid use of command
        when(WhisperMock.getQuestionText()).thenReturn(invalid);
        result = testLogic.parseCommand(invalid);
        assertEquals(result, expectedResult);
        
        //Case 3: no command
        when(WhisperMock.getQuestionText()).thenReturn("");
        result = testLogic.parseCommand(empty);
        assertNull(result);
        
    }
    
    /*
     * Scenario 1: User successfully sends drafted email
     * Given I have properly configured my email settings
     * And I have created an email
     * And have that email selected in my prompt history
     * And I click the “Start” button
     * When I say “Send email to <EMAIL ADDRESS>”
     * Then I will see the “Send email to …” command in the prompt box
     * And “Email successfully sent” in the result box
     */
    /*
     * Scenario 2: User unsuccessfully sends drafted email
     * Given I have improperly configured my email settings
     * And I have created an email
     * And have that email selected in my prompt history
     * And I click the “Start” button
     * When I say “Send email to <EMAIL ADDRESS>”
     * Then I will see the “Send email to …” command in the prompt box
     * And an error message with the SMTP host in the result box
     */ 
    @Test
    void test_1_2_UserSendsEmail() throws InterruptedException {
    	String valid = "Send email to jill at ucsd dot edu";
    	String selectedEntry = "Create Email: To Jill, let's meet";
    	String toEmail = "jill@ucsd.edu";
    	String fromEmail = "julianaavalos4@gmail.com";
    	String sub = "New Message from first last on SayItAssistant2";
    	String body = "test body";
    	String firstName = "first";
    	String lastName = "last";
    	String smtpHost = "smtp.gmail.com";
    	String tlsPort = "587";
    	String password = "xpyymmmrjzojvrie";
    	    	
    	when(WhisperMock.getQuestionText()).thenReturn(valid);
    	when(qpMock.getQuestion()).thenReturn(selectedEntry);
    	when(MongoDBMock.getFirstName()).thenReturn(firstName);
    	when(MongoDBMock.getLastName()).thenReturn(lastName);
    	when(qpMock.getAnswer()).thenReturn(body);
    	when(MongoDBMock.getFromEmail()).thenReturn(fromEmail);
    	when(MongoDBMock.getSMTPHost()).thenReturn(smtpHost);
    	when(MongoDBMock.getTLSPort()).thenReturn(tlsPort);
    	when(MongoDBMock.getPassword()).thenReturn(password);
    	
    	//Start recording
        startButton.doClick();
        TimeUnit.SECONDS.sleep(1);
        //Stop recording
        startButton.doClick();
        
        verify(tlsMock).startEmailSession();
        verify(emailUtilMock).sendEmail(tlsMock.startEmailSession(), toEmail, fromEmail, sub, body);   	
    }
    
    /* 
     * Scenario 3: User unsuccessfully sends email they have not created
     * Given I have properly configured my email settings
     * And have not created an email
     * And I click the “Start” button
     * When I say “Send email to <EMAIL ADDRESS>”
     * Then an error message saying email draft must be selected pops up
     */ 
    @Test
    void test_3_EmailNotSelected() throws InterruptedException {
    	String valid = "Send email to jill at ucsd dot edu";
    	String selectedEntry = "Question: When is Christmas?";
    	String expectedErrorMessage = "Must select created email before sending email.";
    	    	
    	when(WhisperMock.getQuestionText()).thenReturn(valid);
    	when(qpMock.getQuestion()).thenReturn(selectedEntry);
    	
    	//Start recording
        startButton.doClick();
        TimeUnit.SECONDS.sleep(1);
        //Stop recording
        startButton.doClick();
        
        verify(ErrorMessagesMock).showErrorMessage(expectedErrorMessage); 
    }
    
    /*
     * Scenario 4: User tries to send email before configuring email settings
     * Given I haven't configured my email settings
     * And I have selected an email
     * And I click the “Start” button
     * When I say “Send email to <EMAIL ADDRESS>”
     * Then an error message saying email draft must be selected pops up
     */
    @Test
    void test_4_SettingsNotConfigured() throws InterruptedException {
    	String valid = "Send email to jill at ucsd dot edu";
    	String selectedEntry = "Create Email: To Jill, let's meet";
    	String expectedErrorMessage = "Use Setup Email command before sending emails.";
    	    	
    	when(WhisperMock.getQuestionText()).thenReturn(valid);
    	when(qpMock.getQuestion()).thenReturn(selectedEntry);
    	
    	//Start recording
        startButton.doClick();
        TimeUnit.SECONDS.sleep(1);
        //Stop recording
        startButton.doClick();
        
        verify(ErrorMessagesMock).showErrorMessage(expectedErrorMessage); 
    }
    

}