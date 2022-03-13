package objects;

import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import physics.PhysicEngine;
import physics.Vector2D;


public class Ball extends PhysicEngine {
    private final double RADIUS = 5 ;

    private Vector2D position ;
    private Vector2D previousPosition;
    private Vector2D velocity ;
    private Vector2D acceleration;


    private Sphere sphere;


    // TODO store a Mesh object for graphics!!!
    // TODO remove velocity ?
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
        this.sphere.setTranslateY(this.position.getY());;
        Material material = new PhongMaterial(Color.BLUEVIOLET);
        this.sphere.setMaterial(material);
        // TODO add material etc.
    }

    public Vector2D getPosition(){
        return position;
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

    public double getMASS(){ return getMASS(); }



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
