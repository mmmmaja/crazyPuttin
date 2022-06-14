package bot;

import Main.Main;
import physics.Vector2D;


public class RuleBasedBot extends Bot {

    public RuleBasedBot() {
        this.name = "RuleBasedBot";
    }


    /**
     * @return initial velocity to start random shots, which is direction from ball to the target
     */
    private Vector2D getDirection() {
        Vector2D direction =  new Vector2D(
                this.targetPosition.getX() - Main.getUniverse().getBall().getPosition().getX(),
                this.targetPosition.getY() - Main.getUniverse().getBall().getPosition().getY()
        );
        direction = direction.getUnitVector();
        return direction;

    }


    @Override
    public void run() {
        Vector2D direction = getDirection();

        // find the correct multiplier for the direction vector based on the distance
        double distance = this.targetPosition.getEuclideanDistance(universe.getBall().getPosition());
        double g = 9.81;
        double c = Math.sqrt(2 * g * universe.getFileReader().getKineticFriction() * distance);

        this.bestVelocity = direction.multiply(c);
        this.bestResult = new TestShot(this.bestVelocity, this.targetPosition, Heuristics.finalPosition).getTestResult();
        this.shotCounter = 1;

        if (this.shootBall) {
            shootBall();
        }
        stop();
    }
}
