/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rasterpaint;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author artamonov
 */
public class RasterPaint extends JFrame {

    private enum Modiefier {

        BRUSH, ERASER, RECTANGLE, CIRCLE, TRIANGLE, CLEAR, COLOR, SAVE
    }
    private Canvas canvasPanel;
    private BufferedImage canvasBuffered;
    private Graphics2D graphicsBufferedImage;
    private Graphics2D graphCanvas;
    private JPanel buttonPanel;
    private final ActionListener clickListener;
    private Modiefier chosenTool;
    private JButton brushBtn;
    private JButton eraserBtn;
    private JButton rectangleBtn;
    private JButton circleBtn;
    private JButton triangleBtn;
    private JButton clearBtn;
    private JButton choosenColorBtn;
    private JButton saveBtn;
    private Color choosenBtnColor;
    private final Color defaultBtnColor;
    private Color backgraundColor;
    private Color myColor;
    private JLabel priceLabel;
    private final Random rnd;
    private int price;
    private int X;
    private int Y;

    public RasterPaint() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //начальные значения
        choosenBtnColor = new Color(176, 196, 222);
        defaultBtnColor = new Color(255, 99, 71);
        backgraundColor = Color.WHITE;
        myColor = Color.RED;
        price = 0;
        rnd = new Random(System.currentTimeMillis());
        clickListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton tempBtn = (JButton) e.getSource();
                clearFontButton();
                tempBtn.setForeground(choosenBtnColor);
                chosenTool = Modiefier.valueOf(tempBtn.getText());
                switch (chosenTool) {
                    case CLEAR:
                        clear();
                        break;
                    case ERASER:
                        graphicsBufferedImage.setColor(backgraundColor);
                        break;
                    case SAVE:
                        saveImage();
                        break;
                    default:
                        graphicsBufferedImage.setColor(myColor);
                        break;
                }
            }
        };
        createGui();
        chosenTool = Modiefier.BRUSH;
        brushBtn.setForeground(choosenBtnColor);
    }

    private void clearFontButton() {
        brushBtn.setForeground(defaultBtnColor);
        eraserBtn.setForeground(defaultBtnColor);
        rectangleBtn.setForeground(defaultBtnColor);
        circleBtn.setForeground(defaultBtnColor);
        triangleBtn.setForeground(defaultBtnColor);
        clearBtn.setForeground(defaultBtnColor);
        choosenColorBtn.setForeground(defaultBtnColor);
        saveBtn.setForeground(defaultBtnColor);
    }

    private void clear() {
        graphicsBufferedImage.setColor(backgraundColor);
        graphicsBufferedImage.fillRect(0, 0, canvasBuffered.getWidth(), canvasBuffered.getHeight());
        canvasPanel.repaint();
        graphicsBufferedImage.setColor(myColor);
        priceLabel.setText("<html><h3>PRICE:</h3> 0 </html>");
    }

    private void createGui() {
        //установка фрейма
        setTitle("RasterPaint 10.1");
        setResizable(false);
        setBounds(200, 100, 1000, 600);
        setLayout(null);
        //панель с кнопками
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setSize(120, getHeight());
        buttonPanel.setBackground(new Color(200, 245, 109));
        add(buttonPanel);
        brushBtn = new JButton(Modiefier.BRUSH.name());
        changeButton(brushBtn);
        eraserBtn = new JButton(Modiefier.ERASER.name());
        changeButton(eraserBtn);
        rectangleBtn = new JButton(Modiefier.RECTANGLE.name());
        changeButton(rectangleBtn);
        circleBtn = new JButton(Modiefier.CIRCLE.name());
        changeButton(circleBtn);
        triangleBtn = new JButton(Modiefier.TRIANGLE.name());
        changeButton(triangleBtn);
        clearBtn = new JButton(Modiefier.CLEAR.name());
        changeButton(clearBtn);
        choosenColorBtn = new JButton(Modiefier.COLOR.name());
        choosenColorBtn.setForeground(defaultBtnColor);
        choosenColorBtn.setBackground(myColor);
        choosenColorBtn.setFont(new Font(null, Font.BOLD, 14));
        choosenColorBtn.setPreferredSize(new Dimension(buttonPanel.getWidth(), 30));
        choosenColorBtn.setMinimumSize(choosenColorBtn.getPreferredSize());
        choosenColorBtn.setMaximumSize(choosenColorBtn.getPreferredSize());
        buttonPanel.add(choosenColorBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        choosenColorBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myColor = JColorChooser.showDialog(RasterPaint.this, "Choose color", myColor);
            choosenColorBtn.setBackground(myColor);
            graphicsBufferedImage.setColor(myColor);
            choosenColorBtn.setBackground(myColor);
            }
        });
        saveBtn = new JButton(Modiefier.SAVE.name());
        changeButton(saveBtn);

