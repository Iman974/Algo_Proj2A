import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.LinkedList;

public class Physics {

    public static LinkedList<Particle> particles;

    public Physics() {
        particles = new LinkedList<Particle>();

        createParticle(100, 100);
        createParticle(140, 110);
    }

    private void updateScene() {

    }

    // Instancie une particule dans le jeu
    private void createParticle(int x, int y) {
        // Créé une image vierge, simplement pour les tests
        Image blankImg = new Image() {
            public int getWidth(ImageObserver observer) {
                return 25;
            }
            public int getHeight(ImageObserver observer) {
                return 25;
            }

            public ImageProducer getSource() {
                return null;
            }
            public Graphics getGraphics() {
                return null;
            }
            public Object getProperty(String name, ImageObserver observer) {
                return null;
            }
        };

        Particle p = new Particle(x, y, -1, blankImg, 10, 2);
        particles.add(p);
    }
}
