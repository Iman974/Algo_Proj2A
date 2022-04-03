import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class MainWindow extends JFrame implements MouseMotionListener, ActionListener {

    private static Physics physics;

    BufferedImage buffer;
    Graphics bufferG;
    Point origin;
    private static int frameCounter;

    public MainWindow(String nom, int width, int height) {
        super(nom);
        setSize(width, height);
        setLocation(300, 200);

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
        // NE PAS TOUCHER : 17 ms d'intervalle correspond à 60 IMAGES PAR SECONDES
        Timer t = new Timer(100, this);
        t.start();

        this.addMouseMotionListener(this);
    }

    public static int getFrame() {
        return frameCounter;
    }

    public static void main(String[] args) {
        physics = new Physics();
        MainWindow w = new MainWindow("Game", 600, 600);
    }

    // Met a jour l'affichage de toutes les particules a l'ecran, en prenant en compte leurs nouvelles positions
    public void updateScreen() {
        clearScreen();
        for (Particle p : physics.allParticles) {
            drawParticle(p);
        }
        repaint();
    }

    public void paint(Graphics g) {
        g.drawImage(buffer, origin.x, origin.y, null);
    }

    // Efface l'ecran en le remplissant par un fond noir
    private void clearScreen() {
        bufferG.setColor(Color.black);
        bufferG.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
    }

    private void drawParticle(Particle p) {
        bufferG.setColor(p.color);
        Point2D.Double imgSize = new Point2D.Double(p.img.getWidth(null), p.img.getHeight(null));

        // Ramene la position en haut à gauche de l'image. C'est l'origine de tout dessin.
        int x = (int)(p.position.x - imgSize.x / 2.0);
        int y = (int)(p.position.y - imgSize.y / 2.0);

        bufferG.fillOval(x, y, 25, 25);

        // Dessine le collider
//        bufferG.setColor(Color.GREEN);
//        bufferG.drawOval((int)(p.position.x - p.COLLIDER_RADIUS), (int)(p.position.y - p.COLLIDER_RADIUS),
//                p.COLLIDER_RADIUS * 2, p.COLLIDER_RADIUS * 2);
    }

    // Appelée à chaque frame
    public void actionPerformed(ActionEvent e) {
        updateScreen();
        physics.updateScene(buffer.getWidth());
        frameCounter++;
    }

    public void mouseDragged(MouseEvent e) {
    }

    // En test
    public void mouseMoved(MouseEvent e) {
        Point p = new Point(e.getX() - origin.x, e.getY() - origin.y);

//        drawToScreen(p.x, p.y);
//        repaint();
    }
}
