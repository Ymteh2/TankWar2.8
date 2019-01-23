import java.awt.*;

public class Explode {
    int x,y;
    private boolean live = true;
    private TankClient tc;

    private static Toolkit tk = Toolkit.getDefaultToolkit();

     private static Image[] imgs = new Image[]{
             tk.getImage(Explode.class.getClassLoader().getResource("image/0.gif")),
             tk.getImage(Explode.class.getClassLoader().getResource("image/1.gif")),
             tk.getImage(Explode.class.getClassLoader().getResource("image/2.gif")),
             tk.getImage(Explode.class.getClassLoader().getResource("image/3.gif")),
             tk.getImage(Explode.class.getClassLoader().getResource("image/4.gif")),
             tk.getImage(Explode.class.getClassLoader().getResource("image/5.gif")),
             tk.getImage(Explode.class.getClassLoader().getResource("image/6.gif")),
             tk.getImage(Explode.class.getClassLoader().getResource("image/7.gif")),
             tk.getImage(Explode.class.getClassLoader().getResource("image/8.gif")),
             tk.getImage(Explode.class.getClassLoader().getResource("image/9.gif")),
             tk.getImage(Explode.class.getClassLoader().getResource("image/10.gif")),


     };
    int step = 0;

    private static boolean init = false;

    public Explode(int x,int y,TankClient tc){
        this.x = x;
        this.y = y;
        this.tc  = tc;
    }

    public void draw(Graphics g){
        if(false == init){
            for (int i = 0;i < imgs.length;i++) {
                g.drawImage(imgs[i],-100,-100,null);
            }
            init = true;
        }


        if(!live) {
            tc.explodes.remove(this);
            return;
        }
        if(step == imgs.length){
            live = false;
            step = 0;
            return;
        }
        g.drawImage(imgs[step],x,y,null);
        step++;
    }


}
