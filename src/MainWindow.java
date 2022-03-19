import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class MainWindow extends JFrame implements MouseMotionListener, ActionListener {

    BufferedImage buffer;
    Point origin;

    public MainWindow(String nom, int width, int height) {
        super(nom);
        setSize(width,height);
        setLocation(300,200);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        Insets insets = this.getInsets();
        Dimension dim = this.getSize();
        buffer = new BufferedImage(dim.width - insets.left - insets.right,
                dim.height - insets.top - insets.bottom, BufferedImage.TYPE_INT_RGB);

        Timer t = new Timer(1 / 60, this);
        t.start();

        this.addMouseMotionListener(this);

        System.out.println("Insets left: "+ insets.left + ", top: " + insets.top);
        System.out.println("Dim: " + getWidth() + ", " + getHeight());

        // Initialisation graphique
        origin = new Point(insets.left, insets.top);

    }

    public static void main(String[] args) {
        MainWindow w = new MainWindow("Game", 600, 600);
    }

    public void paint(Graphics g) {
        Insets insets = this.getInsets();
        g.drawImage(buffer, origin.x, origin.y, null);
    }

    private void drawToScreen(int x, int y) {
        Graphics g = buffer.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
        g.setColor(Color.orange);
        g.fillOval(135, 110, 30, 30);
        g.setColor(Color.magenta);
        g.fillOval(x, y, 25, 25);
    }

    public void actionPerformed(ActionEvent e) {

    }

    public void mouseDragged(MouseEvent e) { }

    public void mouseMoved(MouseEvent e) {
        Point p = new Point(e.getX() - origin.x, e.getY() - origin.y);

        drawToScreen(p.x, p.y);
        repaint();
    }
}