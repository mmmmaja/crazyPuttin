package bot;

import Main.Universe;
import objects.Obstacle;
import objects.TerrainGenerator;
import physics.Vector2D;

public class ImprovedRandomBot {

    private final Universe universe;
    private final Vector2D targetPosition;
    private final Heuristics heuristics = Heuristics.finalPosition;
    private Vector2D direction;
    private Vector2D slopes;

    private Vector2D bestVelocity;
    private double bestResult;
    private int shotCounter;
    private int testNumber = 500;

    public ImprovedRandomBot(Universe universe) {
        this.universe = universe;
        this.bestVelocity = new Vector2D(0, 0);
        this.shotCounter = 0;
        this.targetPosition = this.universe.getTarget().getPosition();
        startRandomTests(this.testNumber);
    }

    /**
     * extended to fit AStarBot properties
     */
    public ImprovedRandomBot(Universe universe, Vector2D targetPosition) {
        this.universe = universe;
        this.bestVelocity = new Vector2D(0, 0);
        this.shotCounter = 0;
        this.targetPosition = targetPosition;
        startRandomTests(this.testNumber);
    }


    private void analiseCourse() {
        this.direction =  new Vector2D(
                this.universe.getTarget().getPosition().getX() - this.universe.getBall().getPosition().getX(),
                this.universe.getTarget().getPosition().getY() - this.universe.getBall().getPosition().getY()
        );
        double length = direction.getMagnitude();
        direction = direction.getUnitVector();

        Vector2D slopeTarget = TerrainGenerator.getSlopes(this.targetPosition);
        Vector2D slopeBall = TerrainGenerator.getSlopes(this.universe.getBall().getPosition());

//        this.slopes = new Vector2D(
//                (slopeTarget.getY() + slopeBall.getY()) / 2.d,
//                (slopeTarget.getX() + slopeBall.getX()) / 2.d
//        );
//        direction = direction.add(slopes);
        direction = direction.getUnitVector();
//        direction = direction.multiply(5);
    }


    public void setTestNumber(int testNumber) {
        this.testNumber = testNumber;
    }

    public void startRandomTests(int testNumber) {
        analiseCourse();

        this.bestVelocity = direction;
        this.bestResult = new TestShot(this.universe, bestVelocity, this.targetPosition).getTestResult(this.heuristics);
        if (this.bestResult == 0) {
            return;
        }

        for (int i = 0; i < testNumber; i++) {
            this.shotCounter++;
            double range = 50 ;

            Vector2D dir  = direction.multiply( Obstacle.getRandomDouble(5,5));
            Vector2D velocity = dir.rotate( Obstacle.getRandomDouble(-1*range,range));

            // distance between the ball and the target in 3D (takes height into consideration)
            double result = new TestShot(this.universe, velocity, this.targetPosition).getTestResult(this.heuristics);
            if (result < this.bestResult) {
                this.bestResult = result;
                this.bestVelocity = velocity;
            }
            // ball was hit
            if (this.bestResult == 0.0) {
                break;
            }
        }
    }

    /**
     *
     * @return initial velocity that gave the best shot out of every test
     */
    public Vector2D getBestVelocity() {
        return this.bestVelocity;
    }

    public int getShotCounter() {
        return this.shotCounter;
    }

    public String toString() {
        return "Improved Random Bot: "+
                "\nBest velocity: " + this.bestVelocity +
                "\nresult: " + this.bestResult +
                "\nshotCounter: " + this.shotCounter +
                "\nheuristics: " + this.heuristics + "\n";
    }
}
