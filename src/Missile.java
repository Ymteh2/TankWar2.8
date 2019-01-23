import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Missile {
    public static final int X_SPEED = 10;
    public static final int Y_SPEED = 10;
    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;
    int x,y;
    Tank.Direction dir;

    private  boolean live = true;

    private boolean good ;

    private TankClient tc;
    private static Toolkit tk = Toolkit.getDefaultToolkit();

    private static Image[] missileImages = null;
    private  static Map<String,Image> imgs = new HashMap<String,Image>();
    static {
        missileImages = new Image[]{
                tk.getImage(Missile.class.getClassLoader().getResource("image/missileL.gif")),
                tk.getImage(Missile.class.getClassLoader().getResource("image/missileLU.gif")),
                tk.getImage(Missile.class.getClassLoader().getResource("image/missileU.gif")),
                tk.getImage(Missile.class.getClassLoader().getResource("image/missileRU.gif")),
                tk.getImage(Missile.class.getClassLoader().getResource("image/missileR.gif")),
                tk.getImage(Missile.class.getClassLoader().getResource("image/missileRD.gif")),
                tk.getImage(Missile.class.getClassLoader().getResource("image/missileD.gif")),
                tk.getImage(Missile.class.getClassLoader().getResource("image/missileLD.gif")),
        };
        imgs.put("L",missileImages[0]);
        imgs.put("LU",missileImages[1]);
        imgs.put("U",missileImages[2]);
        imgs.put("RU",missileImages[3]);
        imgs.put("R",missileImages[4]);
        imgs.put("RD",missileImages[5]);
        imgs.put("D",missileImages[6]);
        imgs.put("LD",missileImages[7]);
    }//静态执行一段代码，保证执行，最适合给变量初始化

    public Missile(int x, int y, Tank.Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public Missile (int x,int y,boolean good,Tank.Direction dir,TankClient tc){
        this(x,y,dir);
        this.good = good;
        this.tc = tc;
    }

    public void draw (Graphics g){
        if(!live) {
            tc.missiles.remove(this);
            return;
        }
        switch (dir) {
            case L:
                g.drawImage(imgs.get("L"),x,y,null);
                break;
            case LU:
                g.drawImage(imgs.get("LU"),x,y,null);
                break;
            case U:
                g.drawImage(imgs.get("U"),x,y,null);
                break;
            case RU:
                g.drawImage(imgs.get("RU"),x,y,null);
                break;
            case R:
                g.drawImage(imgs.get("R"),x,y,null);
                break;
            case RD:
                g.drawImage(imgs.get("RD"),x,y,null);
                break;
            case D:
                g.drawImage(imgs.get("D"),x,y,null);
                break;
            case LD:
                g.drawImage(imgs.get("LD"),x,y,null);
                break;
        }
        move();
    }

    private void move() {
        switch (dir){
            case L:
                x-=X_SPEED;
                break;
            case LU:
                x-=X_SPEED;
                y-=Y_SPEED;
                break;
            case U:
                y-=Y_SPEED;
                break;
            case RU:
                x+=X_SPEED;
                y-=Y_SPEED;
                break;
            case R:
                x+=X_SPEED;
                break;
            case RD:
                x+=X_SPEED;
                y+=Y_SPEED;
                break;
            case D:
                y+=Y_SPEED;
                break;
            case LD:
                x-=X_SPEED;
                y+=Y_SPEED;
                break;
            case STOP:
                break;
        }
        if(x<0||y<0||x>TankClient.GAME_WIDTH||y>TankClient.GAME_HEIGHT) {
            live = false;
            //tc.missiles.remove(this);
        }
    }
    public boolean isLive() {
        return live;
    }

    public Rectangle getRect(){
        return new Rectangle(x,y,WIDTH,HEIGHT);
    }

    //打击坦克
    public boolean hitTank(Tank t){
        if(this.live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood()){//isLive保证子弹打到死的坦克不会消失
            if(t.isGood()){
                t.setLife(t.getLife()-20);
                if(t.getLife()<=0)
                    t.setLive(false);
            }else
                t.setLive(false);
            this.live = false;
            Explode e = new Explode(x,y,tc);
            tc.explodes.add(e);
            return true;
        }
        return false;
    }

    public boolean hitTanks(List<Tank> tanks ){
        for(int i= 0;i<tanks.size();i++){
            if(hitTank(tanks.get(i))){
                return true;
            }
        }
        return false;
    }

    public boolean hitWall(Wall w){
        if(this.live && this.getRect().intersects(w.getRect())){
            this.live = false;
            return true;
        }
        return false;
    }
}
