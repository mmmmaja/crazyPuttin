package bot;

import Main.Universe;
import objects.Ball;
import objects.TerrainGenerator;
import physics.PhysicsEngine;
import physics.Vector2D;


/**
 * this is the class that performs the testShots for the initial velocity
 * once the shot is done we can extract the fitness function,
 * which is the distance between the target and the ball
 *
 * It doesn't affect the Ball object from the universe, but makes it's copy
 */
public class TestShot implements Comparable<TestShot>{

    private final Ball ball;
    private final Universe universe;
    private final Vector2D targetPosition;

    private double testResult = Integer.MAX_VALUE;
    private static final double WATER_PUNISHMENT = 10; // subtract the score when the ball is hit
    private static final double OBSTACLE_PUNISHMENT = 2; // subtract the score when the obstacle is hit


    public TestShot(Universe universe, Vector2D velocity, Vector2D targetPosition) {
        this.ball = universe.getBall().copyOf();
        this.universe = universe;
        this.ball.setVelocity(velocity);
        this.targetPosition = targetPosition;

        if (velocity.getMagnitude() > 5) {
            Vector2D unit_vector = velocity.getUnitVector();
            this.ball.setVelocity(new Vector2D(unit_vector.getX() * 5, unit_vector.getY() * 5)) ;
        }
        start();
    }


    private void start() {

        // while ball is moving do
        while (true) {

            universe.getSolver().nextStep(ball);
            double distance = ball.getPosition().getEuclideanDistance(this.targetPosition);

//             subtract points if ball hit the water
            if (TerrainGenerator.getHeight(ball.getPosition()) < 0) {
                distance+= WATER_PUNISHMENT;
            }
            // subtract the points if ball hit the obstacle
            if (universe.getSolver().PHYSICS.getCollisionCoordinates(ball) != null ) {
                distance += OBSTACLE_PUNISHMENT;
            }
            if (distance < this.testResult) {
                this.testResult = distance;
            }

            // target was hit
            if (ball.isOnTarget(universe.getTarget()) || ball.isOnTarget(this.targetPosition, this.universe.getFileReader().getTargetRadius())) {
                ball.setVelocity(new Vector2D(0, 0));
                ball.setWillMove(false);
                this.testResult =  0;
                break;
            }

            // ball is in the resting position: target was not hit
            if ((!ball.isMoving() && !ball.getWillMove())) {
                break;
            }
        }
    }


    /**
     * @return 0 if the target was hit otherwise
     * the distance between the ball and the target
     */
    public double getTestResult() {
        return this.testResult;
    }


    @Override
    public int compareTo(TestShot testShot) {
        return (this.testResult < testShot.getTestResult() ? -1 : 1);
    }

}
