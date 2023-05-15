package mock;

import main.PromptHistory;

public class MockPromptHistory extends PromptHistory {
    public MockPromptHistory(){
        super();
        PromptHistory.filePath = "src/mock/questionFile.txt";
        System.out.println("I got here");

    }

    @Override
    public void loadQuestions() {
        listPH.addElement("mock question 0" + "/n" + "mock question 1" + "/n" + "mock question 2");
    }
    // @Override
    // public void addListeners() {
    //     for (int i = 0; i < listPH.getSize(); i++) {
    //         try {
    //             String question = (String)listPH.getElementAt(i);
    //             question = question.replace(' ', '+');
    //             URL url = new URL(URL + "?=" + question);
    //             HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    //             conn.setRequestMethod("DELETE");
    //             BufferedReader in = new BufferedReader(
    //               new InputStreamReader(conn.getInputStream())
    //             );
    //             String response = in.readLine();
    //             in.close();
    //             JOptionPane.showMessageDialog(null, response);
    //           } catch (Exception ex) {
    //             ex.printStackTrace();
    //             JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
    //           }
    //     }
    //     listPH.clear();
    //     saveQuestions();
    // }
}
