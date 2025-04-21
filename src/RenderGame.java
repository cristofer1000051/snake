
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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class RenderGame extends JPanel implements ActionListener,KeyListener {

    private Snake snake_1,snake_2;
    Timer gameLoop;
    private int scoreMela;
    private int scoreBonus;
    private int scoreMalus;
    private int totalPoints;
    private boolean  status;
    private Food apple, star;
    private boolean gameOver;
    List<Food> listFood;
    List<Food> listDanger;
    Random random;
    private JFrame frame; 
    public RenderGame(JFrame frame) {
        this.status=false;
        this.frame = frame;
        listFood = new ArrayList<>();
        listDanger = new ArrayList<>();
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(Variables.SIZE, Variables.SIZE));
        setBackground(Color.BLACK);
        this.random= new Random();
        createSnake();
        createApple();
        createStar();
        createDanger();

        listFood.add(apple);
        listFood.add(star);
        listFood.addAll(listDanger);
        gameLoop = new Timer(100, this);
        gameLoop.start();
        addKeyListener(this);
        setFocusable(true); 
    }

    public void createSnake() {
        snake_1 = new Snake(5, 5, Color.GREEN,1);
        addKeyListener(snake_1);
        snake_2 = new Snake(5,15,Color.RED,2);
        addKeyListener(snake_2);

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
    public void createDanger(){
        int min=15;
        int randomNumber= random.nextInt(30 - min) + min;
        Timer timer3 = new Timer(10000, (ActionEvent e) -> {
            if (listDanger.size()>=randomNumber) {
                listDanger.clear();
            }
            for (int i=0 ;i<randomNumber;i++) {
                Food danger = new Food(30, 30, "Danger", -5);
                createFood(danger);
                listDanger.add(danger);
            }
            
        });
        timer3.start();
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
            for (Tile body: snake_1.getBodyTile() ) {
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
        /*
         for (int i = 0; i < Variables.SIZE / Variables.TILE_SIZE; i++) {
            //rect (x0,y0) -> (x1-y1)
            //Colonne
            g.drawLine(i * Variables.TILE_SIZE, 0, i * Variables.TILE_SIZE, Variables.SIZE);
            //file
            g.drawLine(0, i * Variables.TILE_SIZE, Variables.SIZE, i * Variables.TILE_SIZE);
        }
         */

        snake_1.createHead(g);
        snake_1.createBody(g);

        paintApple(g);
        if (star != null) {
            paintStar(g);
        }
        if (!listDanger.isEmpty()) {
            for (Food d : listDanger) {
                paintDanger(g,d);
            }
        }

        scorePaint(g);
    }

    public void eatFood() {
        //Cui mangiamo la mela
        if (collision(snake_1.getHead(), apple.getFood())) {
            scoreMela += apple.getPoints();
            snake_1.getBodyTile().add(apple.getFood());
            createApple();
        }
        //Cui mangiamo la stella
        if (star != null) {
            if (collision(snake_1.getHead(),star.getFood())) {
                scoreBonus += star.getPoints();
                this.star=null;
            }
        }
        //Se non e vuoto , possiamo mangiare anche il frutto avvelenato
        if (!listDanger.isEmpty()) {
            for(int i=0;i<listDanger.size();i++){
                if (collision(snake_1.getHead(), listDanger.get(i).getFood())) {
                    scoreMalus +=listDanger.get(i).getPoints(); 
                    listDanger.remove(i);
                    if (!snake_1.getBodyTile().isEmpty()) {
                        snake_1.getBodyTile().remove(snake_1.getBodyTile().size()-1);
                    }else{
                        gameOver=true;
                    }
                }
            }
        }
    }

    //Conteggio dei punti 
    public void scorePaint(Graphics g) {
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.setColor(Color.green);
        g.drawString("Mele Mangiate:" + scoreMela, Variables.TILE_SIZE * 25, Variables.TILE_SIZE);

        g.setColor(Color.YELLOW);
        g.drawString("Bonus Points:" + scoreBonus, Variables.TILE_SIZE * 15, Variables.TILE_SIZE);

        g.setColor(Color.WHITE);
        g.drawString("Total Points:" + totalPoints, Variables.TILE_SIZE * 5, Variables.TILE_SIZE);
    }

    public boolean collision(Tile start, Tile target) {
        return start.getPosX() == target.getPosX() && start.getPosY() == target.getPosY();
    }

    public void gameOverFunction() {
        for (Tile body : snake_1.getBodyTile()) {
            if (collision(snake_1.getHead(), body)) {
                this.gameOver = true;
            }
        }
        if (snake_1.getHead().getPosX() >= Variables.SIZE / Variables.TILE_SIZE
                || snake_1.getHead().getPosY() >= Variables.SIZE / Variables.TILE_SIZE
                || snake_1.getHead().getPosX() < 0 || snake_1.getHead().getPosY() < 0) {
            this.gameOver = true;
        }
        totalPoints= scoreMela + scoreBonus +scoreMalus;
        
        if (totalPoints>=50) {
            gameOver=true;
            status=true;
        }

        if (this.gameOver) {
            gameLoop.stop();
            if (this.frame !=null) {
                //Otteniamo il frame padre di questo pannello
                //Rimuoviamo il pannello attuale 
                frame.remove(this);
                GeneralScore generalScore = new GeneralScore(scoreMela, scoreBonus, scoreMalus,status);
                frame.invalidate();
                frame.add(generalScore);
                frame.revalidate();
                frame.repaint();
            }
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

    public void paintDanger(Graphics g,Food danger){
        g.setColor(Color.red);
        g.fillRect(danger.getFood().getPosX()*Variables.TILE_SIZE, danger.getFood().getPosY()*Variables.TILE_SIZE ,Variables.TILE_SIZE-2,Variables.TILE_SIZE-2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        snake_1.move();
        //Si chiama questa funzione prima di mangiare la frutta, così eviti di , avere la testa e il tile succesivo
        //con la stessa posizione
        gameOverFunction();
        eatFood();

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            gameLoop.stop();
        }else{
            gameLoop.start();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    
}
