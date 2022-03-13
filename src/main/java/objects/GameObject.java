package objects;

import physics.Vector2D;

public interface GameObject {

    public void setPosition(Vector2D position);

    public Vector2D getPosition();

    public void setDimension(Vector2D dimension);

    public Vector2D getDimension();

    public boolean isMoving();

    public boolean isOnSlope();


}
