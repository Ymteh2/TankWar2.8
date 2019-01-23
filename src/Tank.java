import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Tank {
    public static final int X_SPEED = 5;
    public static final int Y_SPEED = 5;
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;


    private int x,y;
    private int oldX,oldY;//记录坦克上一步位置

    private boolean live = true;

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    private int life = 100;

    TankClient tc; //持有TankClient 的引用，可以访问里面的成员变量

    public boolean isGood() {
        return good;
    }

    private boolean good;

    private static Random r = new Random();//staric多个对象共享他一个

    private boolean bL  = false,bU = false,bR = false,bD = false;
    enum Direction  {L,LU,U,RU,R,RD,D,LD,STOP};

    private Direction dir = Direction.STOP  ;
    private Direction ptDir = Direction.D ;
    private BloodBar bb = new BloodBar();

    private int step = r.nextInt(12)+3;//控制敌方坦克移动多少步

    private static Toolkit tk = Toolkit.getDefaultToolkit();

    private static Image[] tankImages = null;
    private  static Map<String,Image> imgs = new HashMap<String,Image>();
    static {
        tankImages = new Image[]{
                tk.getImage(Tank.class.getClassLoader().getResource("image/tankL.gif")),
                tk.getImage(Tank.class.getClassLoader().getResource("image/tankLU.gif")),
                tk.getImage(Tank.class.getClassLoader().getResource("image/tankU.gif")),
                tk.getImage(Tank.class.getClassLoader().getResource("image/tankRU.gif")),
                tk.getImage(Tank.class.getClassLoader().getResource("image/tankR.gif")),
                tk.getImage(Tank.class.getClassLoader().getResource("image/tankRD.gif")),
                tk.getImage(Tank.class.getClassLoader().getResource("image/tankD.gif")),
                tk.getImage(Tank.class.getClassLoader().getResource("image/tankLD.gif")),
        };
        imgs.put("L",tankImages[0]);
        imgs.put("LU",tankImages[1]);
        imgs.put("U",tankImages[2]);
        imgs.put("RU",tankImages[3]);
        imgs.put("R",tankImages[4]);
        imgs.put("RD",tankImages[5]);
        imgs.put("D",tankImages[6]);
        imgs.put("LD",tankImages[7]);
    }//静态执行一段代码，保证执行，最适合给变量初始化

    public Tank(int x, int y,boolean good) {
        this.x = x;
        this.y = y;
        this.oldX = x;
        this.oldY = y;
        this.good = good;
    }

    public Tank(int x,int y,boolean good,Direction dir,TankClient tc){
        this(x,y,good);
        this.dir = dir;
        this.tc = tc;
    }

    public void draw(Graphics g){
        if(!live) {
            if(!good)
                tc.tanks.remove(this);
            return;//坦克死了就不花了
        }

        if(good) bb.draw(g);
        switch (ptDir) {
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

    void move(){
        this.oldX = x;
        this.oldY = y;
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
        //坦克出界问题
        if(x < 0) x = 0;
        if(y < 30) y = 30;
        if(x + Tank.WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
        if(y + Tank.HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - Tank.HEIGHT;

        if(this.dir != Direction.STOP){
            this.ptDir = this.dir;
        }
        if(!good){
            Direction [] dirs = Direction.values();//values()返回一个数组，该数组包含此枚举类型的常量，按声明的顺序排列。
            if(step == 0){//step=0 才转方向
                step = r.nextInt(12)+3;
                int rn = r.nextInt(dirs.length);//nextInt()  返回一个伪随机数，它是取自此随机数生成器序列的、在 0（包括）和指定值（不包括）之间均匀分布的 int 值。
                this.dir = dirs[rn];
            }
            step--;
            if(r.nextInt(40)>38)this.fire();
        }
    }

    private void stay(){
        x = oldX;
        y = oldY;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key){
            case  KeyEvent.VK_RIGHT:
                bR = true;
                break;
            case  KeyEvent.VK_UP:
                bU = true;
                break;
            case  KeyEvent.VK_LEFT:
                bL = true;
                break;
            case  KeyEvent.VK_DOWN:
                bD = true;
                break;
            case KeyEvent.VK_F2:
                if(!this.live){
                    this.live = true;
                    this.life = 100;
                }
                break;
        }
        locateDirection();
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key){
            case KeyEvent.VK_CONTROL:
                //tc.missiles.add(fire());
                fire();
                break;
            case  KeyEvent.VK_RIGHT:
                bR = false;
                break;
            case  KeyEvent.VK_UP:
                bU = false;
                break;
            case  KeyEvent.VK_LEFT:
                bL = false;
                break;
            case  KeyEvent.VK_DOWN:
                bD = false;
                break;
            case KeyEvent.VK_A:
                superFire();
                break;
        }
        locateDirection();
    }

    void locateDirection(){
        if(bL && !bU && !bR && !bD) dir = Direction.L;
        else if(bL && bU && !bR && !bD) dir = Direction.LU;
        else if(!bL && bU && !bR && !bD) dir = Direction.U;
        else  if(!bL && bU && bR && !bD) dir = Direction.RU;
        else if(!bL && !bU && bR && !bD) dir = Direction.R;
        else if(!bL && !bU && bR && bD) dir = Direction.RD;
        else if(!bL && !bU && !bR && bD) dir = Direction.D;
        else if(bL && !bU && !bR && bD) dir = Direction.LD;
        else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;

    }

    public Missile fire(){
        if(!live)return null;
        int x = this.x+Tank.WIDTH/2-Missile.WIDTH/2;
        int y = this.y +Tank.HEIGHT/2-Missile.HEIGHT/2;
        //Missile m = new Missile(x,y,ptDir);
        Missile m = new Missile(x,y,good,ptDir,this.tc);
        tc.missiles.add(m);
        return m;
    }

    public Missile fire(Direction dir){
        if(!live)return null;
        int x = this.x+Tank.WIDTH/2-Missile.WIDTH/2;
        int y = this.y +Tank.HEIGHT/2-Missile.HEIGHT/2;
        //Missile m = new Missile(x,y,ptDir);
        Missile m = new Missile(x,y,good,dir,this.tc);
        tc.missiles.add(m);
        return m;
    }

    public Rectangle getRect(){
        return new Rectangle(x,y,tankImages[0].getWidth(null),tankImages[0].getHeight(null));
    }
    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public boolean collidesWithWall(Wall w){
        if(this.live && this.getRect().intersects(w.getRect())){
            this.stay();
            return true;
        }
        return false;
    }

    public boolean collidesWithTanks(List<Tank> tanks){
        for(int i = 0;i<tanks.size();i++){
            Tank t  = tanks.get(i);
            if(this!=t){
                if(this.live&&t.isLive()&&this.getRect().intersects(t.getRect())){
                    this.stay();
                    t.stay();
                    return true;
                }
            }
        }
        return false;
    }

    public void superFire(){
        Direction dirs [] = Direction.values();
        for(int i = 0;i<8;i++){
            fire(dirs[i]);
        }
    }

    private class BloodBar{
        private void draw(Graphics g){
            Color c = g.getColor();
            g.setColor(Color.BLUE);
            g.drawRect(x,y-10,WIDTH,10);
            int w = WIDTH*life/100;
            g.fillRect(x,y-10,w,10);
            g.setColor(c);
        }
    }

    public boolean eat(Blood b) {
        if(this.live && b.isLive()&&this.getRect().intersects(b.getRect())){
            this.life = 100;
            b.setLive(false);
            return true;
        }
        return false;

    }
}

