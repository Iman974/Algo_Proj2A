import java.awt.*;

public class Particle {

    final int INTERACTION_RADIUS = 5;

    int charge;
    Point speed;
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
    }
}