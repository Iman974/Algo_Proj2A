import java.awt.*;
import java.awt.geom.Point2D;

public class Particle {

    enum Type {
        NEUTRON,
        PROTON,
        ELECTRON,
        ANTIMATTER
    }

    final int INTERACTION_RADIUS = 5;

    int charge;
    Point2D.Double speed;
    Point2D.Double position;
    int accelerationY;
    boolean isVisible;
    int amplitudeSpeed;
    Image img;
    double speedFrequency;
    boolean isFromPlayer;

    public Particle(Type type, int x, int y, Image img, double frequency, int amplitude, boolean isPlayer) {
        this.position = new Point2D.Double(x, y);
        this.img = img;
        this.isFromPlayer = isPlayer;

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

        this.speedFrequency = frequency;
        this.amplitudeSpeed = amplitude;

        this.speed = new Point2D.Double(4, 0);
    }

    public void move(int bufferWidth) { // generate the sine move of a particle
        position.x += speed.x;
        speed.y = amplitudeSpeed * Math.cos(2 * Math.PI * speedFrequency * MainWindow.getFrame());
        position.y += speed.y;

        if (position.x >= bufferWidth || position.x <= 0) {
            speed.x = -speed.x;
        }
    }

    public double distanceBetween(Particle p) { // returns the distance between two particles
        return this.position.distance(p.position);
    }

    public void applyForce(Point2D.Double force) {

    }
}
