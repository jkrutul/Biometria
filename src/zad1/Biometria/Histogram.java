package zad1.Biometria;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.*;

public class Histogram{
    private JFrame picFrame, chgdFrame, bttFrame, histFrame, valuesFrame, tmpFrame;
    private JPanel bttPanel, valuesPanel;
    private ImgPanel _picPanel,_chgdPanel,tmpPanel;
    private HistPanel _histPanel;
    private MatrixPanel _matrixPanel;
    
    private int _dhist[];
    private BufferedImage _img,tmpImg;
    
    private JFileChooser _fc;
    
    private JLabel valueLabel, activeLabel;
    
    private Icon iconInv, iconCppx,
                iconNor, iconSpl,
                iconEro,iconDyla,
                iconSave;
    private JButton invcButton, cppxButton, 
                    norButton,  splButton,
                    eroButton,  dylaButton,
                    saveButton, undoButton,
                    progButton,brightenButton,
                    darkenButton, contIncButton,
                    contDecButton, grayButton,
                    matrixButton, valueButton,
                    activPixButton, sizeMatrixButton,
                    loadImageButton, equalizationButton,
                    expansionButton, otsuButton,
                    medianButton, gausButton,
                    addPicButton,   subPicButton,
                    undoOneButton;
    
    class Dialog extends JDialog {
        JTextField input;
        JButton button;
        JTextField awi;
        JTextField ahi;
        
        public Dialog(final String str)
        {
            setLayout(null);
            JLabel label=new JLabel();
            label.setLocation(110,0);
            label.setSize(300,60);
            awi = new JTextField("x");
            awi.setLocation(150, 60);
            awi.setSize(100,20);
            ahi = new JTextField("y");
            input=new JTextField();
            if(str.equals("activ")) {
                input.setVisible(false);
                awi.setVisible(true);
                ahi.setVisible(true);
            }
            else {
                input.setVisible(true);
                awi.setVisible(false);
                ahi.setVisible(false);
            }
            ahi.setLocation(260, 60);
            ahi.setSize(100,20);
            label.setText("Podaj dane:");
            input.setSize(100,20);
            input.setLocation(40, 60);
            button=new JButton("OK");
            button.setSize(60,30);
            button.setLocation(175, 100);
            button.addActionListener(new ActionListener() {
                @Override
            public void actionPerformed(ActionEvent e) {
                int value = 0;
                if(!input.getText().equals("")) value = Integer.parseInt(input.getText());
                    switch (str) {
                        case "value":
                            _chgdPanel.setValue(value);
                            valueLabel.setText("Value: " + Integer.toString(value));
                            break;
                        case "activ":
                            _chgdPanel.setActivPixel(Integer.parseInt(awi.getText()), Integer.parseInt(ahi.getText()));
                            activeLabel.setText("<html>Active X: " + awi.getText() + "<br>Active Y: " + ahi.getText() + "</html>");
                            break;
                        case "matrix":
                            _matrixPanel.setSize(value);
                    }
                setVisible(false);
            }});
            add(ahi);
            add(awi);
            add(label);
            add(input);
            add(button);
            setVisible(true);
        }
    }
    
    public Histogram() {
        initComponents();
    }

