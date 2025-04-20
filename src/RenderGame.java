
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class RenderGame extends JPanel implements ActionListener {

    private Snake snake;
    Timer gameLoop;
    private int scoreMela;
    private int scoreBonus;
    private int scoreMalus;

    private Food apple, star;
    private boolean gameOver;
    List<Food> listFood;

    public RenderGame() {
        listFood = new ArrayList<>();
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(Variables.SIZE, Variables.SIZE));
        setBackground(Color.BLACK);

        createSnake();
        createApple();
        createStar();

        listFood.add(apple);
        listFood.add(star);

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    public void createSnake() {
        snake = new Snake(5, 5, Color.GREEN);
        addKeyListener(snake);
        setFocusable(true);
    }

    public void createApple() {
        apple = new Food(10, 10, "apple", 1);
        createFood(apple);
    }

    public void createStar() {
        Timer timer2 = new Timer(10000, (ActionEvent e) -> {
            star = new Food(30, 30, "star", 10);
            createFood(star);
        });

        timer2.start();
    }

    //In questo caso, creiamo una funzione che chiamerà il metodo di tipo food
    //e verifica che non abbiano la stessa posizione nella griglia
    //In caso contrario, si chimerà in modo ricorsivo
    public void createFood(Food food) {
        food.create();
        if (!listFood.isEmpty()) {
            for (Food f : listFood) {
                if (f != null) {
                    if (collision(f.getFood(),food.getFood())) {
                        createFood(food);
                    }
                }
            }
            for (Tile body: snake.getBodyTile() ) {
                if (collision(body,food.getFood())) {
                    createFood(food);
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        for (int i = 0; i < Variables.SIZE / Variables.TILE_SIZE; i++) {
            //rect (x0,y0) -> (x1-y1)
            //Colonne
            g.drawLine(i * Variables.TILE_SIZE, 0, i * Variables.TILE_SIZE, Variables.SIZE);
            //file
            g.drawLine(0, i * Variables.TILE_SIZE, Variables.SIZE, i * Variables.TILE_SIZE);
        }

        snake.createHead(g);
        snake.createBody(g);

        paintApple(g);
        if (star != null) {
            paintStar(g);
        }

        scorePaint(g);
    }

    public void eatFood() {
        if (collision(snake.getHead(), apple.getFood())) {
            scoreMela += apple.getPoints();
            snake.getBodyTile().add(apple.getFood());
            createApple();
        }
        if (star != null) {
            if (collision(snake.getHead(),star.getFood())) {
                scoreBonus += star.getPoints();
                snake.getBodyTile().remove(snake.getBodyTile().size()-1);
                this.star=null;
            }
        }
    }

    public void scorePaint(Graphics g) {
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.setColor(Color.green);
        g.drawString("Mele Mangiate:" + scoreMela, Variables.TILE_SIZE * 25, Variables.TILE_SIZE);

        g.setColor(Color.YELLOW);
        g.drawString("Bonus Points:" + scoreBonus, Variables.TILE_SIZE * 15, Variables.TILE_SIZE);
    }

    public boolean collision(Tile start, Tile target) {
        return start.getPosX() == target.getPosX() && start.getPosY() == target.getPosY();
    }

    public void gameOverFunction() {
        for (Tile snakeTile : snake.getBodyTile()) {
            if (collision(snake.getHead(), snakeTile)) {
                this.gameOver = true;
            }
        }
        if (snake.getHead().getPosX() >= Variables.SIZE / Variables.TILE_SIZE
                || snake.getHead().getPosY() >= Variables.SIZE / Variables.TILE_SIZE
                || snake.getHead().getPosX() < 0 || snake.getHead().getPosY() < 0) {
            this.gameOver = true;
        }
        if (this.gameOver) {
            gameLoop.stop();

            //Otteniamo il frame padre di questo pannello
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            //Rimuoviamo il pannello attuale 
            frame.remove(this);
            GeneralScore generalScore = new GeneralScore(scoreMela, scoreBonus, scoreMalus);
            frame.add(generalScore);
            frame.revalidate();
            frame.repaint();
        }
    }

    public void paintApple(Graphics g) {
        ImageIcon icon = new ImageIcon(getClass().getResource("./resources/apple.png"));
        Image scaledImage = icon.getImage().getScaledInstance(Variables.TILE_SIZE - 2, Variables.TILE_SIZE - 2, Image.SCALE_SMOOTH);

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
            throw new RuntimeException("Image loading interrupted", e);
        }
        g.drawImage(scaledImage, apple.getFood().getPosX() * Variables.TILE_SIZE, apple.getFood().getPosY() * Variables.TILE_SIZE, null);

    }

    public void paintStar(Graphics g) {
        ImageIcon icon = new ImageIcon(getClass().getResource("./resources/star.png"));
        Image scaledImage = icon.getImage().getScaledInstance(Variables.TILE_SIZE - 2, Variables.TILE_SIZE - 2, Image.SCALE_SMOOTH);

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
            throw new RuntimeException("Image loading interrupted", e);
        }
        g.drawImage(scaledImage, star.getFood().getPosX() * Variables.TILE_SIZE, star.getFood().getPosY() * Variables.TILE_SIZE, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        snake.move();
        //Si chiama questa funzione prima di mangiare la frutta, così eviti di , avere la testa e il tile succesivo
        //con la stessa posizione
        gameOverFunction();
        eatFood();

        repaint();
    }
}
