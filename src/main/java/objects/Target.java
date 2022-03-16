package objects;

import graphics.Display;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import physics.Vector2D;

public class Target implements GameObject {

    private Vector2D position;
    private Vector2D dimension;
    private final Circle circle;

    public Target(Vector2D position, double radius) {
        this.position = position;
        this.circle = new Circle(radius);
        this.circle.setCenterX(position.getX() - Display.translateX);
        this.circle.setCenterY(position.getY() - Display.translateY);
        this.circle.setFill(Color.BLACK);
        this.circle.setTranslateZ(TerrainGenerator.getHeight(position)-5);
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

    public Circle getCircle() {
        return this.circle;
    }
}
