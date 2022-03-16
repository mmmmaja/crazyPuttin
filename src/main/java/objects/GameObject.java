package objects;

import physics.PhysicEngine;
import physics.Vector2D;

public interface GameObject {

    public Vector2D getPosition();
    public void setPosition(Vector2D position);

    public Vector2D getVelocity();
    public void setVelocity(Vector2D velocity);

    public Vector2D getPreviousPosition();
    public void setPreviousPosition(Vector2D previousPosition);

    public Vector2D getDimension();
    public void setDimension(Vector2D dimension);

    public void setState( Vector2D position , Vector2D velocity);
    public double getMass();
    public boolean isMoving();
    public boolean willMove();
    public boolean isOnSlope();

}
