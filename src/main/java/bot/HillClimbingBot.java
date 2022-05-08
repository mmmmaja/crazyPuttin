package bot;

import Main.Universe;
import physics.Vector2D;


/**
 * iterative algorithm: at each iteration we change the velocity and asses the change by the fitness value
 * look at step: what should be the value be?
 */
public class HillClimbingBot {

    private final Universe universe;
    private final Vector2D targetPosition;
    private final Heuristics heuristics = Heuristics.finalPosition;

    private final Vector2D bestVelocity;
    private double bestResult;
    private int shotCounter = 0;


    public HillClimbingBot(Universe universe) {
        this.universe = universe;
        this.targetPosition = universe.getTarget().getPosition();
        this.bestVelocity = climb();
    }

    public HillClimbingBot(Universe universe, Vector2D targetPosition) {
        this.universe = universe;
        this.targetPosition = targetPosition;
        this.bestVelocity = climb();
    }

    private Vector2D climb() {
        double step = 0.01;

        Vector2D velocity = new Vector2D(0, 0);
        this.bestResult = new TestShot(this.universe, velocity, this.targetPosition).getTestResult(this.heuristics);
        double[][] stepArray = {
                {step, 0},
                {-step, 0},
                {0, step},
                {0, -step}
        };
        boolean play = true;

        while (play) {
            play = false;
            this.shotCounter++;
            for (double[] stepCase : stepArray) {

                Vector2D testVelocity = new Vector2D(velocity.getX() + stepCase[0], velocity.getY() + stepCase[1]);
                double testResult = new TestShot(this.universe, testVelocity, this.targetPosition).getTestResult(Heuristics.allPositions);

                // target was reached: break all
                if (testResult == 0) {
                    velocity = testVelocity;
                    this.bestResult = testResult;
                    play = false;
                    break;
                }
                // climb the hill
                else if (testResult <= this.bestResult) {
                    velocity = testVelocity;
                    this.bestResult = testResult;
                    play = true;
                }
            }
        }
        return velocity;
    }

    public Vector2D getBestVelocity() {
        return this.bestVelocity;
    }

    public int getShotCounter() {
        return this.shotCounter;
    }

    public String toString() {
        return "Hill Climbing Bot v2: "+
                "\nBest velocity: " + this.bestVelocity +
                "\nresult: " + this.bestResult +
                "shotCounter: " + this.shotCounter +
                "heuristics: " + this.heuristics+ "\n";
    }
}
