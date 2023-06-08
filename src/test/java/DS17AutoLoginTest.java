import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import processing.ErrorMessages;
import processing.SavefileWriter;
import interfaces.ErrorMessagesInterface;
import interfaces.LoginButtonsSubject;
import interfaces.MongoInterface;
import listeners.CreateAccountListener;
import mainframe.*;
import mediators.*;
import api.MongoDB;

import java.util.ArrayList;

import javax.swing.JButton;


public class DS17AutoLoginTest {

    private static MongoInterface mongoDBMock;
    private static ErrorMessagesInterface errorMessagesMock ;
    private static SavefileWriter sfWriterMock;
    private static LoginPanel lpMock;
    private static AppFrame afMock ;
    private static LoginFrame lfMock ;
    private static QuestionPanel qpMock;
    private static PromptHistory phMock;

    private static JButton createButton;
    private static JButton loginButton;

	private static LoginMediator testLogic;

    @BeforeEach
	public void setUp(){
		ArrayList<LoginButtonsSubject> allButtons = new ArrayList<LoginButtonsSubject>();
		
        //Mock classes
        mongoDBMock = mock(MongoDB.class);
        errorMessagesMock = mock(ErrorMessages.class);
        sfWriterMock = mock(SavefileWriter.class);
        lfMock = mock(LoginFrame.class);
        afMock = mock(AppFrame.class);
        lpMock = mock(LoginPanel.class);
        qpMock = mock(QuestionPanel.class);
        phMock = mock(PromptHistory.class);

        //Mock Method calls
        when(afMock.getQuestionPanel()).thenReturn(qpMock);
        when(afMock.getPromptHistory()).thenReturn(phMock);
        when(lfMock.getLoginPanel()).thenReturn(lpMock);
        when(lpMock.getLoginButton()).thenReturn(loginButton = new JButton());
        when(lpMock.getcreateAccountButton()).thenReturn(createButton = new JButton());

		testLogic = new LoginMediator(lfMock, afMock, mongoDBMock,errorMessagesMock, sfWriterMock);
    }

    // Scenario 1: User agrees to automatic login
	// Given that I have been asked about automatic login
	// When I agree to trusting the device I’m on
	// Then I will be automatically logged in the next time I open the app

    @Test
    void test_1_autoLogin(){
        String testEmail = "Some Email";
        String testPassword = "Some Password";
        ArrayList<String> expectedSavedFile = new ArrayList<String>();
        expectedSavedFile.add(testEmail);
        expectedSavedFile.add(testPassword);

        when(sfWriterMock.getLoginInfo()).thenReturn(expectedSavedFile);
        testLogic.checkAutoLogin();
        verify(this.mongoDBMock).checkValidLogin(testEmail, testPassword);
        
    }

    // Scenario 2: User denies automatic login
	// Given that I have been asked about automatic login
	// When I don’t agree to trusting the device I’m on
	// Then I will not be automatically logged in the next time I open the app
	// And I will again be asked if I would like to trust the device 
    @Test
    void test_2_noAutoLogin(){
        String testEmail = "Some Email";
        String testPassword = "Some Password";
        ArrayList<String> expectedSavedFile = new ArrayList<String>();
        expectedSavedFile.add(testEmail);
        expectedSavedFile.add(testPassword);

        when(sfWriterMock.getLoginInfo()).thenReturn(null);
        testLogic.checkAutoLogin();
        verifyNoInteractions(this.mongoDBMock);
    }

    //  Scenario 3: User asked about automatic login upon account creation
    // 	Given that I am creating a new account
    // 	When I click “Create Account”
    // 	Then a pop-up should ask me if I would like to automatically login on the 
    //  device I am using in the future 
    @Test
    void test_3_CreateAccount(){
        String testEmail = "Some Email";
        String testPassword = "Some Password";
        ArrayList<String> expectedSavedFile = new ArrayList<String>();
        expectedSavedFile.add(testEmail);
        expectedSavedFile.add(testPassword);
        
        when(lpMock.getEmail()).thenReturn(testEmail);
        when(lpMock.getPass1()).thenReturn(testPassword);
        when(errorMessagesMock.confirmAutoLogin()).thenReturn(true);

        createButton.doClick();
        
    }

    //  Scenario 4: User asked about automatic login upon using new device
    // 	Given that I am accessing my account on a new device
    // 	When I click “Login”
    // 	Then a pop-up should ask me if I would like to automatically login on the 
    // device I am using in the future 

    @Test
    void test_4_logIn(){
        String testEmail = "Some Email";
        String testPassword = "Some Password";
        ArrayList<String> expectedSavedFile = new ArrayList<String>();
        expectedSavedFile.add(testEmail);
        expectedSavedFile.add(testPassword);
        
        when(lpMock.getEmail()).thenReturn(testEmail);
        when(lpMock.getPass1()).thenReturn(testPassword);
        when(mongoDBMock.checkValidLogin(testEmail,testPassword)).thenReturn(true);
        when(errorMessagesMock.confirmAutoLogin()).thenReturn(true);

        loginButton.doClick();
        verify(sfWriterMock).setLoginInfo(expectedSavedFile);
        
    }

}   
