package objects;

import javafx.scene.shape.Cylinder;
import physics.Vector2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.transform.Rotate;

public class Pole implements GameObject{

    private Vector2D position;
    private Vector2D dimension;
    private Cylinder pole;

    public Pole(Vector2D position){
        this.position = position;
        this.pole = new Cylinder();
        createFlag();
    };

    public void createFlag(){
        pole.setRadius(0.02);
        pole.setHeight(2);
        this.pole.setTranslateX(this.position.getX());
        this.pole.setTranslateY(this.position.getY());
        this.pole.setTranslateZ( -TerrainGenerator.getHeight(position) - pole.getHeight()/2 );
        this.pole.setRotationAxis(Rotate.X_AXIS);
        this.pole.setRotate(90);
        PhongMaterial pole_phong = new PhongMaterial(Color.WHITE);
        this.pole.setMaterial(pole_phong);
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
    public boolean willMove() { return false; }

    @Override
    public boolean isOnSlope() {
        return false;
    }

    public Cylinder getCylinder() {
        return this.pole;
    }
}