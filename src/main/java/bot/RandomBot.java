package bot;


import physics.Vector2D;


/**
 * This bot performs random shots in all directions
 */
public class RandomBot extends Bot {


    public RandomBot() {
        this.testNumber = 1500;
        this.name = "Random Bot";
    }


    /**
     * simulate the shot and pick the best one of all
     */
    @Override
    public void run() {

        for (int i = 0; i < testNumber; i++) {
            // Thread was killed
            if (!running) {
                stop();
            }
            this.shotCounter++;

            // generate random velocity
            Vector2D initialVelocity = new Vector2D(
                    getRandomDoubleBetween(-5.0, 5.0),
                    getRandomDoubleBetween(-5.0, 5.0)
            );

            // Euclidean distance between the ball and the target
            double result = new TestShot(initialVelocity, this.targetPosition).getTestResult();
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
