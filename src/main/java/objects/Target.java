package objects;

import physics.Vector2D;

public class Target implements GameObject {

    private Vector2D position;
    private Vector2D dimension;


    public Target(Vector2D position) {
        this.position = position;
    }

    @Override
    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public Vector2D getPosition(){
        return position;
    }

    @Override
    public void setDimension(Vector2D dimension) {
        this.dimension = dimension;
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
    public boolean isOnSlope() {
        return false;
    }
}
