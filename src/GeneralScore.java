import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GeneralScore extends JPanel{
    private int apples;
    private int bonus;
    private int malus;
    private int totalPoints;

    public GeneralScore(int apples,int bonus,int malus){
        this.apples = apples;
        this.bonus = bonus;
        this.malus = malus;
        this.totalPoints = apples + bonus + malus;
        setBackground(Color.GRAY);
        setPreferredSize(new Dimension(300, 300));
        setLayout(new GridBagLayout());

        generateLabelScore();
    }
    public final void generateLabelScore(){
        Font font = new Font("Segoe print",Font.BOLD,24);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        //Indichiamo che il componente non deva riempire tutta la cella
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 1;
        constraints.weighty = 1;

        JLabel lblMela = new JLabel("MELE: " + apples);
        lblMela.setFont(font);
        JLabel lblBonus = new JLabel("BONUS: "+bonus);
        lblBonus.setFont(font);
        JLabel lblMalus = new JLabel("MALUS: "+malus);
        lblMalus.setFont(font);
        JLabel lblTotal = new JLabel("TOTAL POINTS: "+ totalPoints);
        lblTotal.setFont(font);

        JButton button = new JButton("Giocare di nuovo");
        button.setPreferredSize(new Dimension(240,50));

        
        constraints.gridy=0;
        add(lblMela,constraints);
        constraints.gridy=1;
        add(lblBonus,constraints);
        constraints.gridy=2;
        add(lblMalus,constraints);
        constraints.gridy=3;
        add(lblTotal,constraints);
        constraints.gridy=4;
        add(button,constraints);

        button.addActionListener((actionEvent) -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.dispose();
            MainFrame mainFrame= new MainFrame();
            mainFrame.initialize();
        });
    }
}
