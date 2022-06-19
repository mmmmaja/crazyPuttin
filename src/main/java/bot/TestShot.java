package bot;

import Main.Main;
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
 * It doesn't affect the Ball object from the universe, but makes its copy
 */
public class TestShot implements Comparable<TestShot>{

    private final Universe universe = Main.getUniverse();

    // copy of the ball from Universe class
    private final Ball ball;

    // where to shoot the ball (could be target or node on the path to the target)
    private final Vector2D targetPosition;

    // indicates how to evaluate the shoot (see enum options)
    private Heuristics heuristics = Heuristics.allPositions;

    // distance between the ball and the target depending on the heuristics
    private double testResult = Integer.MAX_VALUE;

    // subtract the score when the ball is hit
    private static final double WATER_PUNISHMENT = 10;

    // subtract the score when the obstacle is hit
    private static final double OBSTACLE_PUNISHMENT = 0;

    private double tolerance = 0.025;

    /**
     * @param velocity to shoot the ball with
     * @param targetPosition where to shoot the ball (could be target or node on the path to the target)
     */
    public TestShot(Vector2D velocity, Vector2D targetPosition) {
        this.ball = universe.getBall().copyOf();
        this.ball.setVelocity(velocity);
        this.targetPosition = targetPosition;
        this.tolerance = universe.getTarget().getCylinder().getRadius();
        initiate(velocity);
    }

    /**
     * @param velocity to shoot the ball with
     * @param targetPosition where to shoot the ball (could be target or node on the path to the target)
     * @param heuristics indicates how to evaluate the shoot (see enum options)
     */
    public TestShot(Vector2D velocity, Vector2D targetPosition, Heuristics heuristics) {
        this.ball = universe.getBall().copyOf();
        this.ball.setVelocity(velocity);
        this.targetPosition = targetPosition;
        this.heuristics = heuristics;;
        initiate(velocity);
    }

    /**
     * @param velocity to shoot the ball with
     */
    private void initiate(Vector2D velocity) {
        if (velocity.getMagnitude() > 5) {
            Vector2D unit_vector = velocity.getUnitVector();
            this.ball.setVelocity(new Vector2D(unit_vector.getX() * 5, unit_vector.getY() * 5)) ;
        }
        start();
    }


    private void start() {

        // while ball is moving iterate
        while (true) {

            universe.getSolver().nextStep(ball);
            double distance = ball.getPosition().getEuclideanDistance(this.targetPosition);

            // subtract points if ball hit the water
            if (TerrainGenerator.getHeight(ball.getPosition()) < 0) {
                distance+= WATER_PUNISHMENT;
            }
            // subtract the points if ball hit the obstacle
            if (universe.getSolver().PHYSICS.getCollisionCoordinates(ball) != null ) {
                distance += OBSTACLE_PUNISHMENT;
            }
            if (this.heuristics.equals(Heuristics.allPositions) && distance < this.testResult ) {
                this.testResult = distance;
            }

            // REAL target was hit
            if (ball.isOnTarget(this.universe.getTarget().getPosition(),tolerance)) {
                ball.setVelocity(new Vector2D(0, 0));
                ball.setWillMove(false);
                if (this.heuristics == Heuristics.allPositions) {
                    this.testResult = 0;
                    break;
                }
            }

            // ball is in the resting position: target was not hit
            if ((!ball.isMoving() && !ball.getWillMove())) {
                if (this.heuristics == Heuristics.finalPosition) {
                    this.testResult = distance;
                }
                else if (distance < this.testResult && this.heuristics == Heuristics.allPositions) {
                    this.testResult = distance;
                }
                break;
            }
        }
    }


    /**
     * @return the distance between the ball and the target
     */
    public double getTestResult() {
        return this.testResult;
    }


    @Override
    public int compareTo(TestShot testShot) {
        return (this.testResult < testShot.getTestResult() ? -1 : 1);
    }

}
