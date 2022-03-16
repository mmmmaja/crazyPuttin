package objects;

import graphics.Display;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import physics.PhysicEngine;
import physics.Vector2D;


public class Ball extends PhysicEngine implements GameObject {

    private double RADIUS = 0.1;
    private static final double MASS = 0.0459;

    private Vector2D position ;
    private Vector2D previousPosition;
    private Vector2D velocity ;
    private Vector2D acceleration;
    private Sphere sphere;


    public Ball(Vector2D position) {
        this.position = position;
        this.previousPosition = position ;
        velocity = new Vector2D(0,0);
        setAcceleration(velocity);
        createSphere();
    }


    private void createSphere() {
        this.sphere = new Sphere(RADIUS);
        this.sphere.setTranslateX(this.position.getX());
        this.sphere.setTranslateY(this.position.getY());
        this.sphere.setTranslateZ(TerrainGenerator.getHeight(this.position) - RADIUS - 2);
        this.sphere.setMaterial(new PhongMaterial(Color.BLUEVIOLET));
    }

    public boolean isMoving(){
        return velocity.getMagnitude() > 0.01;
    }

    public boolean willMove(){
        return TerrainGenerator.getStaticFrictionCoefficient(position) < ( Math.sqrt( Math.pow(TerrainGenerator.getSlopeX(position) , 2 ) + Math.pow( TerrainGenerator.getSlopeY( getPosition()) ,2) ) ) ;
    }

    public boolean isOnSlope() {
        return TerrainGenerator.getSlopeX(position) != 0  || TerrainGenerator.getSlopeY(position) != 0 ;
    }

    public Vector2D getPosition(){
        return position;
    }

    public void setPosition(Vector2D position){
        this.position = position;
    }

    public void setDimension(Vector2D dimension) {
        this.RADIUS = dimension.getX();
    }

    public Vector2D getDimension() {
        return new Vector2D(this.RADIUS, this.RADIUS);
    }

    public Vector2D getAcceleration(){
        return this.acceleration;
    }

    public void setAcceleration(Vector2D acceleration){
        this.acceleration = acceleration;
    }

    public Vector2D getVelocity(){
        return this.velocity;
    }

    public void setVelocity(Vector2D velocity){
        this.velocity = velocity;
    }

    public double getPositionX(){return position.getX();}
    public double getPositionY(){return position.getY();}

    public void setPreviousPosition(Vector2D previousPosition){
        this.previousPosition = previousPosition;
    }
    public Vector2D getPreviousPosition() {
        return previousPosition;
    }


    public void translateObject(double x, double y) {
        this.position = new Vector2D( this.getPosition().getX() + x , this.getPosition().getY() + y);
    }

    public void setState( Vector2D position , Vector2D velocity){
        setPosition(position);
        System.out.println(position);
        setVelocity(velocity);
    }

    public double getRADIUS() {return this.RADIUS;}
    public double getMass(){return MASS;    }
    public Sphere getSphere() {return this.sphere;}

}
