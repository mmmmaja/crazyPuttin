package bot;


import physics.Vector2D;


/**
 * This bot performs random shots in all directions
 */
public class RandomBot extends Bot {

    private int testNumber = 1500;


    public RandomBot() {
        this.name = "Random Bot";
        start();
    }


    /**
     * @param testNumber number of simulations to run the bot
     */
    public RandomBot(int testNumber) {
        this.testNumber = testNumber;
        this.name = "Random Bot";
        start();
    }

    /**
     * @param testNumber number of simulations to run the bot
     * @param shootBall true if at the end shot should be display and Ball() object modified
     */
    public RandomBot(int testNumber, boolean shootBall) {
        this.testNumber = testNumber;
        this.shootBall = shootBall;
        this.targetPosition = this.universe.getTarget().getPosition();
        this.name = "Random Bot";
        start();
    }

    /**
     * @param targetPosition to be specified for the maze bot
     */
    public RandomBot(Vector2D targetPosition) {
        this.targetPosition = targetPosition;
        this.name = "Random Bot";
        start();
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
                    getRandomDouble(-5.0, 5.0),
                    getRandomDouble(-5.0, 5.0)
            );

            // Euclidean distance between the ball and the target
            double result = new TestShot(this.universe, initialVelocity, this.targetPosition).getTestResult();
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
