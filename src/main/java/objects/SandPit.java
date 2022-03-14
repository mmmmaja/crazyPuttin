package objects;

import physics.Vector2D;

public record SandPit(Vector2D sandPitX, Vector2D sandPitY) {

    public Vector2D getSandPitX() {
        return this.sandPitX;
    }

    public Vector2D getSandPitY() {
        return this.sandPitY;
    }
}
