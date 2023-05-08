import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

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