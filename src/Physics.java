import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.LinkedList;

public class Physics {

    public static LinkedList<Particle> allParticles; // Contient toutes les particules visibles (pour rendering)
    public static LinkedList<Particle> antimatterParticles;

    // Contient toutes les particules qui peuvent causer une collision (celles du joueur uniquement)
    public static LinkedList<Particle> collidableParticles;


    public Physics() {
        allParticles = new LinkedList<Particle>();
        collidableParticles = new LinkedList<Particle>();

        createParticle(Particle.Type.NEUTRON, 340, 300, 0.05, 5, true, Color.magenta);
        createParticle(Particle.Type.NEUTRON, 450, 310, 0.01, 10, false, Color.red);
    }

    public void updateScene(int bufferWidth) {
        for (Particle p : allParticles) {

            p.move(bufferWidth);
            checkCollisions();
        }
    }

    // Instancie une particule dans le jeu
    private void createParticle(Particle.Type type, int x, int y, double frequency, int amplitude, boolean fromPlayer, Color c) {
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

        Particle p = new Particle(type, x, y, blankImg, frequency, amplitude, fromPlayer, c);
        allParticles.add(p);
        if (fromPlayer) {
            collidableParticles.add(p);
        }
    }

    public void checkCollisions() {
        // On parcourt les listes avec des for each car ce sont des LinkedList
        for (Particle p1 : collidableParticles) {
            for (Particle p2 : allParticles) {
                if (p2.isFromPlayer) {
                    continue;
                }
                if (p1.position.distance(p2.position) < p1.COLLIDER_RADIUS + p2.COLLIDER_RADIUS) {
                    System.out.println("Collision");
                    // Détruire les deux particules.
                    // Spawner une (ou plusieurs) particule(s) d'antimatière
                }
            }
        }
    }
}
