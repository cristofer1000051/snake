
import java.util.Random;

public class Food {
    private Tile food;
    private String name;
    private Random random;
    private int points;

    public Food(int posX,int posY,String name,int points){
        this.food= new Tile(posX, posY);
        this.name=name;
        random= new Random();
        this.points=points;
    }

    public void create(){
        food.setPosX(random.nextInt(Variables.SIZE/Variables.TILE_SIZE));
        food.setPosY(random.nextInt(Variables.SIZE/Variables.TILE_SIZE));
    }



    public Tile getFood() {
        return food;
    }

    public void setFood(Tile food) {
        this.food = food;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }
}
