package bot;

import Main.Shot;
import Main.Universe;
import physics.Vector2D;

import java.util.ArrayList;


/**
 * iterative algorithm: at each iteration we change the velocity and asses the change by the fitness value
 */
public class HillClimbingBot extends Bot {

    public HillClimbingBot() {
        this.targetPosition = universe.getTarget().getPosition();
        start();
    }

    /**
     * extended to fit AStarBot properties (specify the target position)
     */
    public HillClimbingBot(Vector2D targetPosition) {
        this.targetPosition = targetPosition;
        start();
    }

    @Override
    public void run() {

        double step = 0.01;

        Vector2D direction =  new Vector2D(
                this.universe.getTarget().getPosition().getX() - this.universe.getBall().getPosition().getX(),
                this.universe.getTarget().getPosition().getY() - this.universe.getBall().getPosition().getY()
        );
        this.bestVelocity = direction.getUnitVector();
        this.bestResult = new TestShot(this.universe, this.bestVelocity, this.targetPosition).getTestResult(this.heuristics);
        double[][] stepArray = {
                {step, 0},
                {-step, 0},
                {0, step},
                {0, -step}
        };
        boolean play = true;
        while (play && running) {
            play = false;
            this.shotCounter++;
            for (double[] stepCase : stepArray) {

                Vector2D testVelocity = new Vector2D(this.bestVelocity.getX() + stepCase[0], this.bestVelocity.getY() + stepCase[1]);
                double testResult = new TestShot(this.universe, testVelocity, this.targetPosition).getTestResult(Heuristics.allPositions);

                // target was reached: break all
                if (testResult == 0) {
                    this.bestVelocity = testVelocity;
                    this.bestResult = testResult;
                    play = false;
                    stop();
                }
                // climb the hill
                else if (testResult <= this.bestResult) {
                    this.bestVelocity = testVelocity;
                    this.bestResult = testResult;
                    play = true;
                }
            }
        }
        stop();
    }


    @Override
    public void shootBall() {
        new Shot(this.universe, this.bestVelocity);
        System.out.println(this);
    }
}
