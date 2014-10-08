package zad1.Biometria;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class HistPanel extends javax.swing.JPanel {
    BufferedImage _img;
    int[] _rgbs;
    int _n;
    public HistPanel(BufferedImage img) {
        _img = img;
        _n = _img.getHeight()*_img.getWidth();
        _rgbs = _img.getRGB(0, 0, _img.getWidth(),  _img.getHeight(), _rgbs, 0, _img.getWidth());
    }
    public void clear(BufferedImage img) {
        _img = img;
        _n = _img.getHeight()*_img.getWidth();
        _rgbs = new int[_n];
        _rgbs = _img.getRGB(0, 0, _img.getWidth(),  _img.getHeight(), _rgbs, 0, _img.getWidth());
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int max=0;
        int r[] = new int[_n];
        int rhist[] = new int[256];
        int vg[] = new int[_n];
        int ghist[] = new int[256];
        int b[] = new int[_n];
        int bhist[] = new int[256];
        int hist[] = new int[256];

        for(int i=0;i<_n;i++) {
            r[i] =(_rgbs[i]&0x00ff0000)>>16;
            //hist[r[i]]++;
            rhist[r[i]]++;
            vg[i] =(_rgbs[i]&0x0000ff00)>>8;
            //hist[vg[i]]++;
            ghist[vg[i]]++;
            b[i] =(_rgbs[i]&0x000000ff);
            //hist[b[i]]++;
            bhist[b[i]]++;
            hist[(r[i]+vg[i]+b[i])/3]++;
        }
        
        for(int i=0;i<256;i++) 
            if(hist[i]>max)
                max = hist[i];
        
        int div = max/400+2;
        for(int i=0;i<256;i++){
            g2.setColor(Color.red);
            g2.drawLine(i, 400, i, 400-rhist[i]/div);
            g2.setColor(Color.green);
            g2.drawLine(260+i, 400, 260+i, 400-ghist[i]/div);
            g2.setColor(Color.blue);
            g2.drawLine(520+i, 400, 520+i, 400-bhist[i]/div);
            g2.setColor(Color.black);
            g2.drawLine(780+i, 400, 780+i, 400-hist[i]/div);
        }
        g2.drawLine(780, 400, 780, 0);
    }
    public void setImg(BufferedImage img) {
        img.getRGB(0, 0, img.getWidth(),  img.getHeight(), _rgbs, 0, img.getWidth());
        System.out.println(_rgbs[100]);
        this.repaint();
    }
}
