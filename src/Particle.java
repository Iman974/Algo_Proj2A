import java.awt.*;
import java.awt.geom.Point2D;

public class Particle {

    final int INTERACTION_RADIUS = 5;

    int charge;
    Point2D.Double speed;
    Point position;
    int accelerationY;
    boolean isVisible;
    int amplitudeSpeed;
    Image img;
    int speedFrequency;

    public Particle(int x, int y, int charge, Image img, int frequency, int amplitude) {
        this.position = new Point(x, y);
        this.charge = charge;
        this.img = img;

        this.speedFrequency = frequency;
        this.amplitudeSpeed = amplitude;

        this.speed = new Point2D.Double(5, 0);
    }

    public void move(int bufferWidth) {
//        speed.y = Math.cos(speedFrequency * );
        
        position.x = (int)(position.x + speed.x);
        position.y = (int)(position.y + speed.y);

        if (position.x >= bufferWidth || position.x <= 0) {
            speed.x = -speed.x;
        }
    }
}