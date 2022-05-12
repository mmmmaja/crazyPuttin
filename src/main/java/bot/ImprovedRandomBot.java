package bot;

import Main.Universe;
import objects.Obstacle;
import objects.TerrainGenerator;
import physics.Vector2D;

import java.util.ArrayList;

public class ImprovedRandomBot implements Bot {

    private final Universe universe;
    private final Vector2D targetPosition;
    private final Heuristics heuristics = Heuristics.finalPosition;

    private Vector2D bestVelocity;
    private double bestResult;
    private int shotCounter;

    public ImprovedRandomBot(Universe universe) {
        this.universe = universe;
        this.bestVelocity = new Vector2D(0, 0);
        this.shotCounter = 0;
        this.targetPosition = this.universe.getTarget().getPosition();
        startRandomTests(1000);
    }

    /**
     * extended to fit AStarBot properties
     */
    public ImprovedRandomBot(Universe universe, Vector2D targetPosition) {
        this.universe = universe;
        this.bestVelocity = new Vector2D(0, 0);
        this.shotCounter = 0;
        this.targetPosition = targetPosition;
        startRandomTests(1000);
    }

    public ImprovedRandomBot(Universe universe, int testNumber) {
        this.universe = universe;
        this.bestVelocity = new Vector2D(0, 0);
        this.shotCounter = 0;
        this.targetPosition = this.universe.getTarget().getPosition();
        startRandomTests(testNumber);
    }


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

        return direction;
    }

    public void startRandomTests(int testNumber) {

        Vector2D direction = analiseCourse();

        this.bestVelocity = direction;
        this.bestResult = new TestShot(this.universe, bestVelocity, this.targetPosition).getTestResult(this.heuristics);
        if (this.bestResult == 0) {
            return;
        }

        for (int i = 0; i < testNumber; i++) {
            this.shotCounter++;
            double range = 60 ;

            Vector2D velocity = direction.multiply( Obstacle.getRandomDouble(0.5,5)).
                    rotate(Obstacle.getRandomDouble(-1*range,range));

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
        return "Improved Random Bot: "+
                "\nBest velocity: " + this.bestVelocity +
                "\nresult: " + this.bestResult +
                "\nshotCounter: " + this.shotCounter +
                "\nheuristics: " + this.heuristics + "\n";
    }
}
