package bot;

import Main.Shot;
import objects.Obstacle;
import objects.TerrainGenerator;
import physics.Vector2D;

public class ImprovedRandomBot extends Bot {

    private int testNumber = 1000;

    public ImprovedRandomBot() {
        this.targetPosition = this.universe.getTarget().getPosition();
        this.name = "Improved Random Bot";
        start();
    }

    /**
     * extended to fit AStarBot properties (specify the target position)
     */
    public ImprovedRandomBot(Vector2D targetPosition) {
        this.targetPosition = targetPosition;
        this.name = "Improved Random Bot";
        start();
    }

    public ImprovedRandomBot(int testNumber) {
        this.testNumber = testNumber;
        this.targetPosition = this.universe.getTarget().getPosition();
        this.name = "Improved Random Bot";
        start();
    }

    public ImprovedRandomBot(int testNumber, boolean shootBall) {
        this.testNumber = testNumber;
        this.shootBall = shootBall;
        this.targetPosition = this.universe.getTarget().getPosition();
        this.name = "Improved Random Bot";
        start();
    }

    /**
     * @return initial velocity to start random shots, which is direction from ball to the target
     */
    private Vector2D analiseCourse() {
        Vector2D direction =  new Vector2D(
                this.universe.getTarget().getPosition().getX() - this.universe.getBall().getPosition().getX(),
                this.universe.getTarget().getPosition().getY() - this.universe.getBall().getPosition().getY()
        );
        direction = direction.getUnitVector();

        Vector2D slopeTarget = TerrainGenerator.getSlopes(this.targetPosition);
        Vector2D slopeBall = TerrainGenerator.getSlopes(this.universe.getBall().getPosition());

        Vector2D slope = new Vector2D(
                (slopeTarget.getY() + slopeBall.getY()) / 2.d,
                (slopeTarget.getX() + slopeBall.getX()) / 2.d
        );
        System.out.println("slope: "+slope);
        return direction;
    }

    @Override
    public void run() {
        Vector2D direction = analiseCourse();

        this.bestVelocity = direction;
        this.bestResult = new TestShot(this.universe, bestVelocity, this.targetPosition).getTestResult();
        if (this.bestResult == 0) {
            stop();
        }

        for (int i = 0; i < testNumber; i++) {
            this.shotCounter++;
            double range = 60; // FIXME make range dependant on the slope

            Vector2D velocity = direction.
                    multiply(Obstacle.getRandomDouble(5, 5)).
                    rotate(Obstacle.getRandomDouble(-1 * range, range));

            // distance between the ball and the target in 3D (takes height into consideration)
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
