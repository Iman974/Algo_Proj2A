import java.awt.*;

import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class Physics {

    public static LinkedList<Particle> allParticles; // Contient toutes les particules visibles (pour rendering)
    public static LinkedList<Particle> antimatterParticles;

    // Contient toutes les particules qui peuvent causer une collision avec toutes les autres (celles du joueur uniquement)
    public static LinkedList<Particle> collidableParticles;


    public Physics() {
        allParticles = new LinkedList<Particle>();
        collidableParticles = new LinkedList<Particle>();
        antimatterParticles = new LinkedList<Particle>();

        final int MIN_PARTICLES_PER_WAVE = 2;
        final int MAX_PARTICLES_PER_WAVE = 8;
        final int MIN_WAVE_DELAY = 3000;
        final int MAX_WAVE_DELAY = 6500;


        Timer physicsTimer = new Timer();

        TimerTask waveTask = new TimerTask() {
            public void run() {
                spawnWave();
                scheduleNextWave();
            }

            public void spawnWave() {
                int particlesCount = Utility.getRandomInRange(MIN_PARTICLES_PER_WAVE, MAX_PARTICLES_PER_WAVE);
                for (int i = 0; i < particlesCount; i++) {
                    spawnParticle();
                }

                System.out.println("Wave spawned");
            }

            public void scheduleNextWave() {
                long randomDelay = Utility.getRandomInRange(MIN_WAVE_DELAY, MAX_WAVE_DELAY);
                TimerTask nextWave = new TimerTask() {
                    public void run() {
                        spawnWave();
                        scheduleNextWave();
                    }
                };
                physicsTimer.schedule(nextWave, randomDelay);
            }
        };
        // Lance la 1ère vague, qui entraînera les suivantes.
        waveTask.run();
    }

    public void updateScene() {
        // Détruit toute particule qui sort de l'écran
        for (Iterator<Particle> iterator = allParticles.iterator(); iterator.hasNext();) {
            Particle p = iterator.next();
            final int LIMIT = 2400;
            if (p.position.x < -LIMIT || p.position.x > GameArea.width + LIMIT ||
                    p.position.y < -LIMIT || p.position.y > GameArea.height + LIMIT) {
                destroyParticle(p);
                iterator.remove();
            }
        }

        for (Particle p : allParticles) {
            p.resetForce();
            for (Particle other : allParticles) {
                if (other == p) {
                    continue;
                }
                if (Vector2D.sqrDistance(p.position, other.position) <= Particle.FORCE_RADIUS * Particle.FORCE_RADIUS) {
                    other.applyForceTo(p);
                }
            }

            p.move();
            checkCollisions();
        }
    }

    public void destroyParticle(Particle p) {
        if (p.type == Particle.Type.ANTIMATTER) {
            antimatterParticles.remove(p);
        }
        if (p.isFromPlayer || p.type == Particle.Type.ANTIMATTER) {
            collidableParticles.remove(p);
        }
    }

    // Instancie une particule dans le jeu
    public static void createParticle(Particle.Type type, int x, int y, double frequency, int amplitude, double startPhase,
                                boolean fromPlayer, Vector2D startSpeed) {
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

        Particle p = new Particle(type, x, y, blankImg, frequency, amplitude, startPhase, fromPlayer, startSpeed);

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
                if (Vector2D.distance(p1.position, p2.position) < p1.COLLIDER_RADIUS + p2.COLLIDER_RADIUS) {
                    System.out.println("Collision");
                    // Détruire les deux particules.
                    // Spawner une (ou plusieurs) particule(s) d'antimatière
                }
            }
        }
    }

    /*
    Spawn une particule aléatoirement. Elle apparaît sortant d'une bordure de l'écran, sauf celle en bas.
     */
    public void spawnParticle() {
        Particle.Type[] types = Particle.Type.values();
        Particle.Type randomType = types[(int)(Math.random() * types.length)];

        final int OUTER_MARGIN = 50; // Retrait des bordures déterminant à partir d'où on spawn les particules
        final double START_SPEED_ANGLE_RANGE = 45; // Angle d'ouverture du cône de vitesse initiale
        final double DIST_OFFSET_RANGE = 200;
        final double MIN_FREQUENCY = 0.01;
        final double MAX_FREQUENCY = 0.06;
        final int MIN_AMPLITUDE = 25;
        final int MAX_AMPLITUDE = 75;
        final int MIN_SPEED = 4;
        final int MAX_SPEED = 12;
        final double ANGLE_RANGE = Math.toRadians(80);

        Vector2D randomDirection = Vector2D.getRandomRangeUnitary(Math.toRadians(40), Math.toRadians(140), true);
        int width = GameArea.width;
        int height = GameArea.height;
        Vector2D randomScaledDirection = Vector2D.getScaled(randomDirection,
                Math.sqrt(width * width + height * height) / 2 + OUTER_MARGIN + Math.random() * DIST_OFFSET_RANGE);
        Vector2D.Int startPosition = Vector2D.add(GameArea.getCenter(), randomScaledDirection).toInt();

        int randomSpeed = Utility.getRandomInRange(MIN_SPEED, MAX_SPEED);
        double randomFrequency = Utility.getRandomInRange(MIN_FREQUENCY, MAX_FREQUENCY);
        double randomPhase = Math.random() * 2 * Math.PI; // TODO : supprimer parametre phase et mettre dans cstrctor particle
        int randomAmplitude = Utility.getRandomInRange(MIN_AMPLITUDE, MAX_AMPLITUDE);

        double dirAngle = Math.atan2(randomDirection.y, randomDirection.x);
        if (dirAngle < 0) {
            dirAngle += 2 * Math.PI;
        }
        Vector2D randomSpeedDir = Vector2D.getRandomRangeUnitary(dirAngle - ANGLE_RANGE / 2, dirAngle + ANGLE_RANGE / 2);

        createParticle(randomType, startPosition.x, startPosition.y, randomFrequency, randomAmplitude, randomPhase,
                false, Vector2D.getScaled(randomSpeedDir, -randomSpeed));
    }
}
