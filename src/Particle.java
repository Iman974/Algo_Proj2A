import java.awt.*;
import java.awt.geom.Point2D;

public class Particle {

    final int INTERACTION_RADIUS = 5;

    int charge;
    Point2D.Double speed;
    Point2D.Double position;
    int accelerationY;
    boolean isVisible;
    int amplitudeSpeed;
    Image img;
    int speedFrequency;

    public Particle(int x, int y, int charge, Image img, int frequency, int amplitude) {
        this.position = new Point2D.Double(x, y);
        this.charge = charge;
        this.img = img;

        this.speedFrequency = frequency;
        this.amplitudeSpeed = amplitude;

        this.speed = new Point2D.Double(1, 0);
    }

    public void move(int bufferWidth) {
		position.x = (position.x + speed.x);
		speed.y = (amplitudeSpeed*Math.cos(2*Math.PI*speedFrequency *MainWindow.FrameCounter));
        position.y = (position.y + speed.y);

        if (position.x >= bufferWidth || position.x <= 0) {
            speed.x = -speed.x;
        }
    }
}
