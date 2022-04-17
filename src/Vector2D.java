public class Vector2D {
    double x;
    double y;
    public Vector2D(double x1, double y1){
        this.x=x1;
        this.y=y1;
    }

    public double getLength(){
        return Math.sqrt(Math.pow(this.x,2)+Math.pow(this.y,2));
    }

    public void normalize(){
        double l=getLength();
        this.x=this.x/l;
        this.y=this.y/l;
    }

    public void scaleBy(double a){
        this.x=this.x*a;
        this.y=this.y*a;
    }

    public void rotateAround(Vector2D v, double a){
        //à compléter
    }

    public static Vector2D getNormalized(Vector2D v){
        double l=v.getLength();
        return new Vector2D(v.x/l , v.y/l);
    }

    public static Vector2D getScaled(Vector2D v, double a){
        return new Vector2D(v.x*a , v.y*a);
    }

    public static Vector2D add(Vector2D v1, Vector2D v2){
        return new Vector2D(v1.x+v2.x , v1.y + v2.y);
    }

    public static Vector2D substract(Vector2D v1, Vector2D v2){
        return new Vector2D(v1.x - v2.x , v1.y - v2.y);
    }

}