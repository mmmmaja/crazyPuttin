package bot;


import physics.Vector2D;


/**
 * This bot performs random shots in all directions
 */
public class RandomBot extends Bot {


    public RandomBot() {
        setTestNumber(1500);
        setName("Random Bot");
    }


    /**
     * simulate the shot and pick the best one of all
     */
    @Override
    public void run() {

        for (int i = 0; i < this.getTestNumber(); i++) {

            setShotCounter(getShotCounter() + 1);

            // generate random velocity
            Vector2D initialVelocity = new Vector2D(
                    getRandomDoubleBetween(-5.0, 5.0),
                    getRandomDoubleBetween(-5.0, 5.0)
            );

            // Euclidean distance between the ball and the target
            double result = new TestShot(initialVelocity, this.getTargetPosition()).getTestResult();
            if (result < getBestResult()) {
                setBestResult(result);
                setBestVelocity(initialVelocity);
            }
            // ball was hit
            if (getBestResult() == 0) {
                stop();
            }
        }
        stop();
    }
}
