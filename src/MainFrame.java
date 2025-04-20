import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainFrame {
    final Font mainFont = new Font("Segoe print",Font.BOLD,18);

    private RenderGame rendering;

    public MainFrame(){
        this.rendering = new RenderGame();
    }

    public void initialize(){
        JFrame frame = new JFrame("Menu");
        frame.setVisible(true);
        frame.setSize(Variables.SIZE,Variables.SIZE);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        JLabel lblTitle = new JLabel("SNAKE");
        lblTitle.setFont(new Font("Segoe print",Font.BOLD,52));

        
        JButton button = new JButton("START!");
        button.setPreferredSize(new Dimension(120,50));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(new Color(128,128,255));

        //Usato per centrare i componenti
        GridBagConstraints constraints = new GridBagConstraints();
        //Specifichiamo che il compente sia posizionato nel centro della cella
        constraints.anchor = GridBagConstraints.CENTER;
        //Indichiamo che il componente non deva riempire tutta la cella
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 1;
        constraints.weighty = 1;
        
        constraints.gridy=0;
        contentPanel.add(lblTitle,constraints);
        constraints.gridy=1;
        contentPanel.add(button,constraints);
        frame.add(contentPanel);
        
        button.addActionListener((ActionEvent e) -> {
            frame.remove(contentPanel);
            frame.invalidate();
            frame.add(rendering);
            frame.validate(); 
            //Questo ti consente di avere le dimensione precissa del panello
            frame.pack();
            rendering.requestFocus();
        });
        
        frame.setLocationRelativeTo(null);
    }
}
