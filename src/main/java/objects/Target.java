package objects;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import physics.Vector2D;

public class Target implements GameObject {
    private Vector2D position;
    private Vector2D dimension;
    private Cylinder cylinder;
    private double radius ;

    public Target(Vector2D position, double radius) {
        this.radius = radius;
        this.position = position;
        createTarget();

    }
    public void createTarget(){
        this.cylinder = new Cylinder();
        cylinder.setRadius(radius);
        cylinder.setHeight(0.01);
        this.cylinder.setTranslateX(this.position.getX());
        this.cylinder.setTranslateY(this.position.getY());
        this.cylinder.setTranslateZ((-TerrainGenerator.getHeight(position) - cylinder.getHeight()*2));
        this.cylinder.setRotationAxis(Rotate.X_AXIS);
        this.cylinder.setRotate(90);
        PhongMaterial cylinder_phong = new PhongMaterial(Color.BLACK);
        this.cylinder.setMaterial(cylinder_phong);
    }

    @Override
    public void setPosition(Vector2D position) {
        this.position = position;
    }

    @Override
    public Vector2D getVelocity() { return new Vector2D(0,0); }

    @Override
    public void setVelocity(Vector2D velocity) {}

    @Override
    public Vector2D getPreviousPosition() {
        return position;
    }

    @Override
    public void setPreviousPosition(Vector2D previousPosition) {}

    public Vector2D getPosition(){
        return position;
    }

    @Override
    public void setDimension(Vector2D dimension) {
        this.dimension = dimension;
    }

    @Override
    public void setState(Vector2D position, Vector2D velocity) {

    }

    @Override
    public double getMass() {
        return 0;
    }

    @Override
    public Vector2D getDimension() {
        return this.dimension;
    }


    @Override
    public boolean isMoving() {
        return false;
    }

    @Override
    public boolean getWillMove() { return false; }

    @Override
    public boolean isOnSlope() {
        return false;
    }

    public Cylinder getCylinder() {
        return this.cylinder;
    }
}
