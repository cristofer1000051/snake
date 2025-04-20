public class Tile {
    private int posX;
    private int posY;

    public Tile(int posX,int posY){
        this.posX=posX;
        this.posY=posY;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void increasePosX(int posX){
        this.posX +=posX;
    }
    public void increasePosY(int posY){
        this.posY +=posY;
    }

}
