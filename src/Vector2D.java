public class Vector2D {

    double x;
    double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getSqrLength() {
        return x * x + y * y;
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    public void normalize() {
        double length = getLength();
        x /= length;
        y /= length;
    }

    public void scaleBy(double scalar) {
        x *= scalar;
        y *= scalar;
    }
    public double getDistanceTo(Vector2D v){
		return Math.sqrt((this.x-v.x)*(this.x-v.x)+(this.y-v.y)*(this.y-v.y));
	}
	public double getDistanceSqTo(Vector2D v){
		return ((this.x-v.x)*(this.x-v.x)+(this.y-v.y)*(this.y-v.y));
	}

    public void rotateAround(Vector2D v, double angle) {
        //à compléter
    }

    public static Vector2D getNormalized(Vector2D v) {
        double length = v.getLength();
        return new Vector2D(v.x / length, v.y / length);
    }

    public static Vector2D getScaled(Vector2D v, double scalar) {
        return new Vector2D(v.x * scalar, v.y * scalar);
    }

    public static Vector2D add(Vector2D a, Vector2D b) {
        return new Vector2D(a.x + b.x, a.y + b.y);
    }

    public static Vector2D subtract(Vector2D a, Vector2D b) {
        return new Vector2D(a.x - b.x, a.y - b.y);
    }

}
