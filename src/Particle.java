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
    double speedFrequency;

    public Particle(int x, int y, int charge, Image img, double frequency, int amplitude) {
        this.position = new Point2D.Double(x, y);
        this.charge = charge;
        this.img = img;

        this.speedFrequency = frequency;
        this.amplitudeSpeed = amplitude;

        this.speed = new Point2D.Double(4, 0);
    }

    public void move(int bufferWidth) { // generate the sinusoidale move of a particle
		position.x = (position.x + speed.x);
		speed.y = (amplitudeSpeed*Math.cos(2*Math.PI*speedFrequency *MainWindow.FrameCounter));
        position.y = (position.y + speed.y);

        if (position.x >= bufferWidth || position.x <= 0) {
            speed.x = -speed.x;
        }
    }
    public double distanceBetween( Particle p){ // returns the distance between two particles
		double retour=Math.sqrt(Math.pow((p.position.x-this.position.x),2)+Math.pow((p.position.y-this.position.y),2));
		return retour;
	}
	public void generateMove(){
		
		
	}
}
