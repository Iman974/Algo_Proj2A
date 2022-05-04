import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class GameArea extends JPanel implements MouseMotionListener, MouseListener {

    private final BufferedImage buffer;
    private static Graphics bufferG;

    private Vector2D shootOrigin;
    private Vector2D mousePosition;
    private BufferedImage aim;

    public static int width;
    public static int height;

    private static Vector2D center;

    public GameArea(int width, int height) {
        setBounds(0, 0, width, height);

        GameArea.width = width;
        GameArea.height = height;

        shootOrigin = new Vector2D(width / 2.0, height - 70);
        mousePosition = new Vector2D();

        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        bufferG = buffer.getGraphics();
        setLayout(null);

        addMouseMotionListener(this);
        addMouseListener(this);
    }

    public void paintComponent(Graphics g) {
        // Affichage carre viseur
        final int LAUNCHER_DIM = 50;
        Vector2DInt launcherPos = transpose(shootOrigin, LAUNCHER_DIM, LAUNCHER_DIM, Anchor.CENTER).toInt();
        bufferG.setColor(Color.DARK_GRAY);
        bufferG.drawRect(launcherPos.x, launcherPos.y, LAUNCHER_DIM, LAUNCHER_DIM);

        // Affichage du viseur
        Vector2D launcherToMouse = Vector2D.fromTo(shootOrigin, mousePosition);
        launcherToMouse.normalize();
        final int AIMER_LENGTH = 40;
        Vector2D aimDirection1 = Vector2D.getScaled(launcherToMouse, LAUNCHER_DIM);
        Vector2D aimDirection2 = Vector2D.getScaled(launcherToMouse, LAUNCHER_DIM + AIMER_LENGTH);
        bufferG.setColor(Color.orange);
        Vector2DInt lineStart = Vector2D.add(shootOrigin, aimDirection1).toInt();
        Vector2DInt lineEnd = Vector2D.add(shootOrigin, aimDirection2).toInt();
        bufferG.drawLine(lineStart.x, lineStart.y, lineEnd.x, lineEnd.y);

        g.drawImage(buffer, 0, 0, null);
    }

    // Transpose des coordonnées du repère physique au repère du dessin en fonction du point d'ancrage
    private Vector2D transpose(Vector2D coords, int width, int height, Anchor anchor) {
        Vector2D result = null;
        switch (anchor) {
            case CENTER -> result = Vector2D.add(coords, -width / 2.0, -height / 2.0);
        }
        return result;
    }

    // Met à jour l'affichage de toutes les particules à l'ecran, en prenant en compte leurs nouvelles positions
    public void updateScreen() {
        clearScreen();
        synchronized (Physics.allParticles) {
            for (Particle p : Physics.allParticles) {
                drawParticle(p);
            }
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
        Vector2DInt pos = transpose(p.getPosition(), particleSize.x, particleSize.y, Anchor.CENTER).toInt();
        bufferG.drawImage(p.img, pos.x, pos.y, null);
    }

    public void mouseMoved(MouseEvent e) {
        mousePosition.set(e.getX(), e.getY());
    }

    public void mousePressed(MouseEvent e) {
        mousePosition.set(e.getX(), e.getY());

        // Détection de clic sur une particule d'antimatière
        for (Particle p : Physics.antimatterParticles) {
            if (p.getPosition().getSqrDistanceTo(mousePosition) <= p.COLLIDER_RADIUS * p.COLLIDER_RADIUS) {
                Main.addScore(1);
                Physics.setDestroyFlag(p);
                // Si une particule d'antimatière est collectée, on ne prend pas en compte le clic pour lancer une nouvelle particule
                return;
            }
        }

        // Spawn une particule dans la direction du launcher vers la souris
        Vector2D direction = Vector2D.fromTo(shootOrigin, mousePosition);
        direction.normalize();
        Vector2D startSpeed = Vector2D.getScaled(direction, 10);
        Physics.createParticle(SelectionBar.selectedType, (int)shootOrigin.x, (int)shootOrigin.y, 0.02,
                50, true, startSpeed);
    }

    public void mouseClicked(MouseEvent e) { }

    public void mouseDragged(MouseEvent e) { }

    public void mouseReleased(MouseEvent e) { }

    public void mouseEntered(MouseEvent e) { }

    public void mouseExited(MouseEvent e) { }

    public static Vector2D getCenter() {
        if (center == null) {
            center = new Vector2D(width / 2.0, height / 2.0);
        }
        return center;
    }
}
