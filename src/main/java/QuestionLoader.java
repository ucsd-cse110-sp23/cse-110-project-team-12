import java.io.*;

public class QuestionLoader{
    private static String filePath = "bin/main/questionFile.txt";
/*
* save asked questions to text file
*/
  public static void saveQuestions() {
    try {
      FileWriter questionFile = new FileWriter(filePath);
      
      for (int i = 0; i < PromptHistory.getPHSize(); i++) {
        
      questionFile.write(PromptHistory.getElementInPH(i));
      questionFile.write("\n");
        
      }
      questionFile.close();
    } catch (IOException e) {
        System.out.println("saveQuestions() failed");
    }	  
  }


   /*
  * load asked questions from text file
  */
  public static void loadQuestions() {
  
    try {
      BufferedReader questionFile = new BufferedReader(new FileReader(filePath));
      String currLine;

      while ((currLine = questionFile.readLine()) != null) {      		
        PromptHistory.addPH(currLine);
      }
      
      questionFile.close();
      
    } catch (IOException e){
      System.out.println("loadQuestions() failed");
    }
    
  }
}