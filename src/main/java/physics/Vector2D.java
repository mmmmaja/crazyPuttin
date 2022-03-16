package physics;

public class Vector2D implements Vector{

    private double x;
    private double y;

    public Vector2D(){}
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }


    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getMagnitude(){
        return Math.sqrt( Math.pow(x,2)  + Math.pow(y,2));
    }
    public Vector2D getUnitVector(){
        return new Vector2D( x / getMagnitude() , y / getMagnitude() );
    }
    public Vector2D reverseUnitVector() {
        return new Vector2D( -1 * x / getMagnitude(), -1 * y / getMagnitude() );
    }
    public Vector2D reverseVector(){
        return new Vector2D(-1 * x, -1 * y);
    }


    public static Vector sum(Vector v1, Vector v2, Vector v3) {
        return new Vector2D( v1.getX() + v2.getX() + v3.getX() , v1.getY() + v2.getY() + v3.getY() );
    }


    public static Vector sum(Vector v1, Vector v2) {
        return new Vector2D( v1.getX() + v2.getX()  , v1.getY() + v2.getY()  );

    }

    @Override
    public String toString() {
        return "Vector2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
