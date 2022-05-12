package bot;

import Main.Universe;
import objects.Ball;
import objects.TerrainGenerator;
import physics.Vector2D;


/**
 * this is the class that performs the testShots for the initial velocity
 * once the shot is done we can extract the fitness function,
 * which is the distance between the target and the ball
 *
 * can include two heuristics to estimate the result of the testShot
 */
public class TestShot implements Comparable<TestShot>{

    private final Ball ball;
    private final Universe universe;
    private final Vector2D targetPosition;

    private double resultFinalPosition = Integer.MAX_VALUE;
    private double resultAllPositions = Integer.MAX_VALUE;


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
        while (true) {

            universe.getSolver().nextStep(ball);
            double distance = ball.getPosition().getEuclideanDistance(this.targetPosition);
            if (distance < this.resultAllPositions) {
                this.resultAllPositions = distance;
            }

            // ball is in the resting position: target was not hit
            if ((!ball.isMoving() && !ball.getWillMove())) {
                if (TerrainGenerator.getHeight(ball.getPosition()) < 0) {
                    distance= Integer.MAX_VALUE;
                }
                this.resultFinalPosition = distance;
                break;
            }

            // target was hit
            if (ball.isOnTarget(universe.getTarget()) || ball.isOnTarget(this.targetPosition, this.universe.getFileReader().getTargetRadius())) {
                ball.setVelocity(new Vector2D(0, 0));
                ball.setWillMove(false);
                this.resultFinalPosition = this.resultAllPositions = 0;
                break;
            }
        }
    }



    /**
     * @param heuristics see enum options
     * @return 0 if the target was hit otherwise
     * the distance between the ball and the target
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


    @Override
    public int compareTo(TestShot testShot) {
        return (this.getTestResult(Heuristics.allPositions) < testShot.getTestResult(Heuristics.allPositions)) ? -1 : 1;
    }

    public Vector2D getFinalPosition() {
        return this.ball.getPosition();
    }

    public Universe getUniverse() {
        return universe;
    }
}
