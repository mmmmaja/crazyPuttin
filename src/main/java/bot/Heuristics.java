package bot;

public enum Heuristics {

    // includes just the final distance between the ball and the target (when the ball is in the resting position)
    finalPosition,

    // considers all the positions of the ball when its moving
    // and takes the minimum distance between the ball and the target
    allPositions
}
