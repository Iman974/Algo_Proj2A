import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

public class GameArea extends JPanel implements MouseMotionListener, MouseListener {

    BufferedImage buffer;
    Graphics bufferG;

    Point launcherPosition;

    static int width;
    static int height;

    // TODO: déplacer cette enum dans une classe plus pertinente
    private enum Anchor {
        BOTTOM_MIDDLE,
        CENTER
    }

    public GameArea(int width, int height) {
        setBounds(0, 0, width, height);

        GameArea.width = width;
        GameArea.height = height;

        this.launcherPosition = new Point(width / 2, height - 70);

        this.buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.bufferG = buffer.getGraphics();
        setLayout(null);

        addMouseMotionListener(this);
        addMouseListener(this);

//        this.aim = new Image() {
//            @Override
//            public int getWidth(ImageObserver observer) {
//                return 25;
//            }
//
//            @Override
//            public int getHeight(ImageObserver observer) {
//                return 50;
//            }
//
//            @Override
//            public ImageProducer getSource() {
//                return null;
//            }
//
//            @Override
//            public Graphics getGraphics() { return null; }
//
//            @Override
//            public Object getProperty(String name, ImageObserver observer) {
//                return null;
//            }
//        };
    }

//    Image aim;
//    Point aimPosition = new Point(200, 200);

    public void paintComponent(Graphics g) {
        // super.paintComponent(g); // TODO: Utile à conserver ?
        g.drawImage(buffer, 0, 0, null);

        // Dessine le viseur
//        final int launcherDim = 50;
//        g.drawRect(launcherPosition.x - launcherDim / 2, launcherPosition.y - launcherDim / 2, launcherDim, launcherDim); // Launcher
//        g.setColor(Color.green);
//        int w = aim.getWidth(null); int h = aim.getWidth(null);
//        Point position = transpose(aimPosition.x, aimPosition.y, w, h, Anchor.BOTTOM_MIDDLE);
//        g.drawRect(position.x, position.y, w, h);

        // Dessine le rayon d'interaction pour chaque particule
//        g.setColor(Color.green);
//        final int radius = Particle.FORCE_RADIUS;
//        for (Particle p : Physics.allParticles) {
//            g.drawOval((int)(p.position.x - radius), (int)(p.position.y - radius), radius * 2, radius * 2);
//        }
    }

    private Point transpose(int x, int y, int width, int height, Anchor anchor) {
        Point result = new Point();
        switch (anchor) {
            case BOTTOM_MIDDLE -> {
                result.x = (int) (x - width / 2.0); // TODO: check diff with no casting
                result.y = y - height;
            }
            case CENTER -> {
                result.x = x - width / 2;
                result.y = y - height / 2;
            }
        }
        return result;
    }

    // Met a jour l'affichage de toutes les particules a l'ecran, en prenant en compte leurs nouvelles positions
    public void updateScreen() {
        clearScreen();
        for (Particle p : Physics.allParticles) {
            drawParticle(p);
        }
        repaint();
    }

    // Efface l'ecran en le remplissant par un fond noir
    private void clearScreen() {
        bufferG.setColor(Color.black);
        bufferG.fillRect(0, 0, width, height);
    }

    private void drawParticle(Particle p) {
        bufferG.setColor(p.color);
        Point particleSize = new Point(p.img.getWidth(null), p.img.getHeight(null));

        // Ramene la position en haut à gauche de l'image. C'est l'origine de tout dessin.
        int x = (int)(p.position.x - particleSize.x / 2.0);
        int y = (int)(p.position.y - particleSize.y / 2.0);

        bufferG.fillOval(x, y, 25, 25);

        // Dessine le collider
//        bufferG.setColor(Color.GREEN);
//        bufferG.drawOval((int)(p.position.x - p.COLLIDER_RADIUS), (int)(p.position.y - p.COLLIDER_RADIUS),
//                p.COLLIDER_RADIUS * 2, p.COLLIDER_RADIUS * 2);
    }

    public void mouseMoved(MouseEvent e) {
//        System.out.println(p);
    }

    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();
        System.out.println("Mouse clicked! at " + MainWindow.getFrame());

        // Spawn une particule dans la direction du launcher vers la souris
//        Point2D.Double direction = new Point2D.Double(p.x - launcherPosition.x, p.y - launcherPosition.y);
//        double l = direction.distance(0, 0);
//        direction.x /= l * 0.1;
//        direction.y /= l * 0.1;
//        Point2D.Double startSpeed = direction;
//        Physics.createParticle(SelectionBar.selectedType, launcherPosition.x, launcherPosition.y, 0.02,
//                50, true, Color.ORANGE, startSpeed);
    }

    public void mouseDragged(MouseEvent e) { }

    public void mousePressed(MouseEvent e) { }

    public void mouseReleased(MouseEvent e) { }

    public void mouseEntered(MouseEvent e) { }

    public void mouseExited(MouseEvent e) { }
}
