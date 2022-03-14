package objects;

import physics.Vector2D;

public class FileReader {

    public Vector2D getInitialPosition() {
        return new Vector2D(200, 200);
    }

    public Vector2D getTargetPosition() {
        return new Vector2D(100, 100);
    }

    public double getTargetRadius() {
        return 5;
    }

    public Vector2D getSandPitX() {
        return new Vector2D(10, 50);
    }

    public Vector2D getSandPitY() {
        return new Vector2D(10, 100);
    }


}
