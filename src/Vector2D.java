public class Vector2D {

    public double x;
    public double y;

    // Cette constante désigne le seuil à partir duquel une valeur est considéree comme nulle
    private static final double EPSILON = 1e-10;

    // TODO: non-static ?
    public static class Int {

        int x;
        int y;

        public Int(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D() {
        this.x = 0;
        this.y = 0;
    }

    public boolean equals(Vector2D other) {
        return Vector2D.sqrDistance(this, other) < EPSILON * EPSILON;
    }

    public void set(double x, double y) {
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
        if (length < EPSILON) {
            // Le vecteur est le vecteur nul.
            set(0, 0);
        } else {
            scale(1 / length);
        }
    }

    public void scale(double scalar) {
        x *= scalar;
        y *= scalar;
    }

    public double getSqrDistanceTo(double a, double b) {
        return (x - a) * (x - a) + (y - b) * (y - b);
    }

    public void add(double deltaX, double deltaY) {
        x += deltaX;
        y += deltaY;
    }

    public void add(Vector2D delta) {
        x += delta.x;
        y += delta.y;
    }

    public Vector2D.Int toInt() {
        return new Int((int) x, (int) y);
    }

    // Retourne une copie de ce vecteur
    public Vector2D copy() {
        return new Vector2D(x, y);
    }

    public static double sqrDistance(Vector2D a, Vector2D b) {
        return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
    }

    public static double distance(Vector2D a, Vector2D b) {
        return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
    }

    public static Vector2D getNormalized(Vector2D v) {
        double length = v.getLength();
        if (length < EPSILON) {
            return new Vector2D(0, 0);
        }
        return new Vector2D(v.x / length, v.y / length);
    }

    public static Vector2D getScaled(Vector2D v, double scalar) {
        return new Vector2D(v.x * scalar, v.y * scalar);
    }

    public static Vector2D add(Vector2D a, Vector2D b) {
        return new Vector2D(a.x + b.x, a.y + b.y);
    }

    public static Vector2D add(Vector2D v, double deltaX, double deltaY) {
        return new Vector2D(v.x + deltaX, v.y + deltaY);
    }

    public static Vector2D fromTo(Vector2D from, Vector2D to) {
        return new Vector2D(to.x - from.x, to.y - from.y);
    }

    // Retourne un vecteur de direction aléatoire et de norme 1.
    public static Vector2D getRandomUnitary() {
        double randomAngle = Math.random() * 2 * Math.PI;
        return new Vector2D(Math.cos(randomAngle), Math.sin(randomAngle));
    }

    /* Retourne un vecteur de norme 1 et de direction aléatoire excluant les directions comprises
       entre startAngle et endAngle (en radians, entre 0 et pi) si exclude est true,
       sinon la direction est comprise entre les angles en paramètre.
    */
    public static Vector2D getRandomRangeUnitary(double startAngle, double endAngle, boolean exclude) {
        double randomAngle;
        if (exclude) {
            double range = endAngle - startAngle;
            randomAngle = Math.random() * (2 * Math.PI - range);
            if (randomAngle > startAngle) {
                randomAngle += range;
            }
        } else {
            randomAngle = Utility.getRandomInRange(startAngle, endAngle);
        }
        return new Vector2D(Math.cos(randomAngle), Math.sin(randomAngle));
    }

    public static Vector2D getRandomRangeUnitary(double startAngle, double endAngle) {
        return getRandomRangeUnitary(startAngle, endAngle, false);
    }

    public static Vector2D middle(Vector2D a, Vector2D b) {
        Vector2D result = Vector2D.add(a,b);
        result.scale(0.5);
        return result;
    }
}
