package bot;

import Main.Universe;
import physics.Vector2D;


import java.util.Random;


/**
 * This bot performs random steps and chooses the best one based on the heuristics
 * shotCounter can be used to assess the heuristics used
 */
public class RandomBot {

    private final Universe universe;
    private Vector2D bestVelocity;
    private double bestResult;
    private int shotCounter;


    public RandomBot(Universe universe) {
        this.universe = universe;
        this.bestVelocity = new Vector2D(0, 0);
        this.shotCounter = 0;
        startRandomTests();
    }

    /**
     * TODO include boundaries for the initialVelocity
     */
    private void startRandomTests() {

        // maximal number of trial shots
        int testNumber = 2000;
        Random random = new Random();
        this.bestResult = Integer.MAX_VALUE;

        for (int i = 0; i < testNumber; i++) {
            shotCounter++;

            int sign1 = (random.nextDouble() < 0.5) ? 1 : -1;
            int sign2 = (random.nextDouble() < 0.5) ? 1 : -1;

            Vector2D initialVelocity = new Vector2D(
                    random.nextDouble() * random.nextInt(10) * sign1,
                    random.nextDouble() * random.nextInt(10) * sign2
            );
            // distance between the ball and the target in 3D (takes height into consideration)
            double result = new TestShot(this.universe, initialVelocity).getTestResult(Heuristics.finalPosition);
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
        System.out.println("\n\nBest velocity: "+this.bestVelocity+"\nresult: "+this.bestResult);
        System.out.println("shotCounter: "+this.shotCounter);
        return this.bestVelocity;
    }

    public int getShotCounter() {
        return this.shotCounter;
    }

}
