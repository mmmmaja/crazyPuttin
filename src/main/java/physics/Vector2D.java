package physics;

import java.util.Objects;

public class Vector2D implements Vector {

    private double x;
    private double y;

    public Vector2D() {
    }

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

    public void reverseVector(){
        setX(-x);
        setY(-y);
    }

    @Override
    public String toString() {
        return "Vector2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2D vector2D = (Vector2D) o;
        if(this.getMagnitude() == 0 || ((Vector2D) o).getMagnitude()==0) return false;
        return Double.compare(vector2D.x, x) == 0 && Double.compare(vector2D.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Vector2D times(double c){
        return new Vector2D( x * c , y * c);
    }

    public void rotate(double angle) {
        double x2 = Math.cos(angle * x) - Math.sin(angle * y);
        double y2 = Math.sin(angle * x) + Math.cos(angle * y);
    }

    public void rotateAroundOrigin(double angle, Vector2D origin) {
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        // translate point back to origin:
        this.x -= origin.getX();
        this.y -= origin.getY();

        // rotate point
        double x2 = origin.getX() * cos - origin.getY() * sin;
        double y2 = origin.getX() * sin - origin.getY() * cos;

        this.x = x2;
        this.y = y2;
    }
}
