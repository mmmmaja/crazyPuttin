package bot;

import Main.Universe;
import objects.Obstacle;
import physics.Vector2D;

import java.util.Random;

public class ImprovedRandomBot {

    private final Universe universe;
    private final Vector2D targetPosition;

    private Vector2D bestVelocity;
    private double bestResult;
    private int shotCounter;

    public ImprovedRandomBot(Universe universe) {
        this.universe = universe;
        this.bestVelocity = new Vector2D(0, 0);
        this.shotCounter = 0;
        this.targetPosition = this.universe.getTarget().getPosition();
        startRandomTests(1000, analiseCourse());
    }

    /**
     * extended to fit AStarBot properties
     */
    public ImprovedRandomBot(Universe universe, Vector2D targetPosition) {
        this.universe = universe;
        this.bestVelocity = new Vector2D(0, 0);
        this.shotCounter = 0;
        this.targetPosition = targetPosition;
        startRandomTests(1000, analiseCourse());
    }


    private Vector2D analiseCourse() {
        Vector2D direction =  new Vector2D(
                this.universe.getTarget().getPosition().getX() - this.universe.getBall().getPosition().getX(),
                this.universe.getTarget().getPosition().getY() - this.universe.getBall().getPosition().getY()
        );
        double length = Math.sqrt(Math.pow(direction.getX(), 2) + Math.pow(direction.getY(), 2));
        direction.multiply(5.0 * length);
        return direction;
    }



    public void startRandomTests(int testNumber, Vector2D direction) {

        this.bestVelocity = direction;
        this.bestResult = new TestShot(this.universe, bestVelocity, this.targetPosition).getTestResult(Heuristics.finalPosition);
        if (this.bestResult == 0) {
            return;
        }

        for (int i = 0; i < testNumber; i++) {
            this.shotCounter++;

            Vector2D velocity = new Vector2D(
                    direction.getX() * Obstacle.getRandomDouble(0.5, 1.5),
                    direction.getY() * Obstacle.getRandomDouble(0.5, 1.5)
            );

            // distance between the ball and the target in 3D (takes height into consideration)
            double result = new TestShot(this.universe, velocity, this.targetPosition).getTestResult(Heuristics.finalPosition);
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
        System.out.println("\n\nBest velocity: "+this.bestVelocity+"\nresult: "+this.bestResult);
        System.out.println("shotCounter: "+this.shotCounter);
        return this.bestVelocity;
    }

    public int getShotCounter() {
        return this.shotCounter;
    }
}
