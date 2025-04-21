
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class Snake implements KeyListener{
    private Tile head;
    private List<Tile> bodyTile;
    private int dirX;
    private int dirY;
    private Color color;
    private int players;
    
    public Snake(int posX,int posY,Color color,int players){
        this.head= new Tile(posX, posY);
        this.bodyTile = new ArrayList<>();
        this.dirX = 0;
        this.dirY = 0;
        this.color= color;
        this.players = players;
        this.initializeBody(3);
        
    }
    public void initializeBody(int size){
        for (int i=1;i<=size;i++) {
            bodyTile.add(new Tile(head.getPosX()-i, head.getPosY()));
        }
    }
    public void createHead(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(head.getPosX()*Variables.TILE_SIZE, head.getPosY()*Variables.TILE_SIZE ,Variables.TILE_SIZE,Variables.TILE_SIZE);
        g.setColor(color);
        g.fillRect(head.getPosX()*Variables.TILE_SIZE, head.getPosY()*Variables.TILE_SIZE ,Variables.TILE_SIZE-4,Variables.TILE_SIZE-4);
    }
    
    public void createBody(Graphics g){
        g.setColor(Color.BLACK);
        if (!bodyTile.isEmpty()) {
            for (Tile body : bodyTile) {
                g.fillRect(body.getPosX()*Variables.TILE_SIZE, body.getPosY()*Variables.TILE_SIZE, Variables.TILE_SIZE, Variables.TILE_SIZE);
            }
        }
        g.setColor(color);
        if (!bodyTile.isEmpty()) {
            for (Tile body : bodyTile) {
                g.fillRect(body.getPosX()*Variables.TILE_SIZE, body.getPosY()*Variables.TILE_SIZE, Variables.TILE_SIZE-4, Variables.TILE_SIZE-4);
            }
        }
    }
    public void move(){
        if (Math.abs(dirX + dirY)>=1) {
            moveBody();
        }
        head.increasePosX(dirX);
        head.increasePosY(dirY);

    }
    public void moveBody(){
        
        if (!bodyTile.isEmpty()) {
            for (int i=bodyTile.size()-1;i>=0;i--) {
                Tile lasTile = bodyTile.get(i);
                if (i==0) {
                    bodyTile.get(0).setPosX(head.getPosX());
                    bodyTile.get(0).setPosY(head.getPosY());
                }else{
                    lasTile.setPosX(bodyTile.get(i-1).getPosX());
                    lasTile.setPosY(bodyTile.get(i-1).getPosY());
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        if (this.players==1) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (dirY != 1) {
                        dirX = 0;
                        dirY = -1;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (dirY !=-1) {
                        dirX = 0;
                        dirY = 1;
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if(dirX!=1){
                        dirX = -1;
                        dirY = 0;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (dirX!=-1) {
                        dirX = 1;
                        dirY=0;
                    }
                    break;
                default:
                    System.out.println("");
                    break;
            }
        }else if(players==2){
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    if (dirY != 1) {
                        dirX = 0;
                        dirY = -1;
                    }
                    break;
                case KeyEvent.VK_S:
                    if (dirY !=-1) {
                        dirX = 0;
                        dirY = 1;
                    }
                    break;
                case KeyEvent.VK_A:
                    if(dirX!=1){
                        dirX = -1;
                        dirY = 0;
                    }
                    break;
                case KeyEvent.VK_D:
                    if (dirX!=-1) {
                        dirX = 1;
                        dirY=0;
                    }
                    break;
                default:
                    System.out.println("");
                    break;
            }
        }
        
    }
    @Override
    public void keyReleased(KeyEvent e) {}

    public List<Tile> getBodyTile() {
        return bodyTile;
    }

    public void setBodyTile(List<Tile> bodyTile) {
        this.bodyTile = bodyTile;
    }

    public Tile getHead() {
        return head;
    }

    public void setHead(Tile head) {
        this.head = head;
    }

}
