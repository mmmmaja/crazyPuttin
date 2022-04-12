package objects;

import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import physics.PhysicEngine;
import physics.Vector2D;


public class Ball extends PhysicEngine implements GameObject {

    private double RADIUS = 0.05;
    private static final double MASS = 0.0459;

    private Vector2D position;
    private Vector2D previousPosition;
    private Vector2D velocity;
    private Vector2D acceleration;
    private Sphere sphere;
    private boolean willMove;

    public Ball(Vector2D position) {
        this.position = position;
        this.previousPosition = position ;
        this.velocity = new Vector2D(0,0);
        this.willMove = false;
        createSphere();
    }


    /**
     * create the Sphere object that will be added to the Display
     */
    private void createSphere() {
        this.sphere = new Sphere(RADIUS * 2);
        // set the position of the sphere at the place indicated by the inputFile
        this.sphere.setTranslateX(this.position.getX());
        this.sphere.setTranslateY(this.position.getY());
        this.sphere.setTranslateZ((-TerrainGenerator.getHeight(position) - 2 * RADIUS));
        Material material = new PhongMaterial(Color.WHITESMOKE);
        this.sphere.setMaterial(material);
    }

    public Vector2D getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector2D acceleration) {
        this.acceleration = acceleration;
    }

    public double getRADIUS() {
        return this.RADIUS;
    }

    /**
     * @return true if ball hit the target and the point is scored
     */
    public boolean isOnTarget(Target target) {
        double xDiff = target.getPosition().getX() - position.getX();
        double yDiff = target.getPosition().getY() - position.getY();
        return new Vector2D(xDiff, yDiff).getMagnitude() < target.getCylinder().getRadius();
    }

    /**
     * @return Sphere object that will be added to the Display
     */
    public Sphere getSphere() {
        return this.sphere;
    }

    /**
     * @return true is ball is in movement, false otherwise
     */
    @Override
    public boolean isMoving() {
        return velocity.getMagnitude() > 0.2;
    }

    /**
     * @return true is ball is in movement, false otherwise
     */
    @Override
    public boolean getWillMove() {
        if (!willMove) {
            return false;
        }
        double slope = (Math.sqrt(Math.pow(TerrainGenerator.getSlopeX(position), 2) + Math.pow(TerrainGenerator.getSlopeY(getPosition()), 2)));
        this.willMove = TerrainGenerator.getStaticFrictionCoefficient(position) < slope;
        return willMove;
    }

    @Override
    public void setWillMove(boolean willMove){
        this.willMove = willMove;
    }

    /**
     * @return true if ball is positioned on the slope of the surface
     */
    @Override
    public boolean isOnSlope() {
        return TerrainGenerator.getSlopeX(position) != 0  || TerrainGenerator.getSlopeY(position) != 0;
    }


    /**
     * @return position of the ball
     */
    @Override
    public Vector2D getPosition(){
        return position;
    }

    @Override
    public void setPosition(Vector2D position) {
        this.position = position;
    }

    /**
     * @return the dimension of the ball (width and length)
     */
    @Override
    public Vector2D getDimension() {
        return new Vector2D(RADIUS, RADIUS);
    }

    @Override
    public void setDimension(Vector2D dimension) {
        this.RADIUS = dimension.getX();
    }


    @Override
    public Vector2D getVelocity() {
        return velocity;
    }

    @Override
    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    @Override
    public void setPreviousPosition(Vector2D previousPosition) {
        this.previousPosition = previousPosition;
    }

    @Override
    public Vector2D getPreviousPosition() {
        return previousPosition;
    }

    @Override
    public void setState(Vector2D position, Vector2D velocity){
        setPosition(position);
        setVelocity(velocity);
    }

    @Override
    public double getMass() {
        return MASS;
    }

    public Ball copyOf() {
        return new Ball(this.position);
    }
}
