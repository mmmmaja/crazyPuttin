package bot;

import Main.Universe;
import physics.Vector2D;


import java.util.Random;

public class RandomBot {

    private final Universe universe;
    private Vector2D bestVelocity;
    private double bestResult;


    public RandomBot(Universe universe) {
        this.universe = universe;
        this.bestVelocity = new Vector2D(0, 0);
        startRandomTests();
    }

    private void startRandomTests() {
        // number of tries to shoot a ball
        int testNumber = 1000;

        Random random = new Random();
        this.bestResult = Integer.MAX_VALUE;

        for (int i = 0; i < testNumber; i++) {

            // FIXME adjust nextInt
            // (maybe oo find the distance between the target and the ball and the difference in height)
            Vector2D initialVelocity = new Vector2D(
                    random.nextDouble() * random.nextInt(20),
                    random.nextDouble() * random.nextInt(20)
            );
            // the smaller result the better
            double result = new TestShot(this.universe, initialVelocity).getTestResult();
            if (result < this.bestResult) {
                this.bestResult = result;
                this.bestVelocity = initialVelocity;
            }
            if (this.bestResult == 0.0) {
                break;
            }
        }
    }

    public Vector2D getBestVelocity() {
        System.out.println("\n\nBest velocity: "+this.bestVelocity+"\nresult: "+this.bestResult);
        return this.bestVelocity;
    }
}
