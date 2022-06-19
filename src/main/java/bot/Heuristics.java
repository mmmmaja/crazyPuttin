package bot;

public enum Heuristics {

    // over the course all position of the ball are tracked and the closest distance is picked
    allPositions,

    // measured just on the final position when ball is not moving
    finalPosition
}
