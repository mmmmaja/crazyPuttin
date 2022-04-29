package objects;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import physics.Vector2D;


/**
 * TODO change TerrainGenerator.getHeight() method so that it loops over the array of splines and gets it height
 */
public class SplineDragger implements GameObject {

    private Vector2D position;
    private Vector2D dimension;
    private final Sphere model;
    private final Color baseColor = Color.ORANGERED;
    private double height;

    public SplineDragger(Vector2D position, double radius) {
        this.position = position;
        this.dimension = new Vector2D(radius, radius);
        this.model = createModel();
        this.height = TerrainGenerator.getHeight(this.position);
    }

    public SplineDragger() {
        this.model = createModel();
    }


    private Sphere createModel() {
        Sphere sphere = new Sphere();
        sphere.setRadius(this.dimension.getX());
        sphere.setTranslateX(this.position.getX());
        sphere.setTranslateY(this.position.getY());
        sphere.setTranslateZ(-TerrainGenerator.getHeight(this.position) - this.dimension.getX() / 2.0);
        PhongMaterial phongMaterial = new PhongMaterial();
        phongMaterial.setDiffuseColor(this.baseColor);
        sphere.setMaterial(phongMaterial);
        return sphere;
    }

    public Sphere getModel() {
        return this.model;
    }


    @Override
    public Vector2D getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(Vector2D position) {
        this.position = position;
    }

    @Override
    public Vector2D getDimension() {
        return this.dimension;
    }

    @Override
    public void setDimension(Vector2D dimension) {
        this.dimension = dimension;
    }

    public void setColor(Color color) {
        PhongMaterial phongMaterial = new PhongMaterial();
        phongMaterial.setDiffuseColor(color);
        this.model.setMaterial(phongMaterial);
    }

    public void resetColor() {
        PhongMaterial phongMaterial = new PhongMaterial();
        phongMaterial.setDiffuseColor(this.baseColor);
        this.model.setMaterial(phongMaterial);
    }


    public void setHeight(double height) {
        this.height = height;
    }


    @Override
    public boolean isMoving() {
        return false;
    }

    @Override
    public boolean getWillMove() {
        return false;
    }

    @Override
    public Vector2D getVelocity() {return null;}

    @Override
    public void setVelocity(Vector2D velocity) {}

    @Override
    public Vector2D getPreviousPosition() {return null;}

    @Override
    public void setPreviousPosition(Vector2D previousPosition) {}

    @Override
    public void setState(Vector2D position, Vector2D velocity) {}

    @Override
    public double getMass() {return 0;}

    @Override
    public void setWillMove(boolean willMove) {}

    @Override
    public boolean isOnSlope() {return false;}


}
