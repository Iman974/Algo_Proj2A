import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.ArrayList;

public class Physics {

    public static ArrayList<Particle> allParticles; // Contient toutes les particules visibles à l'écran
    public static ArrayList<Particle> antimatterParticles;

    // Contient toutes les particules qui peuvent causer une collision (celles du joueur uniquement)
    public static ArrayList<Particle> collidableParticles;


    public Physics() {
        allParticles = new ArrayList<Particle>();

        createParticle(Particle.Type.NEUTRON, 140, 300, 0.05, 5, false);
        createParticle(Particle.Type.NEUTRON, 60, 300, 0.01, 10, false);
    }

    public void updateScene(int bufferWidth) {
        for (Particle p : allParticles) {
            p.move(bufferWidth);
        }
    }

    // Instancie une particule dans le jeu
    private void createParticle(Particle.Type type, int x, int y, double frequency, int amplitude, boolean fromPlayer) {
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

        Particle p = new Particle(type, x, y, blankImg, frequency, amplitude, fromPlayer);
        allParticles.add(p);
        if (fromPlayer) {
            collidableParticles.add(p);
        }
    }

    public void checkCollision() {
        for (int i = 0; i < collidableParticles.size(); i++) {
            Particle p1 = collidableParticles.get(i);
            for (int j = i + 1; j < collidableParticles.size(); j++) {
                Particle p2 = collidableParticles.get(j);
                if (p1.position.distance(p2.position) < p1.INTERACTION_RADIUS + p2.INTERACTION_RADIUS) {
                    // Il y a une collision
                }
            }
        }
    }
}
