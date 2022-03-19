import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class MainWindow extends JFrame implements MouseMotionListener, ActionListener {

    private static Physics physics;

    BufferedImage buffer;
    Graphics bufferG;
    Point origin;

    public MainWindow(String nom, int width, int height) {
        super(nom);
        setSize(width,height);
        setLocation(300,200);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Initialisation graphique
        Insets insets = this.getInsets();
        origin = new Point(insets.left, insets.top);

        Dimension dim = this.getSize();
        buffer = new BufferedImage(dim.width - insets.left - insets.right,
                dim.height - insets.top - insets.bottom, BufferedImage.TYPE_INT_RGB);
        this.bufferG = buffer.getGraphics();

        // Initialisation du timer pour les animations
        Timer t = new Timer(1 / 30, this);
        t.start();

        this.addMouseMotionListener(this);
    }

    public static void main(String[] args) {
        MainWindow w = new MainWindow("Game", 600, 600);
        physics = new Physics();
    }

    // Met a jour l'affichage de toutes les particules a l'ecran, en prenant en compte leurs nouvelles positions
    public void updateScreen() {
        clearScreen();
        for (Particle p : physics.particles) {
            drawParticle(p);
        }
    }

    public void paint(Graphics g) {
        Insets insets = this.getInsets();
        g.drawImage(buffer, origin.x, origin.y, null);
    }

    // Efface l'ecran en le remplissant par un fond noir
    private void clearScreen() {
        bufferG.setColor(Color.black);
        bufferG.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
    }

    private void drawParticle(Particle p) {
        bufferG.setColor(Color.magenta);

        // Ramene la position en haut à gauche de l'image. C'est l'origine de tout dessin.
        int x = p.position.x - p.img.getWidth(null) / 2;
        int y = p.position.y - p.img.getHeight(null) / 2;
        bufferG.fillOval(x, y, 25, 25);
    }

    // Appelée à chaque frame
    public void actionPerformed(ActionEvent e) {
        updateScreen();
    }

    public void mouseDragged(MouseEvent e) { }

    // En test
    public void mouseMoved(MouseEvent e) {
        Point p = new Point(e.getX() - origin.x, e.getY() - origin.y);

//        drawToScreen(p.x, p.y);
//        repaint();
    }
}