    private void initComponents() {
        _matrixPanel = new MatrixPanel(3);
        _matrixPanel.setLocation(20, 180);
        _matrixPanel.setSize(250, 250);
        
        //BUTTONS
        loadImageButton = new JButton("Load image");
        invcButton = new JButton("invc");
        cppxButton = new JButton("cppx");
        norButton = new JButton("normalizacja");
        equalizationButton = new JButton("wyrownanie");
        expansionButton = new JButton("rozciaganie");
        splButton = new JButton("splaszczenie");
        eroButton = new JButton("erozja");
        dylaButton = new JButton("dylatacja");
        grayButton = new JButton("grayscale");
        progButton = new JButton("Progowanie");
        otsuButton = new JButton("Otsu");
        medianButton = new JButton("Median");
        gausButton = new JButton("Gaussian");
        saveButton = new JButton("zapisz");
        undoOneButton = new JButton("cofnij");
        undoButton = new JButton("cofnij wszystko");
        brightenButton = new JButton("Brightness >>");
        darkenButton = new JButton("Brightness <<");
        contIncButton = new JButton("Contrast >>");
        contDecButton = new JButton("Contrast <<");
		addPicButton = new JButton("dodawanie obrazu");
        subPicButton = new JButton("odejmowanie obrazu");
        
        matrixButton = new JButton("Load Matrix");
        matrixButton.setLocation(20, 140);
        matrixButton.setSize(120, 30);
        valueButton = new JButton("Value");
        valueButton.setLocation(20, 20);
        valueButton.setSize(120, 30);
        valueLabel = new JLabel("Value: 0");
        valueLabel.setLocation(160, 20);
        valueLabel.setSize(120, 30);
        activPixButton = new JButton("Activ Pixel");
        activPixButton.setLocation(20, 60);
        activPixButton.setSize(120, 30);
        activeLabel = new JLabel("<html>Active X: 0<br>Active Y: 0</html>");
        activeLabel.setLocation(160, 60);
        activeLabel.setSize(120, 30);
        sizeMatrixButton = new JButton("Matrix Size");
        sizeMatrixButton.setLocation(20, 100);
        sizeMatrixButton.setSize(120, 30);
        /*
          //ICONS FOR BUTTONS   
        iconInv = new ImageIcon("invert.png");
        iconCppx = new ImageIcon("cpypx.png");
        iconNor = new ImageIcon("normalize.png");
        iconSpl = new ImageIcon("splot.png");
        iconEro = new ImageIcon("erozja.png");
        iconDyla = new ImageIcon("dylatacja.png");
        iconSave = new ImageIcon("save.png");       
                
        invcButton = new JButton(iconInv);
        cppxButton = new JButton(iconCppx);
        norButton = new JButton(iconNor);
        splButton = new JButton(iconSpl);
        eroButton = new JButton(iconEro);
        dylaButton = new JButton(iconDyla);
        saveButton = new JButton(iconSave);
        
        
        //SET BUTTONS SIZE
      
        invcButton.setPreferredSize(new Dimension(32,32));
        cppxButton.setPreferredSize(new Dimension(32,32));
        norButton.setPreferredSize(new Dimension(32,32));
        splButton.setPreferredSize(new Dimension(32,32));
        eroButton.setPreferredSize(new Dimension(32,32));
        dylaButton.setPreferredSize(new Dimension(32,32));
        saveButton.setPreferredSize(new Dimension(32,32));
        */
        
        valuesPanel= new JPanel();
        valuesPanel.setLayout(null);
        valuesPanel.setPreferredSize(new Dimension(300,500));
        valuesPanel.setMinimumSize(new Dimension(400, 580));
        valuesPanel.setMaximumSize(new Dimension(400, 580));
        valuesPanel.setBorder(BorderFactory.createTitledBorder("Values tool"));
        valuesPanel.add(valueButton);
        valuesPanel.add(valueLabel);
        valuesPanel.add(activPixButton);
        valuesPanel.add(activeLabel);
        valuesPanel.add(sizeMatrixButton);
        valuesPanel.add(_matrixPanel);
        valuesPanel.add(matrixButton);
        
        //BUTTONS PANEL
        bttPanel= new JPanel();
        bttPanel.setPreferredSize(new Dimension(65,800));
        bttPanel.setMinimumSize(new Dimension(320, 680));
        bttPanel.setMaximumSize(new Dimension(320, 680));
        bttPanel.setBorder(BorderFactory.createTitledBorder("Choose tool"));
        bttPanel.add(loadImageButton);
        bttPanel.add(invcButton); 
        bttPanel.add(cppxButton); 
        bttPanel.add(norButton);
        bttPanel.add(equalizationButton);
        bttPanel.add(expansionButton);
        bttPanel.add(splButton); 
        bttPanel.add(eroButton); 
        bttPanel.add(dylaButton);
        bttPanel.add(grayButton);
        bttPanel.add(progButton);
        bttPanel.add(otsuButton);
        bttPanel.add(medianButton);
        bttPanel.add(gausButton);
        bttPanel.add(saveButton);
        bttPanel.add(undoOneButton);
        bttPanel.add(undoButton);
        bttPanel.add(brightenButton);
        bttPanel.add(darkenButton);
        bttPanel.add(contIncButton);
        bttPanel.add(contDecButton);
		bttPanel.add(addPicButton);
        bttPanel.add(subPicButton);

        //wczytanie obrazka, inicjalizacja ImgPanel 
        //_picPanel = new ImgPanel("grafa.png");
        //_chgdPanel = new ImgPanel("grafa.png");
        String pathToImage = "lena.jpg";
        _picPanel = new ImgPanel(pathToImage);
        _chgdPanel = new ImgPanel(pathToImage);
		tmpPanel = new ImgPanel(pathToImage);
        
        
        setHist();
        
        //setFilter(0); //0 - invertcolor,1 - cupy pixelbypixel, 2 - normalizacja, 3 - splot, 4-erozja, 5-dylacja
        
        loadImageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {                
                if (_fc == null) _fc = new JFileChooser();
                int returnVal = _fc.showDialog(bttPanel, "Open");
                File file = _fc.getSelectedFile();
                _img=_picPanel.setPic(file);
                chgdFrame.setSize(_img.getWidth(), _img.getHeight());
                picFrame.setSize(_img.getWidth(), _img.getHeight());
                _chgdPanel.setPic(file);
                _chgdPanel.repaint();
                _picPanel.repaint();
                 _fc.setSelectedFile(null);
            }
        });
        
        sizeMatrixButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Dialog dialog = new Dialog("matrix");
                dialog.setLocation(200,200);
                dialog.setVisible(true);
                dialog.setSize(400, 200);
            }
        });
        
        matrixButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _chgdPanel.setMatrix(_matrixPanel.getMatrix());
            }
        });
        
       valueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Dialog dialog = new Dialog("value");
                dialog.setLocation(200,200);
                dialog.setVisible(true);
                dialog.setSize(400, 200);
            }
        }); 
       
       activPixButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Dialog dialog = new Dialog("activ");
                dialog.setLocation(200,200);
                dialog.setVisible(true);
                dialog.setSize(400, 200);
            }
        }); 
        
       saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _chgdPanel.saveoutPic("save.png");
            }
        });
       invcButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _chgdPanel.filter(0);
                _chgdPanel.repaint();
                _histPanel.clear(_chgdPanel.getImg());
                _histPanel.repaint();
            }
        });
       cppxButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _chgdPanel.filter(1);
                _chgdPanel.repaint();
                _histPanel.clear(_chgdPanel.getImg());
                _histPanel.repaint();
            }
        });
       norButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _chgdPanel.filter(2);
                _chgdPanel.repaint();
                _histPanel.clear(_chgdPanel.getImg());
                _histPanel.repaint();
            }
        });
       equalizationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _chgdPanel.filter(8);
                _chgdPanel.repaint();
                _histPanel.clear(_chgdPanel.getImg());
                _histPanel.repaint();
            }
        });
       expansionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _chgdPanel.filter(9);
                _chgdPanel.repaint();
                _histPanel.clear(_chgdPanel.getImg());
                _histPanel.repaint();
            }
        });
       splButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _chgdPanel.filter(3);
                _chgdPanel.repaint();
                _histPanel.clear(_chgdPanel.getImg());
                _histPanel.repaint();
            }
        });
       eroButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _chgdPanel.filter(4);
                _chgdPanel.repaint();
                _histPanel.clear(_chgdPanel.getImg());
                _histPanel.repaint();
            }
        });   
       dylaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _chgdPanel.filter(5);
                _chgdPanel.repaint();
                _histPanel.clear(_chgdPanel.getImg());
                _histPanel.repaint();
            }
        });
       grayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _chgdPanel.filter(7);
                _chgdPanel.repaint();
                _histPanel.clear(_chgdPanel.getImg());
                _histPanel.repaint();
            }
        }); 
       progButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _chgdPanel.filter(6);
                _chgdPanel.repaint();
                _histPanel.clear(_chgdPanel.getImg());
                _histPanel.repaint();
            }
        });
       otsuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _chgdPanel.filter(10);
                _chgdPanel.repaint();
                _histPanel.clear(_chgdPanel.getImg());
                _histPanel.repaint();
            }
        });
       medianButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _chgdPanel.filter(11);
                _chgdPanel.repaint();
                _histPanel.clear(_chgdPanel.getImg());
                _histPanel.repaint();
            }
        });
       gausButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _chgdPanel.filter(12);
                _chgdPanel.repaint();
                _histPanel.clear(_chgdPanel.getImg());
                _histPanel.repaint();
            }
        });
       undoOneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _chgdPanel.back();
                _chgdPanel.repaint();
                _histPanel.clear(_chgdPanel.getImg());
                _histPanel.repaint();
            }
      }); 
       undoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _chgdPanel.clear();
                _chgdPanel.repaint();
                _picPanel.repaint();
                _histPanel.clear(_chgdPanel.getImg());
                _histPanel.repaint();
            }
      });     
       
       brightenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _chgdPanel.brighten = true;
                _chgdPanel.changeOffSet();
                System.out.println(_chgdPanel.offset + "=offset");
                _chgdPanel.rescale();
                _chgdPanel.repaint();
            }
      });
      
       darkenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _chgdPanel.brighten = false;
                _chgdPanel.changeOffSet();
                System.out.println(_chgdPanel.offset + "=offset");
                _chgdPanel.rescale();
                _chgdPanel.repaint();
            }
      });
       
       contIncButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _chgdPanel.contrastInc = true;
                _chgdPanel.changeScaleFactor();
                System.out.println(_chgdPanel.scaleFactor + "=scaleF");
                _chgdPanel.rescale();
                _chgdPanel.repaint();
            }
      });
        
       contDecButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _chgdPanel.contrastInc = false;
                _chgdPanel.changeScaleFactor();
                System.out.println(_chgdPanel.scaleFactor + "=scaleF");
                _chgdPanel.rescale();
                _chgdPanel.repaint();
            }
      });
       addPicButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (_fc == null) _fc = new JFileChooser();
                int returnVal = _fc.showDialog(bttPanel, "Open file for addition");
                File file = _fc.getSelectedFile();
                tmpImg=tmpPanel.setPic(file);          
                tmpFrame.setSize(tmpImg.getWidth(), tmpImg.getHeight());
                tmpFrame.setVisible(true);
                tmpPanel.setPic(file);
                tmpPanel.repaint();
                 _fc.setSelectedFile(null);
                 _chgdPanel.addPic(tmpImg);
                 _chgdPanel.repaint();
                
            }
      });
       subPicButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (_fc == null) _fc = new JFileChooser();
                int returnVal = _fc.showDialog(bttPanel, "Open file for substraction");
                File file = _fc.getSelectedFile();
                tmpImg=tmpPanel.setPic(file);          
                tmpFrame.setSize(tmpImg.getWidth(), tmpImg.getHeight());
                tmpFrame.setVisible(true);
                tmpPanel.setPic(file);
                tmpPanel.repaint();
                 _fc.setSelectedFile(null);
                _chgdPanel.subPic(tmpImg);
                _chgdPanel.repaint();
                
            }
      });
 
        picFrame= new JFrame("input picture");
        chgdFrame= new JFrame("output picture");
        histFrame= new JFrame("histogram");
        bttFrame= new JFrame("button panel");
        valuesFrame= new JFrame("values");
	    tmpFrame= new JFrame("tmpFrame");
         //CONTAINER
        //content= picFrame.getContentPane();
        picFrame.setLayout(new BorderLayout());
        chgdFrame.setLayout(new BorderLayout());
        histFrame.setLayout(new BorderLayout());
        bttFrame.setLayout(new BorderLayout());
        valuesFrame.setLayout(new BorderLayout());
       
        histFrame.setSize(1080, 500);
        
        picFrame.add(_picPanel);
        chgdFrame.add(_chgdPanel);
        histFrame.add(_histPanel);
        bttFrame.add(bttPanel);
        valuesFrame.add(valuesPanel);
		tmpFrame.add(tmpPanel);
        
        //picFrame
        
        picFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chgdFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        histFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bttFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        valuesFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   
        //picFrame.setLayout(null);
        picFrame.setVisible(true);
        chgdFrame.setVisible(true);
        histFrame.setVisible(true);
        bttFrame.setVisible(true);
        valuesFrame.setVisible(true);
		tmpFrame.setVisible(false);
        
        picFrame.pack();
        chgdFrame.pack();
        //histFrame.pack();
        bttFrame.pack();
        valuesFrame.pack();
        
        picFrame.setLocation(140, 0);
        chgdFrame.setLocation(680, 0);
        histFrame.setLocation(140, 600);
        valuesFrame.setLocation(1220, 0);
    }

    
    //ustawienie filtra dla _chgdPanel
    private void setFilter(int x){
         _chgdPanel.filter(x); 
    }
    private void setHist(){
        _histPanel = new HistPanel(_chgdPanel.getImg());
        _histPanel.setSize(450, 400);
        _histPanel.repaint();
    }
    
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Histogram h = new Histogram();
            }
        });
    }  
}