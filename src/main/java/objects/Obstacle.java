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
    private final Vector3D dimension;
    private final Box box;

    /**
     * @param position initial position of the obstacle
     */
    public Obstacle(Vector2D position) {
        this.position = position;
        this.dimension = new Vector3D(
                getRandomDouble(0.6, 1.0),
                getRandomDouble(0.5, 1.0),
                getRandomDouble(0.5, 0.9)
        );
        this.box = createBox();
    }

    /**
     * @param position initial position of the obstacle
     * @param dimension (length, width, height) (?)
     */
    public Obstacle(Vector2D position, Vector3D dimension) {
        this.position = position;
        this.dimension = dimension;
        this.box = createBox();
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

    /**
     * @return random Double between minimum and maximum value
     */
    private double getRandomDouble(double minimum, double maximum) {
        Random random = new Random();
        return random.nextDouble() * (maximum - minimum) + minimum;
    }

}
