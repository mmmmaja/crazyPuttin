package bot;

import Main.Universe;
import objects.Obstacle;
import physics.Vector2D;


import java.util.ArrayList;
import java.util.Random;


/**
 * This bot performs random steps and chooses the best one based on the heuristics
 * shotCounter can be used to assess the heuristics used
 */
public class RandomBot implements Bot {

    private final Universe universe;
    private final Vector2D targetPosition;
    private final Heuristics heuristics = Heuristics.finalPosition;

    private Vector2D bestVelocity;
    private double bestResult;
    private int shotCounter;
    private final ArrayList<Vector2D> velocities = new ArrayList<>();

    public RandomBot(Universe universe) {
        this.universe = universe;
        this.bestVelocity = new Vector2D(0, 0);
        this.shotCounter = 0;
        this.targetPosition = this.universe.getTarget().getPosition();
        startRandomTests(1000);
    }


    /**
     * constructor used for the hillClimbing bot to specify number of trials
     */
    public RandomBot(Universe universe, int testNumber) {
        this.universe = universe;
        this.bestVelocity = new Vector2D(0, 0);
        this.shotCounter = 0;
        this.targetPosition = this.universe.getTarget().getPosition();
        startRandomTests(testNumber);
    }

    /**
     * extended to fit AStarBot properties (specify the target position)
     */
    public RandomBot(Universe universe, Vector2D targetPosition) {
        this.universe = universe;
        this.bestVelocity = new Vector2D(0, 0);
        this.shotCounter = 0;
        this.targetPosition = targetPosition;
        startRandomTests(1000);
    }

    /**
     * performs number shots and picks the best one
     * @param testNumber number of testShots performed
     */
    public void startRandomTests(int testNumber) {

        this.bestResult = Integer.MAX_VALUE;

        for (int i = 0; i < testNumber; i++) {
            shotCounter++;

            Vector2D initialVelocity = new Vector2D(
                    Obstacle.getRandomDouble(-5.0, 5.0),
                    Obstacle.getRandomDouble(-5.0, 5.0)
            );

            // euclidean distance between the ball and the target
            double result = new TestShot(this.universe, initialVelocity, this.targetPosition).getTestResult(this.heuristics);
            if (result < this.bestResult) {
                this.bestResult = result;
                this.bestVelocity = initialVelocity;
                velocities.add(0,bestVelocity);
            }
            // ball was hit
            if (this.bestResult == 0.0) {
                break;
            }
        }
    }

    @Override
    public ArrayList<Vector2D> getVelocities() {
        return velocities;
    }

    @Override
    public int getTotalShotCounter() {
        return this.shotCounter;
    }


    @Override
    public String toString() {
        return "Random Bot: "+
                "\nBest velocity: " + this.bestVelocity +
                "\nresult: " + this.bestResult +
                "\nshotCounter: " + this.shotCounter +
                "\nheuristics: " + this.heuristics + "\n";
    }


}
