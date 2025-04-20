
import java.util.Random;

public class Food {
    private Tile food;
    private Random random;
    

    public Food(int posX,int posY){
        this.food= new Tile(posX, posY);
        random= new Random();
    }

    public void createApple(){
        food.setPosX(random.nextInt(Variables.SIZE/Variables.TILE_SIZE));
        food.setPosY(random.nextInt(Variables.SIZE/Variables.TILE_SIZE));
    }

    public Tile getFood() {
        return food;
    }

    public void setFood(Tile food) {
        this.food = food;
    }
}
