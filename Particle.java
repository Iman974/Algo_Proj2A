public abstract class Particle{
    int radius;
    double charge;
    Point speed;
    Point position;
    int accelerationY;
    boolean isVisible;
    int amplitudeSpeed;
    Image img;
    int speedFrequency;

    public Particle(double c, Image i, int f, int a,){ //c,i,f et a viennent des classes filles en super, c'est les caractéristiques propres aux différentes particules
        this.radius= ? ; // je sais pas si le rayon est constant pour n'importe quelle particule ou alors il dépend d'elle
        this.charge=c;
        this.img=i;
        this.speedFrequency=f;
        this.amplitudeSpeed=a;

    }
}