package objects;

import graphics.Display;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import physics.Vector2D;
import physics.Vector3D;

import java.util.Dictionary;
import java.util.Random;

/**
 * TODO add collision detection
 */
public class Obstacle {

    private final Vector2D position;
    public double SIDE_LENGTH = 0.7;
    private final Box box;

    /**
     * @param position initial position of the obstacle
     */
    public Obstacle(Vector2D position) {
        this.position = position;
        this.box = createBox();
    }

    /**
     * @param position initial position of the obstacle
     * @param dimension (length, width, height) (?)
     */
    public Obstacle(Vector2D position, Vector3D dimension) {
        this.position = position;
        this.box = createBox();
    }


    /**
     * create Box object that is the representation of the obstacle
     */
    private Box createBox() {
        Box box = new Box();
        box.setDepth(SIDE_LENGTH);
        box.setWidth(SIDE_LENGTH);
        box.setHeight(SIDE_LENGTH);

        box.setTranslateX(this.position.getX());
        box.setTranslateY(this.position.getY());
        box.setTranslateZ(-(TerrainGenerator.getHeight(this.position) + SIDE_LENGTH / 2));

        return box;
    }


    public Box getBox() {
        return this.box;
    }


    public Vector2D getPosition() {
        return this.position;
    }

    public double getDimension(){
        return this.SIDE_LENGTH;
    }

}
