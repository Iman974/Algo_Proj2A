import java.awt.*;


public class Particle {

    enum Type {
        NEUTRON,
        PROTON,
        ELECTRON,
        ANTIMATTER
    }

    final static int FORCE_RADIUS = 165;
    final int COLLIDER_RADIUS = 13;

    final int moveAmplitude;
    final double speedFrequency;
    final double phaseOffset;

    final int charge;
    Vector2D totalForce;
    Vector2D speed;
    Vector2D position;
    Image img;
    boolean isFromPlayer;
    Color color;
    Type type;

    public Particle(Type type, int x, int y, Image img, double frequency, int amplitude, double phase, boolean isPlayer,
                    Vector2D startSpeed) {
        this.position = new Vector2D(x, y);
        this.img = img;
        this.isFromPlayer = isPlayer;
        this.totalForce = new Vector2D(0,0);
        this.speedFrequency = frequency;
        this.moveAmplitude = amplitude;
        this.speed = new Vector2D(startSpeed.x, startSpeed.y);
        this.phaseOffset = phase;

        this.type = type;
        // On définit les propriétés de la particule selon son type
        switch (type) {
            case NEUTRON:
                this.charge = 0;
                this.color = Color.LIGHT_GRAY;
                break;
            case PROTON:
                this.charge = 1;
                this.color = Color.RED;
                break;
            case ELECTRON:
                this.charge = -1;
                this.color = Color.CYAN;
                break;
            case ANTIMATTER:
                this.charge = 0;
                this.color = Color.YELLOW;
                break;
            default:
                this.charge = 0;
        }
    }

    public void move() {
        // On normalise la vitesse pour avoir la direction de déplacement
        Vector2D direction = Vector2D.getNormalized(speed);

        final double PULSE = 2 * Math.PI * speedFrequency;
        // On evalue la valeur de la vitesse pour le mouvement sinusoïdal pour cette frame
        double waveSpeedEval = PULSE * moveAmplitude * Math.sin(PULSE * MainWindow.getFrame() + phaseOffset);

        speed.moveBy(totalForce);
        // Le mouvement est la somme d'un mouvement sinusoïdal dans la direction transverse à un second
        // mouvement qui est longitudinal (vers l'avant)
        position.moveBy((waveSpeedEval * -direction.y) + speed.x, (waveSpeedEval * direction.x) + speed.y);
//        if( position.x>= 650|| position.x<=10){
//			speed.x=-speed.x;
//		}
//		if( position.y<=10||position.y>=650){
//			speed.y=-speed.y;
//		}
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

        other.totalForce.x += force.x * factor;
        other.totalForce.y += force.y * factor;
    }
}
