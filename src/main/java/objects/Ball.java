package objects;

import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import physics.PhysicEngine;
import physics.Vector2D;


public class Ball extends PhysicEngine implements GameObject {

    private double RADIUS = 5 ;

    private Vector2D position ;
    private Vector2D previousPosition;
    private Vector2D velocity ;
    private Vector2D acceleration;

    private Sphere sphere;


    public Ball(Vector2D position) {
        this.position = position ;
        this.previousPosition = position ;
        velocity = new Vector2D(0,0);
        setAcceleration(velocity);
        createSphere();
    }

    private void createSphere() {
        this.sphere = new Sphere(RADIUS);
        this.sphere.setTranslateX(this.position.getX());
        this.sphere.setTranslateY(this.position.getY());
        this.sphere.setTranslateZ(TerrainGenerator.getHeight(this.position)); // TODO figure it out!

        // TODO add material etc.
        Material material = new PhongMaterial(Color.BLUEVIOLET);
        this.sphere.setMaterial(material);
    }

    public Vector2D getPosition(){
        return position;
    }

    @Override
    public void setDimension(Vector2D dimension) {
        this.RADIUS = dimension.getX();
    }

    @Override
    public Vector2D getDimension() {
        return new Vector2D(RADIUS, RADIUS);
    }

    @Override
    public boolean isMoving() {
        return false;
    }

    @Override
    public boolean isOnSlope() {
        return false;
    }

    public Vector2D getVelocity(){
        return velocity;
    }
    public double getPositionX(){
        return position.getX();
    }
    public double getPositionY(){
        return position.getY();
    }

    public Vector2D getPreviousPosition() { return previousPosition; }
    public Vector2D getAcceleration(){ return acceleration;}

    public double getMass(){
        return getMASS();
    }

    public void setPosition(Vector2D position){
        this.position = position;
    }
    public void setVelocity(Vector2D velocity){
        this.velocity = velocity ;
    }
    public void setAcceleration(Vector2D velocity){
        this.acceleration = calculateAcceleration(velocity);
    }
    public void setPreviousPosition(Vector2D previousPosition){
        this.previousPosition = previousPosition;
    }


    public double getRADIUS() {
        return this.RADIUS;
    }

    public Sphere getSphere() {
        return this.sphere;
    }


}
