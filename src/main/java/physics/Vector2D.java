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


    public double getEuclideanDistance(Vector2D vector2D) {
        return Math.sqrt(
                Math.pow(this.x - vector2D.getX(), 2) +
                        Math.pow(this.y - vector2D.getY(), 2)
        );
    }

    public Vector2D multiply(double c) {
        return new Vector2D(
                this.x * c,
                this.y * c
        );
    }

    public Vector2D add(Vector2D vector2D) {
        return new Vector2D(
                this.x + vector2D.getX(),
                this.y + vector2D.getY()
        );
    }
    public Vector2D rotate(double angle){
        double cos = Math.cos(Math.toRadians(angle));
        double sin = Math.sin(Math.toRadians(angle));
        return new Vector2D( x * cos - y * sin , x * sin + y * cos );
    }

    public Vector2D reflectAroundVector( Vector2D rotateAround){
        double dotProduct = dotProduct(rotateAround);
        double x = -(2 *  dotProduct * rotateAround.getX() - this.x);
        double y = -(2 *  dotProduct * rotateAround.getY() - this.y);
        return new Vector2D( x ,  y);
    }

    public double dotProduct(Vector2D vector ){
        return this.x * vector.getX() + this.y * vector.getY();
    }

    public Vector2D convertToNormalVectorObstacles(){
        if(Math.abs(this.x) > Math.abs(this.y) ){
            return new Vector2D(1 , 0 );
        }else if(Math.abs(this.x) == Math.abs(this.y)){
            return new Vector2D(1,1);
        }else
            return new Vector2D(0 , 1);
    }

    public Vector2D copyOf() {
        return new Vector2D(
                this.x,
                this.y
        );
    }
}
