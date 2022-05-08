package bot;

import Main.Universe;
import objects.Obstacle;
import physics.Vector2D;


import java.util.Random;


/**
 * This bot performs random steps and chooses the best one based on the heuristics
 * shotCounter can be used to assess the heuristics used
 */
public class RandomBot {

    private final Universe universe;
    private final Vector2D targetPosition;
    private final Heuristics heuristics = Heuristics.finalPosition;

    private Vector2D bestVelocity;
    private double bestResult;
    private int shotCounter;
    private int testNumber = 1000;

    public RandomBot(Universe universe) {
        this.universe = universe;
        this.bestVelocity = new Vector2D(0, 0);
        this.shotCounter = 0;
        this.targetPosition = this.universe.getTarget().getPosition();
        startRandomTests(this.testNumber);
    }

    public RandomBot(Universe universe, int testNumber) {
        this.universe = universe;
        this.bestVelocity = new Vector2D(0, 0);
        this.shotCounter = 0;
        this.targetPosition = this.universe.getTarget().getPosition();
        this.testNumber = testNumber;
        startRandomTests(this.testNumber);
    }

    /**
     * extended to fit AStarBot properties
     */
    public RandomBot(Universe universe, Vector2D targetPosition) {
        this.universe = universe;
        this.bestVelocity = new Vector2D(0, 0);
        this.shotCounter = 0;
        this.targetPosition = targetPosition;
        startRandomTests(this.testNumber);
    }

    public void setTestNumber(int testNumber) {
        this.testNumber = testNumber;
    }


    public void startRandomTests(int testNumber) {

        // maximal number of trial shots
        Random random = new Random();
        this.bestResult = Integer.MAX_VALUE;

        for (int i = 0; i < testNumber; i++) {
            shotCounter++;

            Vector2D initialVelocity = new Vector2D(
                    Obstacle.getRandomDouble(-5.0, 5.0),
                    Obstacle.getRandomDouble(-5.0, 5.0)
            );

            // distance between the ball and the target in 3D (takes height into consideration)
            double result = new TestShot(this.universe, initialVelocity, this.targetPosition).getTestResult(this.heuristics);
            if (result < this.bestResult) {
                this.bestResult = result;
                this.bestVelocity = initialVelocity;
            }
            // ball was hit
            if (this.bestResult == 0.0) {
                break;
            }
        }
    }

    /**
     *
     * @return initial velocity that gave the best shot out of every test
     */
    public Vector2D getBestVelocity() {
        return this.bestVelocity;
    }

    public int getShotCounter() {
        return this.shotCounter;
    }

    public String toString() {
        return "Random Bot: "+
                "\nBest velocity: " + this.bestVelocity +
                "\nresult: " + this.bestResult +
                "\nshotCounter: " + this.shotCounter +
                "\nheuristics: " + this.heuristics + "\n";
    }


}
