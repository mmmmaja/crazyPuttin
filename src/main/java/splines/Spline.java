package splines;

import objects.TerrainGenerator;
import physics.Vector2D;


/**
 * 1) change Terrain.getHeight() method so that it returns correct height
 * 2) use interpolate() method to modify the mesh
 * do we need to run over the mesh again???
 */
public class Spline extends SplineInterpolation {

    private final Vector2D position;
    private double height;


    public Spline(Vector2D position) {
        this.position = position;
        this.height = TerrainGenerator.getHeightFromFile(this.position);
    }

    public Vector2D getPosition() {
        return this.position;
    }

    /**
     * TODO interpolate()
     * @param deltaHeight difference in height to be added to the initial value
     */
    public void addHeight(double deltaHeight) {
        this.height+= deltaHeight;
        interpolateTerrain(this);
    }

    public double getHeight() {
        return this.height;
    }


    public double getRADIUS() {
        return 6.0;
    }

}
