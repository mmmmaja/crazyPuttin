package bot;

import physics.Vector2D;


/**
 * Advanced bot
 */
public class HillClimbingBot extends Bot {


    public HillClimbingBot() {
        this.name = "hillClimbingBot";
        start();
    }

    /**
     * @param targetPosition to be specified for the maze bot
     */
    public HillClimbingBot(Vector2D targetPosition) {
        this.name = "hillClimbingBot";
        this.targetPosition = targetPosition;
        start();
    }

    /**
     * @param shootBall true if at the end shot should be display and Ball() object modified
     */
    public HillClimbingBot(boolean shootBall) {
        this.name = "hillClimbingBot";
        this.shootBall = shootBall;
        start();
    }


    @Override
    public void run() {

        double step = 0.01;

        // direction from ball to target
        Vector2D direction =  new Vector2D(
                this.universe.getTarget().getPosition().getX() - this.universe.getBall().getPosition().getX(),
                this.universe.getTarget().getPosition().getY() - this.universe.getBall().getPosition().getY()
        );
        this.bestVelocity = direction.getUnitVector();
        this.bestResult = new TestShot(this.universe, this.bestVelocity, this.targetPosition).getTestResult();

        double[][] stepArray = {
                {step, 0},
                {-step, 0},
                {0, step},
                {0, -step}
        };
        boolean play = true;

        // while adding step to velocity improves the result do
        while (play && running) {
            play = false;
            this.shotCounter++;
            for (double[] stepCase : stepArray) {

                // test the whether the testResult was improved after altering the velocity
                Vector2D testVelocity = new Vector2D(this.bestVelocity.getX() + stepCase[0], this.bestVelocity.getY() + stepCase[1]);
                double testResult = new TestShot(this.universe, testVelocity, this.targetPosition).getTestResult();

                // target was reached: break all
                if (testResult == 0) {
                    this.bestVelocity = testVelocity;
                    this.bestResult = testResult;
                    play = false;
                    stop();
                }
                // climb the hill, set the new state
                else if (testResult <= this.bestResult) {
                    this.bestVelocity = testVelocity;
                    this.bestResult = testResult;
                    play = true;
                }
            }
        }
        stop();
    }
}
