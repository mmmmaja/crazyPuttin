package objects;

import physics.Vector2D;

import java.util.Vector;

public class Ball {
    private final double MASS = 0.0459;
    private final double RADIUS = 5 ;

    private Vector2D position ;
    private Vector2D velocity ;


    // TODO store a Mesh object for graphics!!!
    public Ball(Vector2D position , Vector2D velocity) {
        this.position = position ;
        this.velocity = velocity ;
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


}
