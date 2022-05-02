package objects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import physics.Vector2D;


/**
 * TODO change TerrainGenerator.getHeight() method so that it loops over the array of splines and gets it height
 */
public class Spline {

    private Vector2D position;
    private double height;


    public Spline(Vector2D position) {
        this.position = position;
        this.height = TerrainGenerator.getHeight(this.position);
    }

    public Spline() {}

    public Vector2D getPosition() {
        return this.position;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void addHeight(double deltaHeight) {
        this.height+= deltaHeight;
    }

    public double getHeight() {
        return this.height;
    }
}
