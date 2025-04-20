import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JPanel;

public class GeneralScore extends JPanel{
    private int mela;
    private int bonus;
    private int malus;
    private int totalPoints;

    public GeneralScore(int mela,int bonus,int malus){
        setBackground(Color.GRAY);
        setPreferredSize(new Dimension(300, 300));
        JButton button = new JButton("Giocare di nuovo?");
        add(button,BorderLayout.CENTER);
    }
    public void generateLabelScore(){
        
    }
}
