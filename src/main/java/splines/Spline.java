package splines;

import objects.TerrainGenerator;
import physics.Vector2D;


/**
 * 1) change TerrainGenerator.getHeight() method so that it returns correct height
 * 2) use interpolate() method to modify the mesh
 */
public class Spline {

    private final Vector2D position;
    private double height;
    private final double RADIUS = 5.5;


    public Spline(Vector2D position) {
        this.position = position;
        this.height = TerrainGenerator.getHeight(this.position);
    }

    public Vector2D getPosition() {
        return this.position;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * TODO interpolate()
     * @param deltaHeight difference in height to be added to the initial value
     */
    public void addHeight(double deltaHeight) {
        this.height+= deltaHeight;
    }

    public double getHeight() {
        return this.height;
    }


    public double getRADIUS() {
        return this.RADIUS;
    }

}
