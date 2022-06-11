package bot;

import physics.Vector2D;


public class ImprovedRandomBot extends Bot {

    private int testNumber = 3600;
    private double range = 0; // the range we can rotate the angle of the velocity vector in each iteration


    public ImprovedRandomBot() {
        this.name = "Improved Random Bot";
        start();
    }

    /**
     * @param targetPosition to be specified for the maze bot
     */
    public ImprovedRandomBot(boolean shootBall, Vector2D targetPosition) {
        this.shootBall = shootBall;
        this.targetPosition = targetPosition;
        this.name = "Improved Random Bot";
        start();
    }

    /**
     * @param testNumber number of simulations to run the bot
     */
    public ImprovedRandomBot(int testNumber) {
        this.testNumber = testNumber;
        this.name = "Improved Random Bot";
        start();
    }

    /**
     * @param testNumber number of simulations to run the bot
     * @param shootBall true if at the end shot should be display and Ball() object modified
     */
    public ImprovedRandomBot(int testNumber, boolean shootBall) {
        this.testNumber = testNumber;
        this.shootBall = shootBall;
        this.name = "Improved Random Bot";
        start();
    }

    /**
     * @param shootBall true if at the end shot should be display and Ball() object modified
     */
    public ImprovedRandomBot(boolean shootBall) {
        this.shootBall = shootBall;
        this.name = "Improved Random Bot";
        start();
    }

    /**
     * @return initial velocity to start random shots (direction from ball to the target)
     */
    private Vector2D analiseCourse() {
        Vector2D direction =  new Vector2D(
                this.universe.getTarget().getPosition().getX() - this.universe.getBall().getPosition().getX(),
                this.universe.getTarget().getPosition().getY() - this.universe.getBall().getPosition().getY()
        );
        direction = direction.getUnitVector();
        return direction;
    }

    @Override
    public void run() {
        // direction from ball to target
        Vector2D direction = analiseCourse();

        this.bestVelocity = direction.multiply(5);
        this.bestResult = new TestShot(this.universe, bestVelocity, this.targetPosition).getTestResult();
        this.shotCounter++;

        // target was hit
        if (this.bestResult == 0) {
            stop();
        }
        int rangeToSearch = 90;
        for (int i = 0; i < testNumber; i++) {
            // widen the range of the rotation angle
            if (range < rangeToSearch && i % 40 == 0 ) {
                range++;
            }
            this.shotCounter++;

            Vector2D velocity = direction.
                    multiply(getRandomDoubleBetween(0.1, 5)).
                    rotate(getRandomWithinTwoRanges(-range - 1, -range, range, range + 1));

            // Euclidean distance between the ball and the target
            double result = new TestShot(this.universe, velocity, this.targetPosition).getTestResult();
            if (result < this.bestResult) {
                this.bestResult = result;
                this.bestVelocity = velocity;
            }
            // ball was hit
            if (this.bestResult == 0.0) {
                stop();
            }
        }
        stop();
    }

}