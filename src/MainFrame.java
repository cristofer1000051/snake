
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainFrame {

    final Font mainFont = new Font("Segoe print", Font.BOLD, 18);

    private JFrame frame;

    public MainFrame() {
        
    }

    public void initialize() {
        this.frame = new JFrame("Menu");
       
        frame.setSize(Variables.SIZE, Variables.SIZE);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblTitle = new JLabel("SNAKE");
        lblTitle.setFont(new Font("Segoe print", Font.BOLD, 52));
        lblTitle.setForeground(Color.WHITE);
        
        JLabel author = new JLabel("Made By Cristofer");
        author.setFont(new Font("Segoe print", Font.BOLD, 12));
        author.setForeground(Color.WHITE);

        JButton button = new JButton("START!");
        button.setPreferredSize(new Dimension(120, 50));

        Image image = requestImage();

        JPanel contentPanel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };
        contentPanel.setLayout(new GridBagLayout());
        //contentPanel.setBackground(new Color(128,128,255));

        //Usato per centrare i componenti
        GridBagConstraints constraints = new GridBagConstraints();
        //Specifichiamo che il compente sia posizionato nel centro della cella
        constraints.anchor = GridBagConstraints.CENTER;
        //Indichiamo che il componente non deva riempire tutta la cella
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 1;
        constraints.weighty = 1;

        constraints.gridy = 0;
        contentPanel.add(lblTitle, constraints);
        constraints.gridy = 1;
        contentPanel.add(button, constraints);
        constraints.gridy=2;
        contentPanel.add(author,constraints);
        frame.add(contentPanel);

        button.addActionListener((ActionEvent e) -> {
            RenderGame rendering = new RenderGame(this.frame);
            frame.remove(contentPanel);
            frame.invalidate();
            frame.add(rendering);
            frame.validate();
            //Questo ti consente di avere le dimensione precissa del panello
            frame.pack();
            rendering.requestFocus();
        });
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private Image requestImage() {
        Image image = null;
        try {
            image = new ImageIcon(getClass().getResource("./resources/background.png")).getImage();
        } catch (Error e) {
            e.printStackTrace();
        }
        return image;
    }

}
