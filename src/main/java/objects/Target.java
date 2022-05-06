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
    private final double radius ;

    public Target(Vector2D position, double radius) {
        this.radius = radius;
        this.position = position;
        createTarget();
    }

    /**
     * creates the 3D cylinder that will be added to the Display
     */
    public void createTarget(){
        this.cylinder = new Cylinder();
        this.cylinder.setRadius(radius);
        this.cylinder.setHeight(0.01);
        this.cylinder.setTranslateX(this.position.getX());
        this.cylinder.setTranslateY(this.position.getY());
        this.cylinder.setTranslateZ((-TerrainGenerator.getHeightFromFile(position) - cylinder.getHeight()*2));
        this.cylinder.setRotationAxis(Rotate.X_AXIS);
        this.cylinder.setRotate(90);
        this.cylinder.setMaterial(new PhongMaterial(Color.BLACK));
    }

    @Override
    public void setPosition(Vector2D position) {
        this.position = position;
    }

    @Override
    public Vector2D getVelocity() {
        return new Vector2D(0,0);
    }

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
    public void setState(Vector2D position, Vector2D velocity) {}

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
    public boolean getWillMove() {
        return false;
    }

    @Override
    public void setWillMove(boolean willMove) {}

    @Override
    public boolean isOnSlope() {
        return false;
    }


    public Cylinder getCylinder() {
        return this.cylinder;
    }

    public double getEuclideanDistance(Vector2D vector2D) {
        return this.position.getEuclideanDistance(vector2D);
    }

    public double getEuclideanDistance3D(Vector2D vector2D) {
        //Height is not taken into account to calculate distance anymore.If you want to know why please let me know. E.Karabulut
        return Math.sqrt(
                        Math.pow(this.position.getX() - vector2D.getX(), 2) +
                        Math.pow(this.position.getY() - vector2D.getY(), 2)

        );
    }

}
