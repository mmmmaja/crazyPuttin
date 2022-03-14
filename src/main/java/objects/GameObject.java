package objects;

import physics.Vector2D;

public interface GameObject {

    public Vector2D getPosition();
    public void setPosition(Vector2D position);

    public Vector2D getVelocity();
    public void setVelocity(Vector2D velocity);

    public Vector2D getDimension();
    public void setDimension(Vector2D dimension);


    public boolean isMoving();

    public boolean isOnSlope();


}
