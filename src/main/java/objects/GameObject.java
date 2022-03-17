package objects;

import physics.Vector2D;

/**
 * Interface that will be applied to all the objects appearing in the game
 */
public interface GameObject {

    Vector2D getPosition();
    void setPosition(Vector2D position);

    Vector2D getVelocity();
    void setVelocity(Vector2D velocity);

    Vector2D getPreviousPosition();
    void setPreviousPosition(Vector2D previousPosition);

    Vector2D getDimension();
    void setDimension(Vector2D dimension);

    void setState( Vector2D position , Vector2D velocity);
    double getMass();
    boolean isMoving();
    boolean getWillMove();
    void setWillMove(boolean willMove);
    boolean isOnSlope();
}
