package bot;

import Main.Universe;
import objects.Ball;
import physics.Vector2D;


/**
 * this is the class that performs the testShots for the initial velocity
 * once the shot is done we can extract the fitness function,
 * which is the distance between the target and the ball
 *
 * can include two heuristics to estimate the result of the testShot
 */
public class TestShot {

    private final Ball ball;
    private final Universe universe;

    private double resultFinalPosition = Integer.MAX_VALUE;
    private double resultAllPositions = Integer.MAX_VALUE;


    public TestShot(Universe universe, Vector2D velocity) {
        this.ball = universe.getBall().copyOf();
        this.universe = universe;
        this.ball.setVelocity(velocity);

        if (velocity.getMagnitude() > 5) {
            Vector2D unit_vector = new Vector2D(
                    velocity.getX() / velocity.getMagnitude(),
                    velocity.getY() / velocity.getMagnitude()
            );
            this.ball.setVelocity(new Vector2D(
                    unit_vector.getX() * 5,
                    unit_vector.getY() * 5)
            );
        }
        start();
    }

    private void start() {

        while (true) {

            universe.getSolver().nextStep(ball);
            double distance = universe.getTarget().getEuclideanDistance3D(ball.getPosition());
            if (distance < this.resultAllPositions) {
                this.resultAllPositions = distance;
            }

            // ball is in the resting position: target was not hit
            if ((!ball.isMoving() && !ball.getWillMove()) ) {
                this.resultFinalPosition = distance;
                return;
            }

            // target was hit
            if (ball.isOnTarget(universe.getTarget()) ) {
                ball.setVelocity(new Vector2D(0,0));
                ball.setWillMove(false);
                this.resultFinalPosition = this.resultAllPositions = 0;
                return;
            }
        }
    }



    /**
     *
     * @return 0 if the target was hit
     * in general the distance between the ball and the target
     */
    public double getTestResult(Heuristics heuristics) {
        if (heuristics.equals(Heuristics.finalPosition)) {
            return this.resultFinalPosition;
        }
        if (heuristics.equals(Heuristics.allPositions)) {
            return this.resultAllPositions;
        }
        return Integer.MAX_VALUE;
    }

}
