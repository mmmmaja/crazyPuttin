package objects;

import physics.Vector2D;

public class SandPit {

    private final Vector2D sandPitX;
    private final Vector2D sandPitY;

    public SandPit(Vector2D sandPitX, Vector2D sandPitY) {
        this.sandPitX = sandPitX;
        this.sandPitY = sandPitY;
    }

    public double getHeight(double x, double y) {
        return - Math.pow(x, 2) - Math.pow(y, 2);
    }
}
