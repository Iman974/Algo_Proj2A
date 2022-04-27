import java.awt.Image;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
        for (Particle p : allParticles) {
            // Détruit toute particule qui sort de l'écran
            final int LIMIT = 2400;
            if (p.position.x < -LIMIT || p.position.x > GameArea.width + LIMIT ||
                    p.position.y < -LIMIT || p.position.y > GameArea.height + LIMIT) {
                destroyParticle(p);
                System.out.println("Destroyed particle");
                continue;
            }

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

        removeDeadParticles(allParticles);
        removeDeadParticles(collidableParticles);
        removeDeadParticles(antimatterParticles);
    }

    public void destroyParticle(Particle p) {
        p.isDead = true;
    }

    // Supprime toutes les particules marquées comme 'dead' dans la liste donnée
    public void removeDeadParticles(List<Particle> particleList) {
        for (Iterator<Particle> iterator = particleList.iterator(); iterator.hasNext();) {
            Particle p = iterator.next();
            if (p.isDead) {
                iterator.remove();
            }
        }
    }

    // Instancie une particule dans le jeu
    public static void createParticle(Particle.Type type, int x, int y, double frequency, int amplitude,
                                boolean fromPlayer, Vector2D startSpeed) {
        double randomPhase = Math.random() * 2 * Math.PI;
        Particle p = new Particle(type, x, y, frequency, amplitude, randomPhase, fromPlayer, startSpeed);

        allParticles.add(p);
        if (fromPlayer) {
            collidableParticles.add(p);
        }
    }

    public void checkCollisions() {
        for (Particle p1 : collidableParticles) {
            for (Particle p2 : allParticles) {
                // Pour chaque particule pouvant causer une collision, on vérifie la distance qui la sépare
                // à chaque autre particule
                if (p1 == p2) {
                    // On exclue la particule elle-même
                    continue;
                }
                if (Vector2D.distance(p1.position, p2.position) < p1.COLLIDER_RADIUS + p2.COLLIDER_RADIUS) {
                    Vector2D.Int spawnPosition = Vector2D.middle(p1.position, p2.position).toInt();

                    int speedFactor = Utility.getRandomInRange(4, 13);
                    Vector2D randomSpeed = Vector2D.getScaled(Vector2D.getRandomUnitary(), speedFactor);
//                    createParticle(Particle.Type.ANTIMATTER, spawnPosition.x, spawnPosition.y, 0.1, 20, false, randomSpeed);
                    destroyParticle(p1);
                    destroyParticle(p2);
                }
            }
        }
    }

    /*
    Spawn une particule aléatoirement. Elle apparaît sortant d'une bordure de l'écran, sauf celle en bas.
     */
    public void spawnParticle() {
        Particle.Type[] types = Particle.Type.values();
        Particle.Type randomType = types[(int)(Math.random() * (types.length - 1))];

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
        int randomAmplitude = Utility.getRandomInRange(MIN_AMPLITUDE, MAX_AMPLITUDE);

        double dirAngle = Math.atan2(randomDirection.y, randomDirection.x);
        if (dirAngle < 0) {
            dirAngle += 2 * Math.PI;
        }
        Vector2D randomSpeedDir = Vector2D.getRandomRangeUnitary(dirAngle - ANGLE_RANGE / 2, dirAngle + ANGLE_RANGE / 2);

        createParticle(randomType, startPosition.x, startPosition.y, randomFrequency, randomAmplitude,
                false, Vector2D.getScaled(randomSpeedDir, -randomSpeed));
    }
}
