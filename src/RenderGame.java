
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
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

public class RenderGame extends JPanel implements ActionListener, KeyListener {

    private Snake snake_1, snake_2;
    Timer gameLoop;

    private Food apple, star;
    private boolean gameOver;
    List<Food> listFood;
    List<Food> listApple;
    Random random;
    private JFrame frame;
    Image imageApple, imageStar;

    public RenderGame(JFrame frame) {
        this.frame = frame;
        listFood = new ArrayList<>();
        listApple = new ArrayList<>();
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(Variables.SIZE, Variables.SIZE));
        setBackground(Color.BLACK);
        this.random = new Random();
        createSnake();
        createApple();
        createStar();
        insertImage();

        listFood.addAll(listApple);
        listFood.add(star);
        gameLoop = new Timer(100, this);
        gameLoop.start();
        addKeyListener(this);
        setFocusable(true);
    }

    public void createSnake() {
        snake_1 = new Snake(5, 5, Color.GREEN, 1, "SNAKE 1");
        addKeyListener(snake_1);

        snake_2 = new Snake(5, 15, Color.BLUE, 2, "SNAKE 2");
        addKeyListener(snake_2);

    }

    public void createApple() {
        apple = new Food(10, 10, "apple", 1, 0);
        listApple.add(apple);
        createFood(apple);
        createApples();
    }

    public void createStar() {
        Timer timer2 = new Timer(3000, (ActionEvent e) -> {
            star = new Food(30, 30, "star", 1, 3);
            createFood(star);
        });
        timer2.start();
    }

    public void createApples() {
        int min = 15;
        int randomNumber = random.nextInt(30 - min) + min;
        if (listApple.size() < randomNumber) {
            for (int i = 0; i < 30; i++) {
                Food ap = new Food(30, 30, "apple", 1, 0);
                createFood(ap);
                listApple.add(ap);
            }
        }
        Timer timer3 = new Timer(3000, (ActionEvent e) -> {
            if (listApple.size() <= 30) {
                Food ap = new Food(30, 30, "apple", 1, 0);
                createFood(ap);
                listApple.add(ap);
            }
        });
        timer3.start();
    }

    //In questo caso, creiamo una funzione che chiamerà il metodo di tipo food
    //e verifica che non abbiano la stessa posizione nella griglia
    //In caso contrario, si chiamerà in modo ricorsivo
    public void createFood(Food food) {
        food.create();
        if (!listFood.isEmpty()) {
            for (Food f : listFood) {
                if (f != null) {
                    if (collision(f.getFood(), food.getFood())) {
                        createFood(food);
                    }
                }
            }
            for (Tile body : snake_1.getBodyTile()) {
                if (collision(body, food.getFood())) {
                    createFood(food);
                }
            }
            for (Tile body : snake_2.getBodyTile()) {
                if (collision(body, food.getFood())) {
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

        snake_2.createHead(g);
        snake_2.createBody(g);

        paintApple(g);
        if (star != null) {
            paintStar(g);
        }
        scorePaint(g, snake_1, 1);
        scorePaint(g, snake_2, 2);
    }

    public void eatFood(Snake snake) {
        //Cui mangiamo la mela
        for (int i = 0; i < listApple.size(); i++) {
            if (collision(snake.getHead(), listApple.get(i).getFood())) {
                snake.addScoreMela(listApple.get(i).getPoints());
                snake.getBodyTile().add(listApple.get(i).getFood());
                listApple.remove(i);
                
            }
        }

    }

    public void eatStar(Snake snake1, Snake snake2) {
        //Cui mangiamo la stella
        if (star != null) {

            if (collision(snake1.getHead(), star.getFood())) {
                snake1.addScoreBonus(star.getPoints());
                snake2.subMela(star.getNegativePoints());
                star = null;
            } else if (collision(snake2.getHead(), star.getFood())) {
                snake2.addScoreBonus(star.getPoints());
                snake1.subMela(star.getNegativePoints());
                star = null;
            }

        }
    }

    //Conteggio dei punti 
    public void scorePaint(Graphics g, Snake snake, int Yposition) {
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.setColor(Color.CYAN);
        g.drawString("" + snake.getName(), Variables.TILE_SIZE, Variables.TILE_SIZE * Yposition);

        g.setColor(Color.green);
        g.drawString("Mele Mangiate:" + snake.getScoreMela(), Variables.TILE_SIZE * 25, Variables.TILE_SIZE * Yposition);

        g.setColor(Color.YELLOW);
        g.drawString("Bonus Points:" + snake.getScoreBonus(), Variables.TILE_SIZE * 15, Variables.TILE_SIZE * Yposition);

        g.setColor(Color.WHITE);
        g.drawString("Total Points:" + snake.getTotalPoints(), Variables.TILE_SIZE * 5, Variables.TILE_SIZE * Yposition);
    }

    public boolean collision(Tile start, Tile target) {
        return start.getPosX() == target.getPosX() && start.getPosY() == target.getPosY();
    }

    public void collisionSnakes() {
        for (Tile tile : snake_1.getBodyTile()) {
            if (collision(snake_2.getHead(), tile)) {
                convertSnakeToApple(snake_2);
                resetPositionSnake2();
            }
        }
        for (Tile tile : snake_2.getBodyTile()) {
            if (collision(snake_1.getHead(), tile)) {
                convertSnakeToApple(snake_1);
                resetPositionSnake1();
            }
        }
        
        if(checkSelfCollition(snake_1)){
            resetPositionSnake1();
        }else if (checkSelfCollition(snake_2)) {
            resetPositionSnake2();
        }
    }
    public void checkHeadCollition(){
        if (collision(snake_1.getHead(), snake_2.getHead())) {
            resetPositionSnake1();
            resetPositionSnake2();
            gameLoop.stop();
        }
    }
    public boolean checkSelfCollition(Snake snake){
        for(Tile tile:snake.getBodyTile()){
            if(collision(snake.getHead(), tile)){
                return true;
            }
        }
        return false;
    }
    public void convertSnakeToApple(Snake snake) {
        for (Tile t : snake.getBodyTile()) {
            apple = new Food(t.getPosX(), t.getPosY(), "apple", 1, 0);
            listApple.add(apple);
        }
        
    }

    public void resetPositionSnake1() {
        snake_1.getHead().setPosX(5);
        snake_1.getHead().setPosY(5);
        snake_1.setBodyTile(new ArrayList<>());
        snake_1.initializeBody(3);
        snake_1.setScoreMela(0);
    }

    public void resetPositionSnake2() {
        snake_2.getHead().setPosX(5);
        snake_2.getHead().setPosY(15);
        snake_2.setBodyTile(new ArrayList<>());
        snake_2.initializeBody(3);
        snake_2.setScoreMela(0);
    }

    public void gameOver(Snake snake) {
       

        snake.setTotalPoints(snake.getScoreMela() + snake.getScoreBonus());

        if (snake.getTotalPoints() >= 10) {
            gameOver = true;
            snake.setStatus(true);
        }

        if (this.gameOver) {
            gameLoop.stop();
            if (this.frame != null) {
                //Otteniamo il frame padre di questo pannello
                //Rimuoviamo il pannello attuale 
                frame.remove(this);
                GeneralScore generalScore = new GeneralScore(snake_1, snake_2);
                frame.invalidate();
                frame.add(generalScore);
                frame.revalidate();
                frame.repaint();
            }
        }
    }

    public boolean detectLimits(Snake snake) {
        return snake.getHead().getPosX() >= Variables.SIZE / Variables.TILE_SIZE
                || snake.getHead().getPosY() >= Variables.SIZE / Variables.TILE_SIZE
                || snake.getHead().getPosX() < 0 || snake.getHead().getPosY() < 0;

    }

    public void checkLimits(){
        if (detectLimits(snake_1)) {
            resetPositionSnake1();
            
        }
        if (detectLimits(snake_2)) {
            resetPositionSnake2();
        }
    }
    public void insertImage() {
        try {
            imageApple = new ImageIcon(getClass().getResource("./resources/apple.png")).getImage();
            imageApple = imageApple.getScaledInstance(Variables.TILE_SIZE - 2, Variables.TILE_SIZE - 2, Image.SCALE_SMOOTH);

            imageStar = new ImageIcon(getClass().getResource("./resources/star.png")).getImage();
            imageStar = imageStar.getScaledInstance(Variables.TILE_SIZE - 2, Variables.TILE_SIZE - 2, Image.SCALE_SMOOTH);
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    public void paintApple(Graphics g) {
        for (Food ap : listApple) {
            g.drawImage(imageApple, ap.getFood().getPosX() * Variables.TILE_SIZE, ap.getFood().getPosY() * Variables.TILE_SIZE, null);
        }
    }

    public void paintStar(Graphics g) {
        g.drawImage(imageStar, star.getFood().getPosX() * Variables.TILE_SIZE, star.getFood().getPosY() * Variables.TILE_SIZE, null);
    }

    public void paintDanger(Graphics g, Food danger) {
        g.setColor(Color.red);
        g.fillRect(danger.getFood().getPosX() * Variables.TILE_SIZE, danger.getFood().getPosY() * Variables.TILE_SIZE, Variables.TILE_SIZE - 2, Variables.TILE_SIZE - 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        snake_1.move();
        snake_2.move();
        //Si chiama questa funzione prima di mangiare la frutta, così eviti di , avere la testa e il tile succesivo
        //con la stessa posizione



        checkLimits();
        eatStar(snake_1, snake_2);
        collisionSnakes();
        eatFood(snake_1);
        eatFood(snake_2);

        
        
        gameOver(snake_1);
        gameOver(snake_2);
        checkHeadCollition();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            gameLoop.stop();
        } else {
            gameLoop.start();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
