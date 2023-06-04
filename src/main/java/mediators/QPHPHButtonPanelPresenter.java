package mediators;
import java.util.ArrayList;

import api.Recorder;
import interfaces.*;
import mainframe.*;
import server.ServerCalls;

public class QPHPHButtonPanelPresenter implements ButtonObserver, PanelObserver{
    ArrayList<ButtonSubject> allButtons = new ArrayList<>();
    QuestionPanel qp;
    PromptHistory ph;
    Recorder recorder = new Recorder(null, null);


    public QPHPHButtonPanelPresenter(ArrayList<ButtonSubject> createdButtons, QuestionPanel createdqp, PromptHistory createdph){
        allButtons = createdButtons;
        qp = createdqp;
        ph = createdph;
        allButtons.forEach(button -> button.registerObserver(this));
        qp.registerObserver(this);
        ph.registerObserver(this);
    }
    
    //BUTTON OBSERVER METHODS
    @Override
    public void onStartStop(boolean startedRecording) {
        if (startedRecording){
            System.out.println("startedRecording");
            recorder.startRecording();
            qp.setRecordingLableVisible();
            qp.setStartButtonText("Stop Recording");
        }
        else {
            System.out.println("stoppedRecording");
            final ArrayList<String> qanda = recorder.stopRecording();
            final String question = qanda.get(0);
            final String answer = qanda.get(1);
            ph.addPH(question);
            qp.setQuestion(question);
            qp.setAnswer(answer);
            qp.setRecordingLableInvisible();
            qp.setStartButtonText("New Question");
            ServerCalls.postToServer(question, answer);
        }
    }

    @Override
    public void onListChange(String question, String answer) {
        System.out.println("listChanged");
        qp.setQuestion(question);
        qp.setAnswer(answer); 
        qp.setDeleteButtonVisible();
    }

    @Override
    public void onDelete() {
        System.out.println("deleted");
        final String question = qp.getQuestion();
        final int questionIndex = ph.getIndexInPH(question);
        System.out.println(questionIndex);
        ServerCalls.deleteFromServer(question);
        ph.removePH(questionIndex);
        qp.setQuestion("Your Question will appear here");
        qp.setAnswer("Your Answer will appear here");
        qp.setDeleteButtonInvisible();
    }

    @Override
    public void onClear(){
        System.out.println("cleared");
        for (int i = 0; i < ph.getPHSize(); i++) {
            final String question = ph.getElementInPH(i);
            ServerCalls.deleteFromServer(question);
        }
          //TODO Should be some kind of update method with ClearListener
        qp.setQuestion("Your Question will appear here");
        qp.setAnswer("Your Answer will appear here");
        qp.setDeleteButtonInvisible();
        ph.resetPH();
    }
    //_______________________________


/* save asked questions to text file

public void saveQuestions() {
  try {
    FileWriter questionFile = new FileWriter(filePath);
    
    for (int i = 0; i < ph.getPHSize(); i++) {
      
    questionFile.write(ph.getElementInPH(i));
    questionFile.write("\n");
      
    }
    questionFile.close();
  } catch (IOException e) {
      System.out.println("saveQuestions() failed");
  }	  
}


 /*
* load asked questions from text file
*//*
public void loadQuestions() {

  try {
    BufferedReader questionFile = new BufferedReader(new FileReader(filePath));
    String currLine;

    while ((currLine = questionFile.readLine()) != null) {      		
      String response = ServerCalls.getFromServer(currLine);
      // String question = response.substring(0,);
      // String answer = response.substring()
    }
    
    questionFile.close();
    
  } catch (IOException e){
    System.out.println("loadQuestions() failed");
  }
  
}*/
}
