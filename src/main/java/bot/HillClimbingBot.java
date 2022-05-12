package bot;

import Main.Universe;
import physics.Vector2D;

import java.util.ArrayList;


/**
 * iterative algorithm: at each iteration we change the velocity and asses the change by the fitness value
 * look at step: what should be the value be?
 */
public class HillClimbingBot implements Bot {

    private final Universe universe;
    private final Vector2D targetPosition;
    private final Heuristics heuristics = Heuristics.allPositions;

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

        Vector2D direction =  new Vector2D(
                this.universe.getTarget().getPosition().getX() - this.universe.getBall().getPosition().getX(),
                this.universe.getTarget().getPosition().getY() - this.universe.getBall().getPosition().getY()
        );
        Vector2D velocity = direction.getUnitVector();
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

    @Override
    public int getTotalShotCounter() {
        return this.shotCounter;
    }

    @Override
    public ArrayList<Vector2D> getVelocities() {
        ArrayList<Vector2D> velocities = new ArrayList<>();
        velocities.add(this.bestVelocity);
        return velocities;
    }

    @Override
    public String toString() {
        return "Hill Climbing Bot: "+
                "\nBest velocity: " + this.bestVelocity +
                "\nresult: " + this.bestResult +
                "\nshotCounter: " + this.shotCounter +
                "\nheuristics: " + this.heuristics+ "\n";
    }
}
