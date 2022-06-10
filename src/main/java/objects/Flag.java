package objects;


import javafx.scene.shape.Box;
import physics.Vector2D;

/**
 * Class that holds the objects needed to render 3D flag
 */
public class Flag implements GameObject {

    private Vector2D position;
    private Vector2D dimension;
    private final Box flag;

    public Flag(Vector2D position){
        this.position = position;
        this.flag = new Box();
        createFlag();
    };

    /**
     * create Box that is the representation of the flag
     */
    public void createFlag(){
        flag.setHeight(0.05);
        flag.setWidth(1);
        flag.setDepth(0.5);
        this.flag.setTranslateX(this.position.getX()-flag.getWidth()/2);
        this.flag.setTranslateY(this.position.getY());
        this.flag.setTranslateZ(-TerrainGenerator.getHeight(position)-2  );
    }

    /**
     * @return Box object that will be added to the Display
     */
    public Box getBox() {
        return this.flag;
    }

    @Override
    public void setPosition(Vector2D position) {
        this.position = position;
        this.flag.setTranslateX(this.position.getX()-flag.getWidth()/2);
        this.flag.setTranslateY(this.position.getY());
        this.flag.setTranslateZ(-TerrainGenerator.getHeight(position)-2  );
    }

    @Override
    public Vector2D getVelocity() {
        return new Vector2D(0,0);
    }

    @Override
    public void setVelocity(Vector2D velocity) {}

    @Override
    public Vector2D getPreviousPosition() {
        return position;
    }

    @Override
    public void setPreviousPosition(Vector2D previousPosition) {}

    @Override
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
    public boolean getWillMove() {
        return false;
    }

    @Override
    public void setWillMove(boolean willMove) {}

    @Override
    public boolean isOnSlope() {
        return false;
    }
}
