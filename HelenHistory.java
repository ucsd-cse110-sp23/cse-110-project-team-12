import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JEditorPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

class PromptHistory extends JPanel{
    private JPanel header; //Title
    private JList listPrevResults;
    

}

class QuestionPanel extends JPanel{
    
    Footer footer;

    Border emptyBorder = BorderFactory.createEmptyBorder();

    Color gray = new Color(218, 229, 234);

    QuestionPanel(){
        this.setPreferredSize(new Dimension(800, 800)); // set size of task
        this.setBackground(gray); // set background color of task
        this.setLayout(new BorderLayout()); // set layout of task

        footer = new Footer();
        this.add(footer, BorderLayout.CENTER);
    }
}

class CurrentQuestion extends JEditorPane{

}

class CurrentAnswer extends JEditorPane{

}

class Footer extends JPanel{
    private JLabel recordingLabel; //Can be microphone in later iteration?
    private JButton AskNewQuestion;

    Color gray = new Color(218, 229, 234);

    Footer(){
        this.setPreferredSize(new Dimension(800, 800)); // set size of task
        this.setBackground(gray); // set background color of task
        this.setLayout(new BorderLayout());
        

        AskNewQuestion = new JButton("New Question"); // clear button
        AskNewQuestion.setFont(new Font("Sans-serif", Font.ITALIC, 10)); // set font
        this.add(AskNewQuestion); // add to footer
    }
    

    public JButton getAskButton() {
        return AskNewQuestion;
    }
}

class AppFrame extends JFrame{
    private PromptHistory historypanel;
    private QuestionPanel questionpanel;

    private JButton newQuestionButton;

    AppFrame() {
        this.setSize(800, 1000); // 400 width and 600 height
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close on exit
        this.setVisible(true); // Make visible
    
        // historypanel = new PromptHistory();
        questionpanel = new QuestionPanel();
    
        this.add(historypanel, BorderLayout.WEST); // Add title bar on top of the screen
        // this.add(questionpanel, BorderLayout.EAST); // Add footer on bottom of the screen

        newQuestionButton = questionpanel.footer.getAskButton();
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