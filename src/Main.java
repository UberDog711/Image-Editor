import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

// Draws red circles when you click the mouse
// Uses a BufferedImage so the graphics don't disappear

class Main {

    GraphicsExamplePanel myGraphicsPanel;
    BufferedImage myImage;
    LoadButton loadButton;
    SaveButton saveButton;
    InvertButton invertButton;
    BlurButton blurButton;

    private int solvedHeight;
    private int solvedWidth;

    public void main() {
        myImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        loadButton = new LoadButton("Load Image");
        saveButton = new SaveButton("Save Image");
        invertButton = new InvertButton("Invert Image");
        blurButton = new BlurButton("Blur Image");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));    // try Y_AXIS


        myGraphicsPanel = new GraphicsExamplePanel(800, 600);

        buttonPanel.add(loadButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(invertButton);
        buttonPanel.add(blurButton);

        mainPanel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(myGraphicsPanel, BorderLayout.CENTER);


        JFrame myJFrame = new JFrame("Graphics Example 2");
        myJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        myJFrame.add(mainPanel);
        myJFrame.pack();

        myJFrame.setLocationRelativeTo(null);
        myJFrame.setVisible(true);
    }

    /// ////////////////////////////////////////////////////////////////

    class GraphicsExamplePanel extends JPanel {

        GraphicsExamplePanel(int width, int height) {
            setSize(width, height);
            setPreferredSize(new Dimension(width, height));
            MyMouseListener mouseListener = new MyMouseListener();
            this.addMouseListener(mouseListener);
            this.addMouseMotionListener(mouseListener);

        }

        @Override
        public void paintComponent(Graphics pen) {
            int panelHeight = getHeight();
            int panelWidth = getWidth();
            double panelRatio = (double) panelWidth / panelHeight;

            int imageHeight = myImage.getHeight();
            int imageWidth = myImage.getWidth();
            double imageRatio = (double) imageWidth / imageHeight;

            solvedHeight = panelHeight;
            solvedWidth = panelWidth;

            if (panelRatio > imageRatio) {
                solvedWidth = (int) (imageRatio * solvedHeight);
            } else if (panelRatio < imageRatio) {
                solvedHeight = (int) (solvedWidth / imageRatio);
            }

            pen.drawImage(myImage, 0, 0, solvedWidth, solvedHeight, null);
        }

    }

    /// ////////////////////////////////////////////////////////////////

    class MyMouseListener implements MouseListener, MouseMotionListener {
        private int lastPressedX;
        private int lastPressedY;
        private Tool currentTool = Tool.POINT;

        enum Tool {
            SCRIBBLE,
            POINT,
            STRAIGHTLINE
        }


        public void setTool(Tool tool) {
            currentTool = tool;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            double transform = (double) myImage.getWidth() / solvedWidth;
            int x = (int) (e.getX() * transform);
            int y = (int) (e.getY() * transform);


            Graphics pen = myImage.getGraphics();
            pen.setColor(Color.RED);
            int brushSize = 10;
            if (currentTool == Tool.POINT) {
                pen.fillOval((x - brushSize / 2), (y - brushSize / 2), (int) (brushSize * transform), (int) (brushSize * transform));
            }
            if (currentTool == Tool.STRAIGHTLINE) {
                pen.drawLine(x, y, lastPressedX, lastPressedY);
            }

            myGraphicsPanel.repaint();
            lastPressedX = x;
            lastPressedY = y;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            double transform = (double) myImage.getWidth() / solvedWidth;
            int x = (int) (e.getX() * transform);
            int y = (int) (e.getY() * transform);


            Graphics pen = myImage.getGraphics();
            pen.setColor(Color.RED);
            if (currentTool == Tool.SCRIBBLE) {
                pen.drawLine(x, y, lastPressedX, lastPressedY);
            }
            myGraphicsPanel.repaint();
            lastPressedX = x;
            lastPressedY = y;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }
    }

    /// ////////////////////////////////////////////////////////////////

    class LoadButton extends JButton implements ActionListener {
        LoadButton(String name) {
            super(name);             // calls the super class (JButton) constructor
            addActionListener(this);       // adds this object (itself) as its own action listener
        }

        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showSaveDialog(myGraphicsPanel);
            if (JFileChooser.APPROVE_OPTION == 0) {
                try {
                    myImage = ImageIO.read(fileChooser.getSelectedFile());
                    myGraphicsPanel.repaint();

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    /// ///////////////////////////////////////////////////////////////

    class SaveButton extends JButton implements ActionListener {
        SaveButton(String name) {
            super(name);             // calls the super class (JButton) constructor
            addActionListener(this);       // adds this object (itself) as its own action listener
        }

        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showSaveDialog(myGraphicsPanel);
            if (JFileChooser.APPROVE_OPTION == 0) {
                try {
                    ImageIO.write(myImage, "png", fileChooser.getSelectedFile());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    /// ///////////////////////////////////////////////////////////////

    class InvertButton extends JButton implements ActionListener {
        InvertButton(String name) {
            super(name);             // calls the super class (JButton) constructor
            addActionListener(this);       // adds this object (itself) as its own action listener
        }

        public void actionPerformed(ActionEvent e) {

            for (int x = 0; x < myImage.getWidth(); x++) {
                for (int y = 0; y < myImage.getHeight(); y++) {
                    Color pixelColor = new Color(myImage.getRGB(x, y));
                    Color newColor = new Color(
                            255 - pixelColor.getRed(),
                            255 - pixelColor.getGreen(),
                            255 - pixelColor.getBlue());
                    myImage.setRGB(x, y, newColor.getRGB());
                }

            }
            myGraphicsPanel.repaint();
        }
    }

    /// ///////////////////////////////////////////////////////////////

    class BlurButton extends JButton implements ActionListener {
        BlurButton(String name) {
            super(name);             // calls the super class (JButton) constructor
            addActionListener(this);       // adds this object (itself) as its own action listener
        }

        public void actionPerformed(ActionEvent e) {
            int[][] pixels = new int[myImage.getWidth()][myImage.getHeight()];
            for (int x = 0; x < myImage.getWidth(); x++) {
                for (int y = 0; y < myImage.getHeight(); y++) {
                    pixels[x][y] = blurPoint(x, y);
                }
            }

            for (int x = 0; x < myImage.getWidth(); x++) {
                for (int y = 0; y < myImage.getHeight(); y++) {
                    myImage.setRGB(x, y, pixels[x][y]);
                }
            }
            myGraphicsPanel.repaint();
        }

        public int blurPoint(int centerX, int centerY) {
            int l = 3;

            int totalR = 0;
            int totalG = 0;
            int totalB = 0;

            int count = 0;

            for (int x = centerX - l; x <= centerX + l; x++) {
                for (int y = centerY - l; y <= centerY + l; y++) {

                    if (x >= 0 && x < myImage.getWidth() &&
                            y >= 0 && y < myImage.getHeight()) {

                        Color pixelColor = new Color(myImage.getRGB(x, y));

                        totalR += pixelColor.getRed();
                        totalG += pixelColor.getGreen();
                        totalB += pixelColor.getBlue();

                        count++;
                    }
                }
            }

            int avgR = totalR / count;
            int avgG = totalG / count;
            int avgB = totalB / count;

            Color avgColor = new Color(avgR, avgG, avgB);

            return avgColor.getRGB();
        }
    }
}