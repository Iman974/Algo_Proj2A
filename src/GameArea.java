import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

public class GameArea extends JPanel implements MouseMotionListener, MouseListener {

    private final BufferedImage buffer;
    private static Graphics bufferG;

    private Vector2D launcherOrigin;
    private Vector2D mousePosition;
    private BufferedImage aim;
    private Vector2D aimPosition;

    static int width;
    static int height;
    static int nbPoint;

    // TODO: déplacer cette enum dans une classe plus pertinente
    private enum Anchor {
        BOTTOM_MIDDLE,
        CENTER

    }

    public GameArea(int width, int height) {
        setBounds(0, 0, width, height);

        GameArea.width = width;
        GameArea.height = height;

        this.launcherOrigin = new Vector2D(width / 2.0, height - 70);
        this.aimPosition = new Vector2D();
        this.mousePosition = new Vector2D();

        this.buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.bufferG = buffer.getGraphics();
        setLayout(null);

        addMouseMotionListener(this);
        addMouseListener(this);
    }

    public void paintComponent(Graphics g) {
         super.paintComponent(g); // TODO: Utile à conserver ?

        // Dessine le viseur
        final int LAUNCHER_DIM = 50;
        Vector2D.Int launcherPos = transpose(launcherOrigin, LAUNCHER_DIM, LAUNCHER_DIM, Anchor.CENTER).toInt();
        bufferG.setColor(Color.DARK_GRAY);
        bufferG.drawRect(launcherPos.x, launcherPos.y, LAUNCHER_DIM, LAUNCHER_DIM);

        Vector2D direction = Vector2D.fromTo(launcherOrigin, mousePosition);
        direction.normalize();
        aimPosition = Vector2D.add(launcherOrigin, Vector2D.getScaled(direction, 65));
//        Vector2D.Int aimImgSize = new Vector2D.Int(aim.getWidth(null), aim.getHeight(null));
//        Vector2D.Int aimPos = transpose(aimPosition, w, h, Anchor.BOTTOM_MIDDLE).toInt();

        // Version temporaire de l'affichage du viseur sans image
        bufferG.setColor(Color.green);
        Vector2D.Int lineEnd = Vector2D.add(launcherOrigin, aimPosition).toInt();
        bufferG.drawLine((int)launcherOrigin.x, (int)launcherOrigin.y, (int)aimPosition.x, (int)aimPosition.y);

        // Dessine le rayon d'interaction pour chaque particule
//        g.setColor(Color.green);
//        final int radius = Particle.FORCE_RADIUS;
//        for (Particle p : Physics.allParticles) {
//            g.drawOval((int)(p.position.x - radius), (int)(p.position.y - radius), radius * 2, radius * 2);
//        }

        g.drawImage(buffer, 0, 0, null);
    }

    // Effectue la rotation d'une image et la retourne (angle en radians)
    public BufferedImage rotate(BufferedImage img, double angle) {
        final double sin = Math.abs(Math.sin(angle));
        final double cos = Math.abs(Math.cos(angle));
        final int w = (int) Math.floor(img.getWidth() * cos + img.getHeight() * sin);
        final int h = (int) Math.floor(img.getHeight() * cos + img.getWidth() * sin);
        final BufferedImage rotatedImage = new BufferedImage(w, h, img.getType());
        final AffineTransform transform = new AffineTransform();
        transform.translate(w / 2.0, h / 2.0);
        transform.rotate(angle,0, 0);
        transform.translate(-img.getWidth() / 2, -img.getHeight() / 2);
        final AffineTransformOp rotateOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);

        return rotateOp.filter(img, rotatedImage);
    }

    // Transpose des coordonnées du repère physique au repère du dessin en fonction du point d'ancrage
    private Vector2D transpose(Vector2D coords, int width, int height, Anchor anchor) {
        Vector2D result = null;
        switch (anchor) {
            case BOTTOM_MIDDLE -> result = Vector2D.add(coords, -width / 2.0, -height);
            case CENTER -> result = Vector2D.add(coords, -width / 2.0, -height / 2.0);
        }
        return result;
    }

    // Met à jour l'affichage de toutes les particules à l'ecran, en prenant en compte leurs nouvelles positions
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
        Point particleSize = new Point(p.img.getWidth(), p.img.getHeight());

        // Ramene la position en haut à gauche de l'image. C'est l'origine de tout dessin.
        Vector2D.Int pos = transpose(p.getPosition(), particleSize.x, particleSize.y, Anchor.CENTER).toInt();
        bufferG.drawImage(p.img, pos.x, pos.y, null);

        // Dessine le collider
//        bufferG.setColor(Color.GREEN);
//        bufferG.drawOval((int)(p.position.x - p.COLLIDER_RADIUS), (int)(p.position.y - p.COLLIDER_RADIUS),
//                p.COLLIDER_RADIUS * 2, p.COLLIDER_RADIUS * 2);
    }

    public void mouseMoved(MouseEvent e) {
        mousePosition.set(e.getX(), e.getY());
    }

    public void mouseClicked(MouseEvent e) {
        mousePosition.set(e.getX(), e.getY());

        // Spawn une particule dans la direction du launcher vers la souris
        Vector2D direction = Vector2D.fromTo(launcherOrigin, mousePosition);
        direction.normalize();
        Vector2D startSpeed = Vector2D.getScaled(direction, 10);
        Physics.createParticle(SelectionBar.selectedType, (int)launcherOrigin.x, (int)launcherOrigin.y, 0.02,
                50, true, startSpeed);

        // Détection de clic sur une particule d'antimatière
        for (Particle p : Physics.antimatterParticles) {
            if (p.getPosition().getSqrDistanceTo(e.getX(), e.getY()) <= p.COLLIDER_RADIUS * p.COLLIDER_RADIUS) {
                nbPoint = nbPoint + 1;
            }
        }
    }

    public void mouseDragged(MouseEvent e) { }

    public void mousePressed(MouseEvent e) { }

    public void mouseReleased(MouseEvent e) { }

    public void mouseEntered(MouseEvent e) { }

    public void mouseExited(MouseEvent e) { }

    public static Vector2D getCenter() {
        return new Vector2D(width / 2.0, height / 2.0);
    }
}
