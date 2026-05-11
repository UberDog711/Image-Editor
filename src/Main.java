import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

// Draws red circles when you click the mouse
// Uses a BufferedImage so the graphics don't disappear

class Main {

    GraphicsExamplePanel myGraphicsPanel;
    BufferedImage myImage;
    LoadButton loadButton;
    SaveButton saveButton;

    public void main() {
        myImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        loadButton = new LoadButton("Load Image");
        saveButton = new SaveButton("Save Image");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));    // try Y_AXIS
        

        myGraphicsPanel = new GraphicsExamplePanel(800, 600);

        buttonPanel.add(loadButton);
        buttonPanel.add(saveButton);

        mainPanel.add(buttonPanel,BorderLayout.WEST);
        mainPanel.add(myGraphicsPanel, BorderLayout.CENTER);





        JFrame myJFrame = new JFrame("Graphics Example 2");
        myJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        myJFrame.add(mainPanel);
        myJFrame.pack();

        myJFrame.setLocationRelativeTo(null);
        myJFrame.setVisible(true);
    }

    ///////////////////////////////////////////////////////////////////

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
            pen.drawImage(myImage, 0, 0, getWidth(), getHeight(), null);
        }

    }

    ///////////////////////////////////////////////////////////////////

    class MyMouseListener implements MouseListener, MouseMotionListener {
        private int lastPressedX;
        private int lastPressedY;
        private Tool currentTool = Tool.SCRIBBLE;

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
            int x = e.getX();
            int y = e.getY();
            Graphics pen = myImage.getGraphics();
            pen.setColor(Color.RED);
            int brushSize = 10;
            if (currentTool == Tool.POINT) {
                pen.fillOval(x-brushSize/2, y-brushSize/2, brushSize, brushSize);
            } if (currentTool == Tool.STRAIGHTLINE) {
                pen.drawLine(x,y,lastPressedX,lastPressedY);
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
            int x = e.getX();
            int y = e.getY();
            Graphics pen = myImage.getGraphics();
            pen.setColor(Color.RED);
            if (currentTool == Tool.SCRIBBLE) {
                pen.drawLine(x,y,lastPressedX,lastPressedY);
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

    ///////////////////////////////////////////////////////////////////

    class LoadButton extends JButton implements ActionListener
    {
        LoadButton(String name) {
            super(name);             // calls the super class (JButton) constructor
            addActionListener(this);       // adds this object (itself) as its own action listener
        }

        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showSaveDialog(myGraphicsPanel);
            if (JFileChooser.APPROVE_OPTION ==0) {
                try {
                    myImage = ImageIO.read(fileChooser.getSelectedFile());
                    myGraphicsPanel.repaint();

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    //////////////////////////////////////////////////////////////////

    class SaveButton extends JButton implements ActionListener
    {
        SaveButton(String name) {
            super(name);             // calls the super class (JButton) constructor
            addActionListener(this);       // adds this object (itself) as its own action listener
        }

        public void actionPerformed(ActionEvent e) {
            ;
        }
    }

    //////////////////////////////////////////////////////////////////

    class MySlider extends JSlider implements ChangeListener
    {
        MySlider() {
            super();
            addChangeListener(this);
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            ;
        }
    }
}