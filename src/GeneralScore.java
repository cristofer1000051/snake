import java.awt.BorderLayout;
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


    private Snake snake1,snake2;
    public GeneralScore(Snake snake1,Snake snake2){
        this.snake1=snake1;
        this.snake2=snake2;
        setPreferredSize(new Dimension(300, 300));
        setLayout(new BorderLayout());

        generateLabelScore();
    }
    public final void generateLabelScore(){
        Font font = new Font("Segoe print",Font.BOLD,24);
        JPanel pannelloGrid = new JPanel(new GridBagLayout());
        pannelloGrid.setBackground(Color.gray);
        GridBagConstraints constraints = new GridBagConstraints();
        JPanel pannello= new JPanel(new BorderLayout());

        constraints.anchor = GridBagConstraints.CENTER;
        //Indichiamo che il componente non deva riempire tutta la cella
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.5;
        constraints.weighty = 1;
        
        Font fontS = new Font("Roboto",Font.BOLD,24);
        JLabel vin = new JLabel("vincitore");
        vin.setForeground(Color.YELLOW);
        vin.setFont(fontS);
        JLabel per = new JLabel("Perdente");
        per.setForeground(Color.BLUE);
        per.setFont(fontS);
        
        

        JLabel lbl1 = new JLabel("SNAKE 1: ");
        lbl1.setFont(font);
        JLabel lblMela = new JLabel("MELE: " + snake1.getScoreMela());
        lblMela.setFont(font);
        JLabel lblBonus = new JLabel("STELLE: "+snake1.getScoreBonus());
        lblBonus.setFont(font);
        JLabel lblTotal = new JLabel("TOTAL POINTS: "+ snake1.getTotalPoints());
        lblTotal.setFont(font);

        JLabel lbl2 = new JLabel("SNAKE 2: ");
        lbl2.setFont(font);
        JLabel lblMela2 = new JLabel("MELE: " + snake2.getScoreMela());
        lblMela2.setFont(font);
        JLabel lblBonus2 = new JLabel("STELLE: "+snake2.getScoreBonus());
        lblBonus2.setFont(font);
        JLabel lblTotal2 = new JLabel("TOTAL POINTS: "+ snake2.getTotalPoints());
        lblTotal2.setFont(font);
        

        JButton button = new JButton("Giocare di nuovo");
        button.setPreferredSize(new Dimension(120, 50));
        
        constraints.gridy=0;
        constraints.gridx=0;
        pannelloGrid.add(lbl1,constraints);
        constraints.gridx=0;
        constraints.gridy=1;
        pannelloGrid.add(lblMela,constraints);
        constraints.gridx=0;
        constraints.gridy=2;
        pannelloGrid.add(lblBonus,constraints);
        constraints.gridx=0;
        constraints.gridy=3;
        pannelloGrid.add(lblTotal,constraints);
        constraints.gridx=0;
        constraints.gridy=4;
        if (snake1.isStatus()) {
            pannelloGrid.add(vin,constraints);
        }else{
            pannelloGrid.add(per,constraints);
        }

        constraints.gridx=1;
        constraints.gridy=0;
        pannelloGrid.add(lbl2,constraints);
        constraints.gridx=1;
        constraints.gridy=1;
        pannelloGrid.add(lblMela2,constraints);
        constraints.gridx=1;
        constraints.gridy=2;
        pannelloGrid.add(lblBonus2,constraints);
        constraints.gridx=1;
        constraints.gridy=3;
        pannelloGrid.add(lblTotal2,constraints);
        constraints.gridx=1;
        constraints.gridy=4;
        if (snake2.isStatus()) {
            pannelloGrid.add(vin,constraints);
        }else{
            pannelloGrid.add(per,constraints);
        }

        pannello.add(pannelloGrid,BorderLayout.CENTER);
        pannello.add(button,BorderLayout.SOUTH);
        this.add(pannello,BorderLayout.CENTER);

        button.addActionListener((actionEvent) -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.dispose();
            MainFrame mainFrame= new MainFrame();
            mainFrame.initialize();
        });
    }
}
