import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Particle {

    enum Type {
        NEUTRON,
        PROTON,
        ELECTRON,
        ANTIMATTER
    }

    public boolean isDead;

    public final static int FORCE_RADIUS = 165;
    public final int COLLIDER_RADIUS = 13;

    public final BufferedImage img;
    public final boolean isFromPlayer;
    public final Type type;

    private final int moveAmplitude;
    private final double speedFrequency;
    private final double phaseOffset;
    private final int charge;

    private final Vector2D totalForce;
    private final Vector2D speed;
    private final Vector2D position;

    // TODO: Ne pas utiliser de Vector2D en paramètre car passage par référence (dans tout le code)

    public Particle(Type type, int x, int y, double frequency, int amplitude, double phase, boolean isPlayer,
                    Vector2D startSpeed) {
        this.position = new Vector2D(x, y);
        this.isFromPlayer = isPlayer;
        this.totalForce = new Vector2D(0,0);
        this.speedFrequency = frequency;
        this.moveAmplitude = amplitude;
        this.speed = startSpeed.copy();
        this.phaseOffset = phase;

//        this.img = getParticleImage();
        this.type = type;
        // On définit les propriétés de la particule selon son type
        switch (type) {
            case NEUTRON:
                this.charge = 0;
                break;
            case PROTON:
                this.charge = 1;
                break;
            case ELECTRON:
                this.charge = -1;
                break;
            case ANTIMATTER:
                this.charge = 0;
                break;
            default:
                this.charge = 0;
        }

        this.img = getParticleImage();
    }

    public Vector2D getPosition() {
        return position.copy();
    }

    private BufferedImage getParticleImage() {
        String currentDirectory = System.getProperty("user.dir");

        String root;
        if (currentDirectory.contains("src")) {
            root = "..\\resources\\";
        } else {
            root = "\\resources\\";
        }
        System.out.println(currentDirectory + root + type + ".png");
        File imageFile = new File(currentDirectory, root + type + ".png");
        try {
            return ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void move() {
        // On normalise la vitesse pour avoir la direction de déplacement
        Vector2D direction = Vector2D.getNormalized(speed);

        final double PULSE = 2 * Math.PI * speedFrequency;
        // On evalue la valeur de la vitesse pour le mouvement sinusoïdal pour cette frame
        double waveSpeedEval = PULSE * moveAmplitude * Math.sin(PULSE * MainWindow.getFrame() + phaseOffset);

        speed.add(totalForce);
        // Le mouvement est la somme d'un mouvement sinusoïdal dans la direction transverse à un second
        // mouvement qui est longitudinal (vers l'avant)
        position.add((waveSpeedEval * -direction.y) + speed.x, (waveSpeedEval * direction.x) + speed.y);
    }

    public void resetForce() {
        totalForce.set(0, 0);
    }

    // Applique la force de Coulomb sur l'autre particule
    public void applyForceTo(Particle other) {
        final double INTENSITY = 200;
        Vector2D force = Vector2D.fromTo(this.position, other.position);

        final Vector2D zero = new Vector2D(0, 0);
        if (force.equals(zero)) {
            return;
        }
        double factor = INTENSITY * this.charge * other.charge / Math.pow(force.getSqrLength(), 1.5);
        force.scale(factor);

        other.totalForce.add(force);
    }
}
