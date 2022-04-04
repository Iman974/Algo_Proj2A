import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class GameArea extends JPanel {

    BufferedImage buffer;
    Graphics bufferG;
    Physics physics;

    static int width;
    static int height;

    public GameArea(int width, int height) {
        setBounds(0, 0, width, height);

        this.width = width;
        this.height = height;

        this.buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.bufferG = buffer.getGraphics();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(buffer, 0, 0, null);
    }

    // Met a jour l'affichage de toutes les particules a l'ecran, en prenant en compte leurs nouvelles positions
    public void updateScreen() {
        clearScreen();
        for (Particle p : physics.allParticles) {
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
}
