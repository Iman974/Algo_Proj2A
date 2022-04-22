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

    int charge;
    Vector2D totalForce;
    Vector2D speed;
    Vector2D position;
    int moveAmplitude;
    Image img;
    double speedFrequency;
    boolean isFromPlayer;
    Color color;
    Type type;

    public Particle(Type type, int x, int y, Image img, double frequency, int amplitude, boolean isPlayer, Color c,
                    Vector2D startSpeed) {
        this.position = new Vector2D(x, y);
        this.img = img;
        this.isFromPlayer = isPlayer;
        this.color = c;
        this.totalForce = new Vector2D(0,0);
        this.speedFrequency = frequency;
        this.moveAmplitude = amplitude;
        this.speed = new Vector2D(startSpeed.x, startSpeed.y);

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
        }
    }

    public void move() {
        // On normalise la vitesse pour avoir la direction de déplacement
        double speedLength = speed.getLength();
        Vector2D direction = Vector2D.getNormalized(speed);

        final double PULSE = 2 * Math.PI * speedFrequency;
        // On evalue la valeur de la vitesse pour le mouvement sinusoïdal pour cette frame
        double waveSpeedEval = PULSE * moveAmplitude * Math.sin(PULSE * MainWindow.getFrame());

        speed.x += totalForce.x;
        speed.y += totalForce.y;
        // Le mouvement est la somme d'un mouvement sinusoïdal dans la direction transverse à un second
        // mouvement qui est longitudinal (vers l'avant)
        position.x += (waveSpeedEval * -direction.y) + speed.x;
        position.y += (waveSpeedEval * direction.x) + speed.y;
//        if( position.x>= 650|| position.x<=10){
//			speed.x=-speed.x;
//		}
//		if( position.y<=10||position.y>=650){
//			speed.y=-speed.y;
//		}
    }

    public void resetForce() {
        this.totalForce.x = 0;
        this.totalForce.y = 0;
    }

    // Applique la force de Coulomb sur l'autre particule
    public void applyForceTo(Particle other) {
        final double INTENSITY = 20;
        Vector2D force = Vector2D.fromTo(this.position, other.position);
        if (force.equals(Vector2D.zero)) {
            return;
        }
        double factor = INTENSITY * this.charge * other.charge / Math.pow(force.getSqrLength(), 1.5);

        other.totalForce.x += force.x * factor;
        other.totalForce.y += force.y * factor;
    }
}
