package zad1.Biometria;

import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImgPanel extends JPanel{
    float scaleFactor = 1.0f;
    float offset = 10;
    boolean brighten, contrastInc;
    RescaleOp rescale;
    BufferedImage biSrc, biDest; //sorce, destination - zapamiętują stan obrazka żeby można było powrócić np. rozjaśniając a później cieniując do tego samego stanu
    Graphics2D big;
    Image displayImage;
    File file;
    int value=0;
    float matrix[][]=new float[0][0];
    int awp=0; //x aktywnego piksla
    int ahp=0; //y aktywnego piksla
    private String img_name;
   
     
    private BufferedImage _img, _backup;
    
    private int[] _rgbs, r, g, b;
    private int _n;
   
    public ImgPanel(String img_name) {
        this.img_name=img_name; //zapamiętuje nazwę obrazka w polu klasu
        initVariables();//wywołane dla nazwy obrazka podanego w konstruktorze
    }
    
    private void initVariables(){// to co było konstruktorze + moje rzeczy do działąnia zmiany kontrastu i jasności
        setPic(img_name);// _img zainicjalizowany tutaj
        _n = _img.getHeight()*_img.getWidth();
        _rgbs=new int[_n];
        r=new int[_n];
        g=new int[_n];
        b=new int[_n];
        setPreferredSize(new Dimension(_img.getWidth(), _img.getHeight()));//stąd wywaliłem img
        ColorModel cm = _img.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = _img.copyData(null);
        _img = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        _backup = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        
        _rgbs=_img.getRGB(0, 0, _img.getWidth(),  _img.getHeight(), _rgbs, 0, _img.getWidth());
        for(int i=0;i<_n;i++) {
            r[i] =(_rgbs[i]&0x00ff0000)>>16;
            g[i] =(_rgbs[i]&0x0000ff00)>>8;
            b[i] =(_rgbs[i]&0x000000ff);
        }
   //<EDIT>----------------------------------------
        displayImage = Toolkit.getDefaultToolkit().getImage(img_name); 
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(displayImage,1);
        try {
            mt.waitForAll();
        } catch (Exception e) {
            System.out.println("Exception while loading.");
        }
 
        if (displayImage.getWidth(this) == -1) {
            System.out.println("No jpg file");
            System.exit(0);
        }        
        biSrc= _img;
        big = biSrc.createGraphics();
        big.drawImage(displayImage, 0, 0, this);
        biDest = new BufferedImage(displayImage.getWidth(this),
                                   displayImage.getHeight(this),
                                   BufferedImage.TYPE_INT_RGB);
        _img = biSrc;   
    }
    
    public void clear(){
       scaleFactor = 1.0f;
       offset = 10;
       initVariables();
       
       
        /*ColorModel cm = img.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = img.copyData(null);
        _img = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        _rgbs=_img.getRGB(0, 0, _img.getWidth(),  _img.getHeight(), _rgbs, 0, _img.getWidth());
        _n = _img.getHeight()*_img.getWidth();
        * 
        */
    }
  
    //<EDIT2>-------------------
    public void changeOffSet() {
    if (brighten) {
        if (offset < 255)
            offset = offset+5.0f;
    }
    else {
        if (offset > 0)
            offset = offset-5.0f;
        }
    }
    public void changeScaleFactor() {
        if (contrastInc) {
            if (scaleFactor < 2)
                scaleFactor = scaleFactor+0.1f;
        }
        else {
            if (scaleFactor > 0)
                scaleFactor = scaleFactor-0.1f;
        }
    }
    public void rescale() {
    rescale = new RescaleOp(scaleFactor, offset, null);
    rescale.filter(biSrc, biDest);  
    _img = biDest;
    }
    @Override
    public void update(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        paintComponent(g);
    }
    //</EDIT2>|||||||||||||||||||||||||||||
    
    
    //<EDIT3>--wstawiłem tutaj funkcję pobierania obrazka z pliku która była w Histogram.java--------------------------------
    public void setPic(String pic_name){
        
        file= new File("lena.jpg");
        
        if (!file.exists() && !file.isDirectory()) {
        	try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        try {
           _img = ImageIO.read(file);
           System.out.println("wczytano obrazek");
        } catch (IOException e) {
            System.out.println("Blad wczytania pliku");
        }
        /*
        Graphics g = _img.getGraphics();
        _picPanel = new ImgPanel(_img);
        _chgdPanel = new ImgPanel(_img);  
        */
    }
    
    public BufferedImage setPic(File file){
        try {
           _img = ImageIO.read(file);
           System.out.println("wczytano obrazek");
        } catch (IOException e) {
            System.out.println("Blad wczytania pliku");
        }   
        setSize(_img.getWidth(), _img.getHeight());
        biSrc=_img;
        biDest = new BufferedImage(displayImage.getWidth(this),
                                   displayImage.getHeight(this),
                                   BufferedImage.TYPE_INT_RGB);
        displayImage = Toolkit.getDefaultToolkit().getImage(img_name);
        this.img_name=file.getName();
        initVariables();
        
        return _img;
    }
    
    //</EDIT3>|||||||||||||||||||||||||||||||
    
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;       
        g2.drawImage(_img, 0, 0, this);
    }
    
    public void setActivPixel(int w, int h) {
        awp=w;
        ahp=h;
    }
    
    public void setValue(int v) {
        value = v;
    }
    
    public int getValue() {
        return value;
    }
    
    public int getActiveX() {
        return awp;
    }
    
    public int getActiveY() {
        return ahp;
    }
    
    public void setMatrix(float[][] mx) {
        matrix = mx;
    }
    
    public int[] getHist() {
        return _rgbs;
    }
    
    public int getImgSize() {
        return _n;
    }
    
    public BufferedImage getImg() {
        return _img;
    }
    
    public void filter(int num) {
        ColorModel cm = _img.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = _img.copyData(null);
        _backup = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        Graphics g1 = _img.getGraphics();
        Graphics2D g2 = (Graphics2D) g1;
        ByteLookupTable blut;
        LookupOp lop;
        int min;
        int max;
        int minr=255;
        int maxr=0;
        int ming=255;
        int maxg=0;
        int minb=255;
        int maxb=0;
        int cw=0;
        int ch=0;
        int sw=0;
        int sh=0;
        int w = _img.getWidth();
        int h = _img.getHeight();
        int n = w*h;
        int rhist[] = new int[256];
        int ghist[] = new int[256];
        int bhist[] = new int[256];
        int grayrgbs[] = new int[_n];
        int gray=0;
        int mrgbs[][];
        byte lut[] = new byte[256];
        int red;
        int green;
        int blue;
        int alpha;
        int newPixel = 0;
        _n = n;
        int[] rgbs = new int[n];
        switch(num) {
            case 0: //LKO
                for (int j=0; j<256; j++) {
                    lut[j] = (byte)(256-j); 
                }
                blut = new ByteLookupTable(0, lut); 
                lop = new LookupOp(blut, null);
                g2.drawImage(_img, lop, 0, 0);
                _img.getRGB(0, 0, w,  h, _rgbs, 0, w);
            break;
            case 1: //show pixel which has proper value in grayscale
                _img.getRGB(0, 0, w,  h, _rgbs, 0, w);
                for(int i=0;i<_n;i++) {
                    r[i] =(_rgbs[i]&0x00ff0000)>>16;
                    g[i] =(_rgbs[i]&0x0000ff00)>>8;
                    b[i] =(_rgbs[i]&0x000000ff);
                    rhist[r[i]]++;
                    ghist[g[i]]++;
                    bhist[b[i]]++;
                    gray = (int)((0.299*r[i])+(0.587*g[i])+(b[i]*0.114));
                    if(gray==value) gray=255;
                    else gray=0;
                    grayrgbs[i] = ((int)gray<<16)&0x00ff0000 | ((int)gray<<8)&0x0000ff00 | (int)gray;
                }
                for(int i=0;i<w;i++)
                    for(int j=0;j<h;j++)
                        _img.setRGB(i, j, grayrgbs[i+w*j]);
                _img.getRGB(0, 0, w,  h, _rgbs, 0, w);
            break;
            case 2: //normalizacja
                _img.getRGB(0, 0, w,  h, _rgbs, 0, w);
                minr=255;
                maxr=0;
                ming=255;
                maxg=0;
                minb=255;
                maxb=0;
                for(int i=0;i<_n;i++) {
                    r[i] =(_rgbs[i]&0x00ff0000)>>16;
                    g[i] =(_rgbs[i]&0x0000ff00)>>8;
                    b[i] =(_rgbs[i]&0x000000ff);
                    rhist[r[i]]++;
                    ghist[g[i]]++;
                    bhist[b[i]]++;
                    if(r[i]<minr) minr=r[i];
                    if(r[i]>maxr) maxr=r[i];
                    if(g[i]<ming) ming=g[i];
                    if(g[i]>maxg) maxg=g[i];
                    if(b[i]<minb) minb=b[i];
                    if(b[i]>maxb) maxb=b[i];
                }
                for(int i=0;i<_n;i++) {
                    r[i] = ((int)((255/((double)(maxr-minr)))*(r[i]-minr)));
                    g[i] = ((int)((255/((double)(maxg-ming)))*(g[i]-ming)));
                    b[i] = ((int)((255/((double)(maxb-minb)))*(b[i]-minb)));
                    _rgbs[i] = r[i]<<16&0x00ff0000 |  g[i]<<8&0x0000ff00  | b[i];
                }
                for(int i=0;i<w;i++)
                    for(int j=0;j<h;j++)
                        _img.setRGB(i, j, _rgbs[i+w*j]);
                _img.getRGB(0, 0, w,  h, _rgbs, 0, w);
            break;
            case 3: //convolve, splot
                int ln = matrix.length;
                float op[] = new float[ln*ln];
                for(int i=0;i<ln;i++)
                    for(int j=0;j<ln;j++)
                        op[i+ln*j]=matrix[i][j];
                Kernel kern = new Kernel(matrix.length, matrix.length, op);
                int edge = ConvolveOp.EDGE_ZERO_FILL;
                ConvolveOp conv = new ConvolveOp(kern, edge, null);
                
                _img = conv.filter(_img, null);
                _img.getRGB(0, 0, w,  h, _rgbs, 0, w);
                break;
            case 4: //erozja
                mrgbs = new int[w][h];
                int[] ergbs = new int[n];
                sw=matrix.length;
                sh=matrix.length;
                int e[][] = new int[sw][sh];
                int erozja[][] = new int[sw][sh];                
                for(int i=0;i<sw;i++)
                    for(int j=0;j<sh;j++){
                        erozja[i][j]=(int)matrix[i][j];
                        e[i][j]=-1;
                    }
                cw=awp;
                ch=ahp; //aktywny piksel w masce
                _img.getRGB(0, 0, w, h, rgbs, 0, w);
                for(int i=0;i<w;i++)
                    for(int j=0;j<h;j++)
                        mrgbs[i][j]=rgbs[i+w*j];
                for(int i=0;i<w;i++)
                    for(int j=0;j<h;j++){
                        min=mrgbs[i][j];
                        for(int l=0;l<sw;l++)
                            for(int m=0;m<sh;m++)
                                if(erozja[l][m]==1)
                                    if(i-cw+l>=0 && j-ch+m>=0 && i-cw+l<w && j-ch+m<h){
                                        e[l][m]=mrgbs[i-cw+l][j-ch+m];
                                        if(e[l][m]>min) min=e[l][m];
                                    }
                        ergbs[i+w*j]=min;
                    }
                for(int i=0;i<w;i++)
                    for(int j=0;j<h;j++)
                        _img.setRGB(i, j, ergbs[i+w*j]);
                _img.getRGB(0, 0, w,  h, _rgbs, 0, w);
                break;
            case 5: //dylacja
                mrgbs = new int[w][h];                
                int[] drgbs = new int[n];
                sw=matrix.length;
                sh=matrix.length;
                int d[][] = new int[sw][sh];
                int dylacja[][] = new int[sw][sh];
                for(int i=0;i<sw;i++)
                    for(int j=0;j<sh;j++){
                        dylacja[i][j]=(int)matrix[i][j];
                        d[i][j]=-1;
                    }
                cw=awp;
                ch=ahp; //aktywny piksel w masce
                _img.getRGB(0, 0, w, h, rgbs, 0, w);
                for(int i=0;i<w;i++)
                    for(int j=0;j<h;j++)
                        mrgbs[i][j]=rgbs[i+w*j];
                for(int i=0;i<w;i++)
                    for(int j=0;j<h;j++){
                        max=0;
                        for(int l=0;l<sw;l++)
                            for(int m=0;m<sh;m++)
                                if(dylacja[l][m]==1)
                                    if(i-cw+l>=0 && j-ch+m>=0 && i-cw+l<w && j-ch+m<h){
                                        d[l][m]=mrgbs[i-cw+l][j-ch+m];
                                        if(d[l][m]<max) max=d[l][m];
                                    }
                        drgbs[i+w*j]=max;
                    }
                for(int i=0;i<w;i++)
                    for(int j=0;j<h;j++)
                        _img.setRGB(i, j, drgbs[i+w*j]);
                _img.getRGB(0, 0, w,  h, _rgbs, 0, w);
            break;
            case 6: //progowanie
                System.out.println(value);
                _img.getRGB(0, 0, w,  h, _rgbs, 0, w);
                for(int i=0;i<_n;i++) {
                    r[i] =(_rgbs[i]&0x00ff0000)>>16;
                    g[i] =(_rgbs[i]&0x0000ff00)>>8;
                    b[i] =(_rgbs[i]&0x000000ff);
                    rhist[r[i]]++;
                    ghist[g[i]]++;
                    bhist[b[i]]++;
                    gray = (int)((0.299*r[i])+(0.587*g[i])+(b[i]*0.114));
                    if(gray>=value) gray=255;
                    else if(gray<value) gray=0;
                    grayrgbs[i] = ((int)gray<<16)&0x00ff0000 | ((int)gray<<8)&0x0000ff00 | (int)gray;
                }
                for(int i=0;i<w;i++)
                    for(int j=0;j<h;j++)
                        _img.setRGB(i, j, grayrgbs[i+w*j]);
                _img.getRGB(0, 0, w,  h, _rgbs, 0, w);
            break;
            case 7: //grayscale
                _img.getRGB(0, 0, w,  h, _rgbs, 0, w);
                for(int i=0;i<_n;i++) {
                    r[i] =(_rgbs[i]&0x00ff0000)>>16;
                    g[i] =(_rgbs[i]&0x0000ff00)>>8;
                    b[i] =(_rgbs[i]&0x000000ff);
                    rhist[r[i]]++;
                    ghist[g[i]]++;
                    bhist[b[i]]++;
                    gray = (int)((0.299*r[i])+(0.587*g[i])+(b[i]*0.114));
                    grayrgbs[i] = ((int)gray<<16)&0x00ff0000 | ((int)gray<<8)&0x0000ff00 | (int)gray;
                }
                for(int i=0;i<w;i++)
                    for(int j=0;j<h;j++)
                        _img.setRGB(i, j, grayrgbs[i+w*j]);
                _img.getRGB(0, 0, w,  h, _rgbs, 0, w);
            break;
            case 8: //wyrownanie histogramu
                for(int i=0; i<rhist.length; i++) rhist[i] = 0;
                for(int i=0; i<ghist.length; i++) ghist[i] = 0;
                for(int i=0; i<bhist.length; i++) bhist[i] = 0;
                for(int i=0; i<w; i++) {
                    for(int j=0; j<h; j++) {
                        red = new Color(_img.getRGB (i, j)).getRed();
                        green = new Color(_img.getRGB (i, j)).getGreen();
                        blue = new Color(_img.getRGB (i, j)).getBlue();
                        rhist[red]++; ghist[green]++; bhist[blue]++;
                    }
                }
                ArrayList<int[]> hist = new ArrayList<int[]>();
                hist.add(rhist);
                hist.add(ghist);
                hist.add(bhist);
                ArrayList<int[]> imageLUT = new ArrayList<int[]>();
                long sumr = 0;
                long sumg = 0;
                long sumb = 0;
                float scale_factor = (float) (255.0 / n);

                for(int i=0; i<rhist.length; i++) {
                    sumr += hist.get(0)[i];
                    int valr = (int) (sumr * scale_factor);
                    if(valr > 255) {
                        rhist[i] = 255;
                    }
                    else rhist[i] = valr;
                    sumg += hist.get(1)[i];
                    int valg = (int) (sumg * scale_factor);
                    if(valg > 255) {
                        ghist[i] = 255;
                    }
                    else ghist[i] = valg;
                    sumb += hist.get(2)[i];
                    int valb = (int) (sumb * scale_factor);
                    if(valb > 255) {
                        bhist[i] = 255;
                    }
                    else bhist[i] = valb;
                }
                imageLUT.add(rhist);
                imageLUT.add(ghist);
                imageLUT.add(bhist);
                for(int i=0; i<w; i++) {
                    for(int j=0; j<h; j++) {
                        alpha = new Color(_img.getRGB (i, j)).getAlpha();
                        red = new Color(_img.getRGB (i, j)).getRed();
                        green = new Color(_img.getRGB (i, j)).getGreen();
                        blue = new Color(_img.getRGB (i, j)).getBlue();
                        red = imageLUT.get(0)[red];
                        green = imageLUT.get(1)[green];
                        blue = imageLUT.get(2)[blue];
                        newPixel = 0;
                        newPixel += alpha; newPixel = newPixel << 8;
                        newPixel += red; newPixel = newPixel << 8;
                        newPixel += green; newPixel = newPixel << 8;
                        newPixel += blue;
                        _img.setRGB(i, j, newPixel);
                    }
                }
                _img.getRGB(0, 0, w,  h, _rgbs, 0, w);
            break;
            case 9: //rozciaganie
                _img.getRGB(0, 0, w,  h, _rgbs, 0, w);
                minr=255;
                maxr=0;
                ming=255;
                maxg=0;
                minb=255;
                maxb=0;
                for(int i=0;i<_n;i++) {
                    r[i] =(_rgbs[i]&0x00ff0000)>>16;
                    g[i] =(_rgbs[i]&0x0000ff00)>>8;
                    b[i] =(_rgbs[i]&0x000000ff);
                    rhist[r[i]]++;
                    ghist[g[i]]++;
                    bhist[b[i]]++;
                    if(r[i]<minr) minr=r[i];
                    if(r[i]>maxr) maxr=r[i];
                    if(g[i]<ming) ming=g[i];
                    if(g[i]>maxg) maxg=g[i];
                    if(b[i]<minb) minb=b[i];
                    if(b[i]>maxb) maxb=b[i];
                }
                rgbs = new int[_n];
                int nr, ng, nb;
                for(int i=0;i<_n;i++) {
                     nr = ((int)(((ahp-awp)/((double)(maxr-minr)))*(r[i]-minr))+awp);
                     //if (nr>255) nr=255;
                     //else if (nr<0) nr=0;
                     ng = ((int)(((ahp-awp)/((double)(maxg-ming)))*(g[i]-ming))+awp);
                     //if (ng>255) ng=255;
                     //else if (ng<0) ng=0;
                     nb = ((int)(((ahp-awp)/((double)(maxb-minb)))*(b[i]-minb))+awp);
                     //if (nb>255) nb=255;
                     //else if (nb<0) nb=0;
                     rgbs[i] = nr<<16&0x00ff0000 | ng<<8&0x0000ff00 | nb;
                }
                for(int i=0;i<w;i++)
                    for(int j=0;j<h;j++)
                        _img.setRGB(i, j, rgbs[i+w*j]);
                _img.getRGB(0, 0, w,  h, _rgbs, 0, w);
            break;
            case 10: //otsu
                _img.getRGB(0, 0, w,  h, _rgbs, 0, w);
                int[] hgray = new int[256];
                for(int i=0;i<_n;i++) {
                    r[i] =(_rgbs[i]&0x00ff0000)>>16;
                    g[i] =(_rgbs[i]&0x0000ff00)>>8;
                    b[i] =(_rgbs[i]&0x000000ff);
                    rhist[r[i]]++;
                    ghist[g[i]]++;
                    bhist[b[i]]++;
                    gray = (int)((0.299*r[i])+(0.587*g[i])+(b[i]*0.114));
                    hgray[gray]++;
                }
                float sum = 0;
                for(int i=0; i<256; i++) sum += i * hgray[i];
                float sumB = 0;
                int wB = 0;
                int wF = 0;
                float varMax = 0;
                int threshold = 0;
                for(int i=0 ; i<256 ; i++) {
                    wB += hgray[i];
                    if(wB == 0) continue;
                    wF = _n - wB;
                    if(wF == 0) break;
                    sumB += (float) (i * hgray[i]);
                    float mB = sumB / wB;
                    float mF = (sum - sumB) / wF;
                    float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);
                    if(varBetween > varMax) {
                        varMax = varBetween;
                        threshold = i;
                    }
                }
                for(int i=0;i<_n;i++) {
                    r[i] =(_rgbs[i]&0x00ff0000)>>16;
                    g[i] =(_rgbs[i]&0x0000ff00)>>8;
                    b[i] =(_rgbs[i]&0x000000ff);
                    rhist[r[i]]++;
                    ghist[g[i]]++;
                    bhist[b[i]]++;
                    gray = (int)((0.299*r[i])+(0.587*g[i])+(b[i]*0.114));
                    if(gray>threshold) gray=255;
                    else gray=0;
                    grayrgbs[i] = ((int)gray<<16)&0x00ff0000 | ((int)gray<<8)&0x0000ff00 | (int)gray;
                }
                System.out.println(threshold);
                for(int i=0;i<w;i++)
                    for(int j=0;j<h;j++)
                        _img.setRGB(i, j, grayrgbs[i+w*j]);
                _img.getRGB(0, 0, w,  h, _rgbs, 0, w);
            break;
            case 11: //Median filter
                _img.getRGB(0, 0, w,  h, _rgbs, 0, w);
                w = _img.getWidth();
                h = _img.getHeight();//ImageProcessor copy = orig.duplicate();
                int K=4;
                int[] P = new int[2*K+1]; //vector to hold pixels from 3x3 neighborhood
                for (int v=1; v<=h-2; v++) {
                    for (int u=1; u<=w-2; u++) { 
                        int k = 0;//fill the pixel vector P for filter position (u,v)
                        for (int j=-1; j<=1; j++) {
                            for (int i=-1; i<=1; i++) {
                                P[k] = _rgbs[u+i+w*(v+j)];
                                k++;
                            }
                        }       
                        Arrays.sort(P);//sort the pixel vector and take center element
                        _img.setRGB(u, v, P[K]);
                    }
                }
                _img.getRGB(0, 0, w,  h, _rgbs, 0, w);
                break;
            case 12: //gaussian
                ln = value;
                if (value==0) break;
                int rad = (int)Math.ceil(ln);
		int rows = rad*2+1;
		float[] mat = new float[rows];
		float sigma = ln/3;
		float sigma22 = 2*sigma*sigma;
		float sigmaPi2 = 2*(float)Math.PI*sigma;
		float sqrtSigmaPi2 = (float)Math.sqrt(sigmaPi2);
		float radius2 = rad*rad;
		float total = 0;
		int index = 0;
		for (int row = -rad; row <= rad; row++) {
			float distance = row*row;
			if (distance > radius2)
				mat[index] = 0;
			else
				mat[index] = (float)Math.exp(-(distance)/sigma22) / sqrtSigmaPi2;
			total += mat[index];
			index++;
		}
		for (int i = 0; i < rows; i++){
			mat[i] /= total;
                }
                kern = new Kernel(rows, 1, mat);
                edge = ConvolveOp.EDGE_ZERO_FILL;
                conv = new ConvolveOp(kern, edge, null);
                _img = conv.filter(_img, null);
                _img.getRGB(0, 0, w,  h, _rgbs, 0, w);
                break;
            default:
                break;
        }
    }
    
    public void back() {
        _img = _backup;
    }
    
    public void saveoutPic(String filename){
      try {
            File sfile = new File(filename);
            ImageIO.write(getImg(), "png", sfile);
        } catch (IOException ef) {
            System.out.println("Blad zapisu do pliku");
        }
    }
    
    public void setRGBto_img(int [] r, int [] g, int []b, BufferedImage image){
        int h= image.getHeight(),
            w= image.getWidth(),
            n=h*w;
        System.out.println("lena ma rozmiar: "+n);
        int [] tmp_rgbs= new int [n]; 
        for(int i=0 ;i<n ;i++ )
            tmp_rgbs[i] = (((int)r[i]<<16)&0x00ff0000 | ((int)g[i]<<8)&0x0000ff00 | (int)b[i]);
        for(int i=0;i<w;i++)
           for(int j=0;j<h;j++)
               this._img.setRGB(i, j, tmp_rgbs[i+w*j]); 
    }

    public void set_r_g_b_tab(int [] r, int [] g, int [] b){// uzupełniam tablice r,g,b wartościami z _rgbs, czyli tym co jest w _img.getRGB
        this._rgbs=this._img.getRGB(0, 0, _img.getWidth(),  _img.getHeight(), _rgbs, 0, _img.getWidth());
        for(int i=0;i<this._n;i++) {
            r[i] =((this._rgbs[i]&0x00ff0000)>>16);
            g[i] =((this._rgbs[i]&0x0000ff00)>>8);
            b[i] =(this._rgbs[i]&0x000000ff);    
        }
    }
    
    public void addPic(BufferedImage bi){
        ColorModel cm = _img.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = _img.copyData(null);
        _backup = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        int bi_w= bi.getWidth(),
            bi_h= bi.getHeight(),
            bi_n = (bi_w*bi_h);
        System.out.println("dodawanie obrazka zakończone\nobrazek ma rozmiar "+bi_n);
        int [] bi_rgbs = new int[bi_n],
               r_bi = new int[bi_n],
               g_bi = new int[bi_n],
               b_bi = new int[bi_n]; 
        bi.getRGB(0, 0, bi.getWidth(), bi.getHeight(), bi_rgbs, 0, bi.getWidth());
        //uzupełniam tablice wartościami kolorów
                for(int i=0;i<bi_n;i++) {
                    r_bi[i] =((bi_rgbs[i]&0x00ff0000)>>16);
                    g_bi[i] =((bi_rgbs[i]&0x0000ff00)>>8);
                    b_bi[i] =(bi_rgbs[i]&0x000000ff);
                } 
       set_r_g_b_tab(this.r, this.g, this.b);// uzupełniam tablice r,g,b wartościami z _rgbs, czyli tym co jest w _img.getRGB

                for(int i=0; i<bi_n && i<this._n; i++){
                        this.r[i]= (this.r[i]+r_bi[i])/2;
                        this.g[i]= (this.r[i]+g_bi[i])/2;
                        this.b[i]= (this.r[i]+b_bi[i])/2;
                }    
       setRGBto_img(this.r, this.g, this.b,this._img);         
    }
    
    public void subPic(BufferedImage bi){
        ColorModel cm = _img.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = _img.copyData(null);
        _backup = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        int bi_w= bi.getWidth(),
            bi_h= bi.getHeight(),
            bi_n = (bi_w*bi_h);
        System.out.println(" obrazek ma rozmiar "+bi_n);
        int [] bi_rgbs = new int[bi_n],
               r_bi = new int[bi_n],
               g_bi = new int[bi_n],
               b_bi = new int[bi_n]; 
        bi.getRGB(0, 0, bi.getWidth(), bi.getHeight(), bi_rgbs, 0, bi.getWidth());
        //uzupełniam tablice wartościami kolorów
                for(int i=0;i<bi_n;i++) {
                    r_bi[i] =((bi_rgbs[i]&0x00ff0000)>>16);
                    g_bi[i] =((bi_rgbs[i]&0x0000ff00)>>8);
                    b_bi[i] =(bi_rgbs[i]&0x000000ff);
                }       
       set_r_g_b_tab(this.r, this.g, this.b);// uzupełniam tablice r,g,b wartościami z _rgbs, czyli tym co jest w _img.getRGB
        for(int i=0; i<bi_n && i<this._n; i++){
            this.r[i]= (Math.abs( (this.r[i]-r_bi[i]) ));
            this.g[i]= (Math.abs( (this.g[i]-g_bi[i]) ));
            this.b[i]= (Math.abs( (this.b[i]-b_bi[i]) ));
        }           
       setRGBto_img(this.r, this.g, this.b,this._img);  
    }
}

