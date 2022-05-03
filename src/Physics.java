import java.util.*;

public class Physics {

    public static LinkedList<Particle> allParticles; // Contient toutes les particules visibles (pour rendering)
    public static LinkedList<Particle> antimatterParticles;

    // Contient toutes les particules qui peuvent causer une collision avec toutes les autres (celles du joueur uniquement)
    private static LinkedList<Particle> collidableParticles;

    private static LinkedList<Particle> particlesToSave;

    private final int UPDATES_PER_SECOND = 60;

    public static int updateCount;

    public Physics() {
        allParticles = new LinkedList<Particle>();
        collidableParticles = new LinkedList<Particle>();
        antimatterParticles = new LinkedList<Particle>();
        particlesToSave = new LinkedList<Particle>();

        Timer physicsTimer = new Timer();

        TimerTask updateTask = new TimerTask() {

            public void run() {
                updateScene();
            }
        };
        final int START_DELAY = 1500; // Délai nécessaire pour stabilisation du framerate en début de jeu
        physicsTimer.schedule(updateTask, START_DELAY, (long)(1.0 / UPDATES_PER_SECOND * 1000));

        TimerTask waveTask = new TimerTask() {
            public void run() {
                spawnWave();
                scheduleNextWave();
            }

            public void scheduleNextWave() {
                final int MIN_WAVE_DELAY = 2500;
                final int MAX_WAVE_DELAY = 6500;
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

    public void spawnWave() {
        final int MIN_PARTICLES_PER_WAVE = 2;
        final int MAX_PARTICLES_PER_WAVE = 6;
        int particlesCount = Utility.getRandomInRange(MIN_PARTICLES_PER_WAVE, MAX_PARTICLES_PER_WAVE);
        for (int i = 0; i < particlesCount; i++) {
            spawnParticle();
        }
    }

    public void updateScene() {
        for (Particle p : allParticles) {
            // Détruit toute particule qui sort de la zone limite, en dehors des bordures de l'écran
            final int LIMIT = 1000;
            Vector2D position = p.getPosition();
            if (position.x < -LIMIT || position.x > GameArea.width + LIMIT ||
                    position.y < -LIMIT || position.y > GameArea.height + LIMIT) {
                setDestroyFlag(p);
                continue;
            }

            // Applique les forces d'interactions provenant des particules dans le voisinage
            p.resetForce();
            for (Particle other : allParticles) {
                if (other == p) {
                    continue;
                }
                if (Vector2D.sqrDistance(position, other.getPosition()) <= Particle.FORCE_RADIUS * Particle.FORCE_RADIUS) {
                    other.applyForceTo(p);
                }
            }
            p.move();
        }

        checkCollisions();

        removeDeadParticles(allParticles);
        removeDeadParticles(collidableParticles);
        removeDeadParticles(antimatterParticles);
        saveParticles();
    }

    // Marque la particule comme devant être détruite pour la prochaine mise à jour physique.
    public static void setDestroyFlag(Particle p) {
        p.isDead = true;
    }

    // Supprime toutes les particules marquées comme 'dead' dans la liste donnée
    private void removeDeadParticles(List<Particle> particleList) {
        for (Iterator<Particle> iterator = particleList.iterator(); iterator.hasNext();) {
            Particle particle = iterator.next();
            if (particle.isDead) {
                iterator.remove();
            }
        }
    }

    // Instancie une particule dans le jeu
    public static void createParticle(Particle.Type type, int x, int y, double frequency, int amplitude,
                                boolean fromPlayer, Vector2D startSpeed) {
        double randomPhase = Math.random() * 2 * Math.PI;
        Particle newParticle = new Particle(type, x, y, frequency, amplitude, randomPhase, fromPlayer, startSpeed);
        // TODO: /!\ erreur à corriger (multi-thread) en lien avec updateScreen de GameArea
        particlesToSave.add(newParticle);
    }

    // Sauvegarde les particules nouvellement créées en les insérant dans les listes en fonction de leur nature.
    private void saveParticles() {
        for (int i = 0; i < particlesToSave.size(); i++) {
            Particle newParticle = particlesToSave.get(i);
            allParticles.add(newParticle);
            if (newParticle.type == Particle.Type.ANTIMATTER) {
                antimatterParticles.add(newParticle);
            } else if (newParticle.isFromPlayer) {
                collidableParticles.add(newParticle);
            }
        }
        particlesToSave.clear();
    }

    private void checkCollisions() {
        for (Particle p1 : collidableParticles) {
            for (Particle p2 : allParticles) {
                // Pour chaque particule pouvant causer une collision, on vérifie la distance qui la sépare
                // à chaque autre particule
                if (p1 == p2) { // TODO : trouver un moyen de ne pas recomparer les même particules
                    // On exclue la particule elle-même
                    continue;
                }
                Vector2D position1 = p1.getPosition();
                Vector2D position2 = p2.getPosition();
                if (Vector2D.distance(position1, position2) < p1.COLLIDER_RADIUS + p2.COLLIDER_RADIUS) {
                    setDestroyFlag(p1);
                    setDestroyFlag(p2);
                    if (p1.type != Particle.Type.ANTIMATTER && p2.type != Particle.Type.ANTIMATTER) {
                        final int MIN_ANTIMATTER_COUNT = 1;
                        final int MAX_ANTIMATTER_COUNT = 1;
                        Vector2D spawnPosition = Vector2D.middle(position1, position2);
                        for (int i = 0; i < Utility.getRandomInRange(MIN_ANTIMATTER_COUNT, MAX_ANTIMATTER_COUNT); i++) {
                            spawnAntimatter(spawnPosition);
                        }
                    }
                }
            }
        }
    }

    public void spawnAntimatter(Vector2D spawnPosition) {
        final double MIN_PARTICLE_SPEED = 1.5;
        final double MAX_PARTICLE_SPEED = 3;
        double speedFactor = Utility.getRandomInRange(MIN_PARTICLE_SPEED, MAX_PARTICLE_SPEED);
        Vector2D randomSpeed = Vector2D.getScaled(Vector2D.getRandomUnitary(), speedFactor);
        createParticle(Particle.Type.ANTIMATTER, (int)spawnPosition.x, (int)spawnPosition.y, 0.015, 40, false, randomSpeed);
    }

    /*
    Spawn une particule aléatoirement. Elle apparaît sortant d'une bordure de l'écran, sauf celle en bas.
     */
    private void spawnParticle() {
        Particle.Type[] types = Particle.Type.values();
        Particle.Type randomType = types[(int)(Math.random() * (types.length - 1))];

        final int OUTER_MARGIN = 50; // Retrait des bordures déterminant à partir d'où on spawn les particules
        final double DIST_OFFSET_RANGE = 200;
        final double MIN_FREQUENCY = 0.005;
        final double MAX_FREQUENCY = 0.03;
        final int MIN_AMPLITUDE = 25;
        final int MAX_AMPLITUDE = 100;
        final double MIN_SPEED = 2;
        final double MAX_SPEED = 7;
        final double START_SPEED_ANGLE_RANGE = Math.toRadians(80); // Angle d'ouverture du cône de vitesse initiale

        Vector2D randomDirection = Vector2D.getRandomRangeUnitary(Math.toRadians(40), Math.toRadians(140), true);
        int width = GameArea.width;
        int height = GameArea.height;
        Vector2D randomScaledDirection = Vector2D.getScaled(randomDirection,
                Math.sqrt(width * width + height * height) / 2 + OUTER_MARGIN + Math.random() * DIST_OFFSET_RANGE);
        Vector2D.Int startPosition = Vector2D.add(GameArea.getCenter(), randomScaledDirection).toInt();

        double speedFactor = Utility.getRandomInRange(MIN_SPEED, MAX_SPEED);
        double randomFrequency = Utility.getRandomInRange(MIN_FREQUENCY, MAX_FREQUENCY);
        int randomAmplitude = Utility.getRandomInRange(MIN_AMPLITUDE, MAX_AMPLITUDE);

        double dirAngle = Math.atan2(randomDirection.y, randomDirection.x);
        if (dirAngle < 0) {
            dirAngle += 2 * Math.PI;
        }
        Vector2D randomSpeedDir = Vector2D.getRandomRangeUnitary(dirAngle - START_SPEED_ANGLE_RANGE / 2, dirAngle + START_SPEED_ANGLE_RANGE / 2);

        createParticle(randomType, startPosition.x, startPosition.y, randomFrequency, randomAmplitude,
                false, Vector2D.getScaled(randomSpeedDir, -speedFactor));
    }
}
