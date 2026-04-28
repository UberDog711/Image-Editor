import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

// Draws red circles when you click the mouse
// Uses a BufferedImage so the graphics don't disappear

class Main {

    GraphicsExamplePanel myGraphicsPanel;
    BufferedImage myImage;
    JButton loadButton;
    JButton saveButton;

    public void main() {
        myImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        loadButton = new JButton("Load Image");
        saveButton = new JButton("Save Image");

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
            pen.drawImage(myImage, 0, 0, null);
        }

    }

    ///////////////////////////////////////////////////////////////////

    class MyMouseListener implements MouseListener, MouseMotionListener {

        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            Graphics pen = myImage.getGraphics();
            pen.setColor(Color.RED);
            int brushSize = 30;
            pen.fillOval(x-brushSize/2, y-brushSize/2, brushSize, brushSize);
            myGraphicsPanel.repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
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
}