import java.awt.*;
import java.awt.geom.Point2D;

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
    Point2D.Double totalForce;
    Point2D.Double speed;
    Point2D.Double position;
    Point2D.Double waveAcceleration;
    boolean isVisible;
    int moveAmplitude;
    Image img;
    double speedFrequency;
    boolean isFromPlayer;
    Color color;

    public Particle(Type type, int x, int y, Image img, double frequency, int amplitude, boolean isPlayer, Color c,
                    Point2D.Double startSpeed) {
        this.position = new Point2D.Double(x, y);
        this.img = img;
        this.isFromPlayer = isPlayer;
        this.color = c;
        this.totalForce = new Point2D.Double();
        this.speedFrequency = frequency;
        this.moveAmplitude = amplitude;
        this.speed = new Point2D.Double(startSpeed.x, startSpeed.y);

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
        System.out.println(position);
        // On normalise la vitesse pour avoir la direction de déplacement
        double speedLength = speed.distance(0, 0);
        Point2D.Double direction = new Point2D.Double(0, 0);
        if (speedLength > 0) {
            // TODO: normalize vector function
            direction.x =  speed.x / speedLength;
            direction.y = speed.y / speedLength;
        }

        final double PULSE = 2 * Math.PI * speedFrequency;
        // On evalue la valeur de la vitesse pour le mouvement sinusoïdal pour cette frame
        double waveSpeedEval = PULSE * moveAmplitude * Math.sin(PULSE * MainWindow.getFrame());

        speed.x += totalForce.x;
        speed.y += totalForce.y;
        // Le mouvement est la somme d'un mouvement sinusoïdal dans la direction transverse à un second
        // mouvement qui est longitudinal (vers l'avant)
        position.x += (waveSpeedEval * -direction.y) + speed.x;
        position.y += (waveSpeedEval * direction.x) + speed.y;
        if( position.x>= 650|| position.x<=10){
			speed.x=-speed.x;
		}
		if( position.y<=10||position.y>=650){
			speed.y=-speed.y;
		}
    }

    public void resetForce() {
        this.totalForce.x = 0;
        this.totalForce.y = 0;
    }

    // Applique la force de Coulomb sur l'autre particule
    public void applyForceTo(Particle other) {
        final double INTENSITY = 20;
        Point2D.Double force = new Point2D.Double(
                other.position.x - this.position.x, other.position.y - this.position.y);
        double factor = INTENSITY * this.charge * other.charge / Math.pow(force.distanceSq(0, 0), 1.5); // TODO: prevent division by zero /!\

        other.totalForce.x += force.x * factor;
        other.totalForce.y += force.y * factor;
    }
}
