
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class RenderGame extends JPanel implements ActionListener{
    private Snake snake;
    Timer gameLoop;
    private int score;
    private Food apple;
    public RenderGame() {
        setPreferredSize(new Dimension(Variables.SIZE,Variables.SIZE));
        setBackground(Color.BLACK);

        
        createSnake();
        createApple();
        
        gameLoop = new Timer(100,this);
        gameLoop.start();
    }
    public final void createSnake(){
        snake = new Snake(5,5,Color.GREEN);
        addKeyListener(snake);
        setFocusable(true);
    }

    public final void createApple(){
        apple= new Food(10, 10);
        apple.createApple();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        for(int i=0;i < Variables.SIZE/Variables.TILE_SIZE;i++){
            //rect (x0,y0) -> (x1-y1)
            //Colonne
            g.drawLine(i*Variables.TILE_SIZE, 0,i*Variables.TILE_SIZE, Variables.SIZE);
            //file
            g.drawLine(0,i*Variables.TILE_SIZE, Variables.SIZE,i*Variables.TILE_SIZE);
        }
        
        snake.createHead(g);
        snake.createBody(g);
        createApple(g);
        score(g);
    }

    public void eatFood(){
        if(collision(snake.getHead(), apple.getFood())){
            snake.getBodyTile().add(apple.getFood());
            createApple();
        }
    }

    public void score(Graphics g){
        g.setFont(new Font("Arial",Font.PLAIN,16));
        g.setColor(Color.green);
        score=snake.getBodyTile().size();
        g.drawString("Mele Mangiate:"+score, Variables.TILE_SIZE*25, Variables.TILE_SIZE);
    }
    public boolean  collision(Tile snakeHead, Tile food){
        return snakeHead.getPosX() == food.getPosX() && snakeHead.getPosY() == food.getPosY();
    }
    public void createApple(Graphics g){
        ImageIcon icon = new ImageIcon(getClass().getResource("./resources/apple.png"));
        Image scaledImage = icon.getImage().getScaledInstance(Variables.TILE_SIZE-2,Variables.TILE_SIZE-2, Image.SCALE_SMOOTH);

        //L'immagine ridimensionata viene aggiunta a mediatracker
        MediaTracker tracker = new MediaTracker(new Container());
        tracker.addImage(scaledImage, 0);
        //Questo pezzo di codice attende che ttute le immagini aggiunte al mediaTracker siano
        //Siano completamente caricate
        try {
            //permete bloccare l'esecuzione del programma fino quando tutte le risorse siano
            //
            tracker.waitForAll();
        } catch (InterruptedException e) {
            throw new RuntimeException ("Image loading interrupted",e);
        }
        g.drawImage(scaledImage, apple.getFood().getPosX()*Variables.TILE_SIZE, apple.getFood().getPosY()*Variables.TILE_SIZE, null);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        snake.move();
        eatFood();
        repaint();
    }
}
