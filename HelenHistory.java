import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;


class QuestionPanel extends JPanel{
    private JLabel title;
    private JTextArea currQuestion;
    private JTextArea currAnswer;
    private JLabel recordingLabel;
    private JButton askQuestion;

    String ph = "Placeholder";

    LayoutManager qpLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);

    private void configTitle(){
        title = new JLabel("Team 12 App", SwingConstants.CENTER);
        title.setFont(new Font("Sans-serif", Font.BOLD, 30));
        title.setAlignmentX(CENTER_ALIGNMENT);
        
    }
    private void configcurrQuestion(){
        this.currQuestion = new JTextArea(ph);
        currQuestion.setEditable(false);
        currQuestion.setLineWrap(true);
        currQuestion.setAlignmentX(CENTER_ALIGNMENT);
        currQuestion.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
        currQuestion.getBorder()));
        currQuestion.setPreferredSize(new Dimension(600,200));
        currQuestion.setMaximumSize(new Dimension(600,200));
        currQuestion.setFont(new Font("Sans-serif", Font.BOLD, 18));
    }

    private void configcurrAnswer(){
        this.currAnswer = new JTextArea(ph);
        currAnswer.setEditable(false);
        currAnswer.setLineWrap(true);
        currAnswer.setAlignmentX(CENTER_ALIGNMENT);
        currAnswer.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
        currAnswer.getBorder()));
        currAnswer.setPreferredSize(new Dimension(600,700));
        currAnswer.setMaximumSize(new Dimension(600,700));
        currAnswer.setFont(new Font("Sans-serif", Font.PLAIN, 14));
    }

    private void configaskQuestion(){
        askQuestion = new JButton("New Question"); 
        askQuestion.setFont(new Font("Sans-serif", Font.PLAIN, 24));
        askQuestion.setAlignmentX(CENTER_ALIGNMENT);
    }

    private void configrecordingLabel(){
        recordingLabel = new JLabel(ph, SwingConstants.CENTER);
        recordingLabel.setPreferredSize(new Dimension(200,40));
        recordingLabel.setAlignmentX(CENTER_ALIGNMENT);
    }

    QuestionPanel(){
        this.setBackground(Color.green); // set background color of task
        this.setLayout(qpLayout);

        configTitle();
        configcurrQuestion();
        configcurrAnswer();
        configaskQuestion();
        configrecordingLabel();

        this.add(title);
        this.add(currQuestion);
        this.add(currAnswer);
        this.add(Box.createVerticalGlue());
        this.add(recordingLabel);
        this.add(askQuestion);
        this.add(Box.createVerticalGlue());
        this.add(Box.createRigidArea(new Dimension(100,20)));
    }
    

    // public JButton getAskButton() {
    //     return askQuestion;
    // }
}

class PromptHistory extends JPanel{
    private JLabel header;
    private JScrollPane pastResults;
    String[] listPH = new String[200];


    LayoutManager phLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
    
    private void configheader(){
        header = new JLabel("Prompt History"); 
        header.setFont(new Font("Sans-serif", Font.BOLD, 40));
        header.setAlignmentX(CENTER_ALIGNMENT);
    }

    private void configpastResults(){
        JList<String> list;
        for (int i = 0; i < 199; i++){
            listPH[i] = "placeholder";
        }
        
        list = new JList<String>(listPH);
        pastResults = new JScrollPane(list);
        // pastResults.setMinimumSize(new Dimension(400, 800));
        pastResults.setPreferredSize(new Dimension(400, 800));
        // pastResults.setMaximumSize(new Dimension(400, 800));

    }
    
    PromptHistory(){
        this.setBackground(Color.cyan);
        this.setLayout(phLayout);
        this.setPreferredSize(new Dimension(400,1000));

        configheader();
        configpastResults();

        this.add(header);
        this.add(pastResults);
    }
}


class AppFrame extends JFrame{
    private QuestionPanel qp;
    private PromptHistory ph;
    LayoutManager afLayout = new BorderLayout();

    // private JButton newQuestionButton;

    AppFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 1000); 
        this.setLayout(afLayout);
        this.setVisible(true);
    
        // historypanel = new PromptHistory();
        qp = new QuestionPanel();
        ph = new PromptHistory();
    
        this.add(qp, BorderLayout.CENTER); 
        this.add(ph, BorderLayout.WEST); 

        // newQuestionButton = questionpanel.footer.getAskButton();
        // clearButton = footer.getClearButton();
        // loadButton = footer.getLoadButton();
        // saveButton = footer.getSaveButton();
        revalidate();
    }
}



public class HelenHistory {
    
    public static void main (String args[]){
        new AppFrame();
    }  
}