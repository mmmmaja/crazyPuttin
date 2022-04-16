package objects;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import physics.Vector2D;

public class Tree implements GameObject {

    private Vector2D position;
    private Vector2D dimension;
    private final Cylinder cylinder;
    private final Sphere sphere;


    public Tree(double height, double radius, Vector2D position) {
        this.dimension = new Vector2D(radius, height);
        this.position = position;
        this.cylinder = createCylinder();
        this.sphere = createSphere();
    }


    private Sphere createSphere() {
        Sphere sphere = new Sphere();
        sphere.setRadius(this.dimension.getX() * 12);
        sphere.setTranslateX(this.position.getX());
        sphere.setTranslateY(this.position.getY());
        sphere.setTranslateZ(-this.dimension.getY() - TerrainGenerator.getHeight(this.position));
        PhongMaterial phongMaterial = new PhongMaterial();
        phongMaterial.setDiffuseColor(Color.FORESTGREEN);
        sphere.setMaterial(phongMaterial);
        return sphere;
    }


    private Cylinder createCylinder() {
        Cylinder cylinder = new Cylinder();
        cylinder.setRadius(this.dimension.getX());
        cylinder.setHeight(this.dimension.getY());
        cylinder.setTranslateX(this.position.getX());
        cylinder.setTranslateY(this.position.getY());
        cylinder.setTranslateZ(- TerrainGenerator.getHeight(position) - cylinder.getHeight() / 2);
        cylinder.setRotationAxis(Rotate.X_AXIS);
        cylinder.setRotate(90);

        return cylinder;
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

    @Override
    public boolean isMoving() {
        return false;
    }



    @Override
    public Vector2D getVelocity() { return null; }

    @Override
    public void setVelocity(Vector2D velocity) {}

    @Override
    public Vector2D getPreviousPosition() { return null; }

    @Override
    public void setPreviousPosition(Vector2D previousPosition) {}

    @Override
    public void setState(Vector2D position, Vector2D velocity) {}

    @Override
    public double getMass() { return Integer.MAX_VALUE; }

    @Override
    public boolean getWillMove() { return false; }

    @Override
    public void setWillMove(boolean willMove) {}

    @Override
    public boolean isOnSlope() { return false; }

    public Cylinder getCylinder() {
        return this.cylinder;
    }

    public Sphere getSphere() {
        return this.sphere;
    }
}
