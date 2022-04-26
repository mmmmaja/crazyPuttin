package objects;

import graphics.Display;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import physics.Vector2D;
import physics.Vector3D;

import java.util.Dictionary;

/**
 * TODO add collision detection
 * TODO improve movement based on the rotation of the scene
 */
public class Obstacle {

    private Vector2D position;
    private final Vector3D dimension;
    private final Box box;

    /**
     * @param position initial position of the obstacle
     */
    public Obstacle(Vector2D position) {
        this.position = position;
        this.dimension = new Vector3D(0.5, 0.5, 0.5);
        this.box = createBox();
    }

    /**
     * @param position initial position of the obstacle
     * @param dimension (length, width, height)
     */
    public Obstacle(Vector2D position, Vector3D dimension) {
        this.position = position;
        this.dimension = dimension;
        this.box = createBox();
    }


    /**
     * move the box by the given deltaX and deltaY
     * TODO take the rotation of the scene into the consideration, how though?
     */
    public void move(double deltaX, double deltaY) {

        double moveFactor = 0.05;

        deltaX*= moveFactor;
        deltaY*= moveFactor;

        this.setPosition(new Vector2D(
                this.position.getX() + deltaX,
                this.position.getY() + deltaY));
        this.box.setTranslateX(deltaX + this.position.getX());
        this.box.setTranslateY(deltaY + this.position.getY());

        box.setTranslateZ(-(TerrainGenerator.getHeight(this.position) + dimension.getZ() / 2));
    }



    /**
     * create Box object that is the representation of the obstacle
     */
    private Box createBox() {
        Box box = new Box();
        box.setDepth(dimension.getX());
        box.setWidth(dimension.getY());
        box.setHeight(dimension.getZ());

        box.setTranslateX(this.position.getX());
        box.setTranslateY(this.position.getY());
        box.setTranslateZ(-(TerrainGenerator.getHeight(this.position) + dimension.getZ() / 2));

        return box;
    }


    public Box getBox() {
        return this.box;
    }


    public Vector2D getPosition() {
        return this.position;
    }


    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public Vector3D getDimension() { return this.dimension; }

}
