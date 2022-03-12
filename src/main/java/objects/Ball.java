package objects;

import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import physics.Vector2D;
import java.util.Vector;


public class Ball {
    private final double MASS = 0.0459;
    private final double RADIUS = 5 ;

    private Vector2D position ;
    private Vector2D velocity ;

    private Sphere sphere;


    // TODO store a Mesh object for graphics!!!
    public Ball(Vector2D position , Vector2D velocity) {
        this.position = position ;
        this.velocity = velocity ;
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


    public void setPosition(Vector2D position){
        this.position = position;
    }
    public void setVelocity(Vector2D velocity){
        this.velocity = velocity ;
    }

    public double getRADIUS() {
        return this.RADIUS;
    }

    public Sphere getSphere() {
        return this.sphere;
    }


}
