package bot;

import Main.Universe;
import objects.Ball;
import physics.Vector2D;


/**
 * this is the class that performs the testShots for the initial velocity
 * once the shot is done we can extract the fitness function,
 * which is the distance between the target and the ball (its copy)
 */
public class TestShot {

    private final Ball ball;
    private final Universe universe;
    private double result = Integer.MAX_VALUE;


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

            // ball is in the resting position: target was not hit
            if ((!ball.isMoving() && !ball.getWillMove()) ) {
                this.result = universe.getTarget().getEuclideanDistance(ball.getPosition());
                return;
            }

            // target was hit
            if (ball.isOnTarget(universe.getTarget()) && ball.getVelocity().getMagnitude() < 0.5) {
                ball.setVelocity(new Vector2D(0,0));
                ball.setWillMove(false);
                this.result = 0;
                return;
            }
        }
    }

    /**
     *
     * @return 0 if the target was hit
     * in general the distance between the ball and the target
     */
    public double getTestResult() {
        return this.result;
    }
}