//пример как прикрепить картинку----
//        try {
//            File file = new File("price.png");
//            System.out.println(file.getAbsolutePath());
//            BufferedImage advertImage = ImageIO.read(file);
//            Canvas advertCanvas = new Canvas() {
//                public void paint(Graphics g) {
//                    g.drawImage(advertImage, 0, 0, null);
//                }
//            };
//            advertCanvas.setPreferredSize(new Dimension(250, advertImage.getHeight()));
//            advertCanvas.setMaximumSize(advertCanvas.getPreferredSize());
//buttonPanel.add(advertCanvas);
//         } catch (IOException ex) {
//            Logger.getLogger(RasterPaint.class.getName()).log(Level.SEVERE, null, ex);
//        }
        //промежуток между последней кнопкой и ценой
        buttonPanel.add(Box.createRigidArea(new Dimension(0, buttonPanel.getHeight() - 10 * 30 - 7 * 5 - 30)));
        priceLabel = new JLabel("<html><h3>PRICE:</h3>" + price + "</html>");
        priceLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
        buttonPanel.add(priceLabel);

        //price
        //растровый буфер
        canvasBuffered = new BufferedImage(this.getWidth() - buttonPanel.getWidth(), buttonPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
        graphicsBufferedImage = (Graphics2D) canvasBuffered.getGraphics();
        graphicsBufferedImage.setColor(myColor);
        graphicsBufferedImage.setStroke(new BasicStroke(3.0f));
        graphicsBufferedImage.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //канва - на нее выводим растровый буфер
        canvasPanel = new Canvas() {

            @Override
            public void paint(Graphics g) {
                g.drawImage(canvasBuffered, 0, 0, this);
            }

            //эта функция заливала картинку фоном, из-за чего были мерцания -> закомментируем super. - на не нужна заливка фоном(лишнее мерцание)
            @Override
            public void update(Graphics g) {
                paint(g);
            }
        };
        // canvasPanel.setFocusTraversalKeysEnabled(true);
        canvasPanel.setLocation(buttonPanel.getWidth(), 0);
        canvasPanel.setSize(canvasBuffered.getWidth(), canvasBuffered.getHeight());
        graphCanvas = (Graphics2D) canvasPanel.getGraphics();
        add(canvasPanel);

        createListener();
        //очистка фона
        clear();
    }

    private void updatePrice() {
        if (chosenTool == Modiefier.BRUSH || chosenTool == Modiefier.CIRCLE || chosenTool == Modiefier.RECTANGLE
                || chosenTool == Modiefier.TRIANGLE || chosenTool == Modiefier.ERASER) {
            price = (int) (rnd.nextInt(55555) / (rnd.nextInt(1000) + 1));
            priceLabel.setText("<html><h3>PRICE:</h3>" + price + "</html>");
        }
    }

    private void changeButton(JButton btn) {
        btn.setContentAreaFilled(false);
        btn.setForeground(defaultBtnColor);
        btn.setFont(new Font(null, Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(buttonPanel.getWidth(), 30));
        btn.setMinimumSize(btn.getPreferredSize());
        btn.setMaximumSize(btn.getPreferredSize());
        btn.addActionListener(clickListener);
        buttonPanel.add(btn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private void createListener() {
        canvasPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                X = e.getX();
                Y = e.getY();
                graphCanvas = (Graphics2D) canvasPanel.getGraphics();
                graphCanvas.setColor(myColor);
                graphCanvas.setStroke(new BasicStroke(3.0f));
                graphCanvas.drawOval(Math.min(X, e.getX()), Math.min(Y, e.getY()), Math.abs(X - e.getX()), Math.abs(Y - e.getY()));
                switch (chosenTool) {
                    case BRUSH:
                        graphicsBufferedImage.fillRect(e.getX(), e.getY(), 6, 6);
                        canvasPanel.paint(canvasPanel.getGraphics());
                        break;
                    case ERASER:
                        graphicsBufferedImage.fillRect(e.getX(), e.getY(), 6, 6);
                        canvasPanel.paint(canvasPanel.getGraphics());
                        break;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                switch (chosenTool) {
                    case BRUSH:
                        break;
                    case ERASER:
                        break;
                    case RECTANGLE:
                        graphicsBufferedImage.drawRect(Math.min(X, e.getX()), Math.min(Y, e.getY()), Math.abs(X - e.getX()), Math.abs(Y - e.getY()));
                        canvasPanel.paint(canvasPanel.getGraphics()); //короче вызвать репеинт, но он последовательно вызывает
                        //сначала апдейт(), после пеинт
                        break;
                    case CIRCLE:
                        graphicsBufferedImage.drawOval(Math.min(X, e.getX()), Math.min(Y, e.getY()), Math.abs(X - e.getX()), Math.abs(Y - e.getY()));
                        canvasPanel.paint(canvasPanel.getGraphics());
                        break;
                    case TRIANGLE:
                        int x2 = (int) (e.getX() + Math.abs(Y - e.getY()) * Math.tan(0.785));
                        int x3 = (int) (e.getX() - Math.abs(Y - e.getY()) * Math.tan(0.785));
                        ;
                        graphicsBufferedImage.drawPolygon(new int[]{X, x2, x3, X}, new int[]{Y, e.getY(), e.getY(), Y}, 4);
                        canvasPanel.paint(canvasPanel.getGraphics());
                        break;
                    case CLEAR:
                        break;

                }
                updatePrice();  //нарисовали - обновили цену
            }
        });
        canvasPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                switch (chosenTool) {
                    case BRUSH:
                        graphicsBufferedImage.fillRect(e.getX(), e.getY(), 6, 6);
                        canvasPanel.paint(canvasPanel.getGraphics());
                        break;
                    case ERASER:
                        graphicsBufferedImage.fillRect(e.getX(), e.getY(), 6, 6);
                        canvasPanel.paint(canvasPanel.getGraphics());
                        break;
                    case RECTANGLE:
                        canvasPanel.paint(canvasPanel.getGraphics());
                        graphCanvas.drawRect(Math.min(X, e.getX()), Math.min(Y, e.getY()), Math.abs(X - e.getX()), Math.abs(Y - e.getY()));
                        break;
                    case CIRCLE:
                        canvasPanel.paint(canvasPanel.getGraphics());
                        graphCanvas.drawOval(Math.min(X, e.getX()), Math.min(Y, e.getY()), Math.abs(X - e.getX()), Math.abs(Y - e.getY()));
                        break;
                    case TRIANGLE:
                        canvasPanel.paint(canvasPanel.getGraphics());
                        int x2 = (int) (e.getX() + Math.abs(Y - e.getY()) * Math.tan(0.785));
                        int x3 = (int) (e.getX() - Math.abs(Y - e.getY()) * Math.tan(0.785));
                        graphCanvas.drawPolygon(new int[]{X, x2, x3, X}, new int[]{Y, e.getY(), e.getY(), Y}, 4);
                        break;
                    case CLEAR:
                        break;
                }
            }
        });
    }

    private void saveImage() {
        JFileChooser jfc = new JFileChooser();
        //jfc.removeChoosableFileFilter(new FileNameExtensionFilter("All Files", ""));
        jfc.addChoosableFileFilter(new FileNameExtensionFilter(".jpg", "jpg"));
        jfc.addChoosableFileFilter(new FileNameExtensionFilter(".png", "png"));
        jfc.addChoosableFileFilter(new FileNameExtensionFilter(".gif", "gif"));
        //какой тип выбран начальным
        jfc.setFileFilter(jfc.getChoosableFileFilters()[1]);
        //открываем и проверяем, что открылось все норм
        int retVal = jfc.showSaveDialog(null);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            //File f = jfc.getSelectedFile();
            
            String exstensionWithPoint = jfc.getFileFilter().getDescription().replace("All Files", "");
            String path = jfc.getSelectedFile().getAbsolutePath() + exstensionWithPoint;

            String exstensionWithoutPoint;
            switch (exstensionWithPoint) {
                case ".jpg":
                    exstensionWithoutPoint = "jpg";
                    break;
                case ".png":
                    exstensionWithoutPoint = "png";
                    break;
                case ".gif":
                    exstensionWithoutPoint = "gif";
                    break;
                default:
                    exstensionWithoutPoint = "jpg";
                    break;
            }

            try {
                ImageIO.write(canvasBuffered, exstensionWithoutPoint, new File(path));
                System.out.println(path);
            } catch (IOException ex) {
                Logger.getLogger(RasterPaint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {
        RasterPaint paint = new RasterPaint();
        paint.setVisible(true);
    }
}
