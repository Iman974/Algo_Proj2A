import java.awt.*;

public abstract class Particle {

    final int INTERACTION_RADIUS = 5;

    double charge;
    Point speed;
    Point position;
    int accelerationY;
    boolean isVisible;
    int amplitudeSpeed;
    Image img;
    int speedFrequency;

    public Particle(double charge, Image img, int frequency, int amplitude) {
        this.charge = charge;
        this.img = img;
        this.speedFrequency = frequency;
        this.amplitudeSpeed = amplitude;
    }
}