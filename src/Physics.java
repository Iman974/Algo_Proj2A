import java.awt.*;
import java.awt.geom.Point2D;
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


        createParticle(Particle.Type.PROTON, 70, 200, 0.02, 55, false, Color.magenta,
                new Point2D.Double(2, 0));
        createParticle(Particle.Type.ELECTRON, 320, 120, 1, 0, false, Color.red);
        spawnParticle();
        spawnParticle();
        spawnParticle();
    }

    public void updateScene() {
        for (Particle p : allParticles) {
            p.resetForce();
            for (Particle other : allParticles) {
                if (other == p) {
                    continue;
                }
                if (p.position.distanceSq(other.position) <= Particle.FORCE_RADIUS * Particle.FORCE_RADIUS) {
                    other.applyForceTo(p);
                }
            }

            p.move();
            checkCollisions();
        }
    }

    private void createParticle(Particle.Type type, int x, int y, double frequency, int amplitude,
                                boolean fromPlayer, Color c) {
        createParticle(type, x, y, frequency, amplitude, fromPlayer, c, new Point2D.Double());
    }

        // Instancie une particule dans le jeu
    private void createParticle(Particle.Type type, int x, int y, double frequency, int amplitude,
                                boolean fromPlayer, Color c, Point2D.Double startSpeed) {
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

        Particle p = new Particle(type, x, y, blankImg, frequency, amplitude, fromPlayer, c, startSpeed);
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

    public void spawnParticle(){
            int borderRandom = (int)(Math.random()*3);
            if (borderRandom==0){
                createParticle(Particle.Type.PROTON, 0, (int)(Math.random()*700), 0.02, 55, false, Color.magenta,
                new Point2D.Double(Math.random()*5, 5-Math.random()*10));
            }
            if (borderRandom==1){
                createParticle(Particle.Type.PROTON, 700, (int)(Math.random()*700), 0.02, 55, false, Color.magenta,
                new Point2D.Double(-Math.random()*5, 5-Math.random()*10));
            }
            if (borderRandom==2){
                createParticle(Particle.Type.PROTON, (int)(Math.random()*700), 0, 0.02, 55, false, Color.magenta,
                new Point2D.Double(2-Math.random()*4, -Math.random()*5));
            }
    }
}
