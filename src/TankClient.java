import java .awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

public class TankClient extends Frame {
    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;
    Tank myTank = new Tank(700, 500, true,Tank.Direction.STOP,this);//当前的TankClient对象

    Wall w1 = new Wall(100,200,20,150,this),w2 = new Wall(300,100,300,20,this);

    List<Explode> explodes = new ArrayList<Explode>();

    List<Missile> missiles = new ArrayList<Missile>();
    List<Tank> tanks = new ArrayList<Tank>();
    Image offScreanImage = null;

    Blood b = new Blood();
    public void paint(Graphics g) {
        g.drawString("missiles count"+" "+missiles.size(),10,50);
        g.drawString("explodes count"+" "+explodes.size(),10,70);
        g.drawString("tanks count"+" "+tanks.size(),10,90);
        g.drawString("tanks Life"+" "+myTank.getLife(),10,110);
        if(tanks.size()<=0){
            for(int i = 0;i<5;i++){
                tanks.add(new Tank(50+40*(i+1),50,false,Tank.Direction.D,this));
            }
        }
        for(int i = 0;i<missiles.size();i++){
            Missile m = missiles.get(i);
            m.hitTanks(tanks);
            m.hitTank(myTank);
            m.hitWall(w1);
            m.hitWall(w2);
           // if(!m.isLive())
               // missiles.remove(m);
            //else
            m.draw(g);
            w1.draw(g);
            w2.draw(g);
            b.draw(g);
        }

        for(int i = 0;i<explodes.size();i++){
            Explode e  = explodes.get(i);
            e.draw(g);
        }

        for(int i = 0;i<tanks.size();i++){
            Tank t  =tanks.get(i);
            t.draw(g);
            t.collidesWithWall(w1);
            t.collidesWithWall(w2);
            t.collidesWithTanks(tanks);
        }

        myTank.draw(g);
        myTank.eat(b);

    }

    @Override
    public void update(Graphics g) {
        if(offScreanImage == null){
            offScreanImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);
        }
        Graphics gOffScrean = offScreanImage.getGraphics();
        Color c = gOffScrean.getColor();
        gOffScrean.setColor(Color.GREEN);
        gOffScrean.fillRect(0,0,GAME_WIDTH,GAME_HEIGHT);
        gOffScrean.setColor(c);
        paint(gOffScrean);
        g.drawImage(offScreanImage,0,0,null);
    }

    public void launchFrame(){
        for(int i = 0;i<10;i++){
            tanks.add(new Tank(50+40*(i+1),50,false,Tank.Direction.D,this));
        }
        this.setLocation(300,400);
        this.setSize(GAME_WIDTH,GAME_HEIGHT);
        this.setTitle("TankWar");
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setResizable(false);
        this.setBackground(Color.GREEN);
        this.setVisible(true);
        new Thread(new PaintThread()).start();
        this.addKeyListener(new KeyMonitor());
    }

    public static void main(String[] args) {
        TankClient tc = new TankClient();
        tc.launchFrame();

    }
    private class PaintThread implements Runnable{
        @Override
        public void run() {
            while(true){
                repaint();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class KeyMonitor extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            myTank.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            myTank.keyReleased(e);
        }
    }
}
