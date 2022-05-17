package bot;

import Main.Shot;
import Main.Universe;
import objects.Obstacle;
import physics.Vector2D;


import java.util.ArrayList;
import java.util.Random;


/**
 * This bot performs random shots in all directions
 */
public class RandomBot extends Bot {

    private int testNumber = 1000;

    public RandomBot() {
        this.targetPosition = this.universe.getTarget().getPosition();
        this.name = "Random Bot";
        start();
    }

    /**
     * constructor used for the hillClimbing bot to specify number of trials
     */
    public RandomBot(int testNumber) {
        this.testNumber = testNumber;
        this.targetPosition = this.universe.getTarget().getPosition();
        this.name = "Random Bot";
        start();
    }

    /**
     * constructor used for the hillClimbing bot to specify number of trials
     */
    public RandomBot(int testNumber, boolean shootBall) {
        this.testNumber = testNumber;
        this.shootBall = shootBall;
        this.targetPosition = this.universe.getTarget().getPosition();
        this.name = "Random Bot";
        start();
    }

    /**
     * constructor extended to fit AStarBot properties (specify the target position)
     */
    public RandomBot(Vector2D targetPosition) {
        this.targetPosition = targetPosition;
        this.name = "Random Bot";
        start();
    }

    /**
     * simulate the shot and pick the best one of all based on heuristics
     */
    @Override
    public void run() {

        for (int i = 0; i < testNumber; i++) {
            if (!running) {
                stop();
            }
            this.shotCounter++;

            Vector2D initialVelocity = new Vector2D(
                    Obstacle.getRandomDouble(-5.0, 5.0),
                    Obstacle.getRandomDouble(-5.0, 5.0)
            );

            // euclidean distance between the ball and the target
            double result = new TestShot(this.universe, initialVelocity, this.targetPosition).getTestResult(this.heuristics);
            if (result < this.bestResult) {
                this.bestResult = result;
                this.bestVelocity = initialVelocity;
            }
            // ball was hit
            if (this.bestResult == 0.0) {
                stop();
            }
        }
        stop();
    }

}
