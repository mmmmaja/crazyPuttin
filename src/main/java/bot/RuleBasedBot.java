package bot;

import Main.Main;
import Main.Universe;
import physics.Vector2D;


public class RuleBasedBot extends Bot {

    private Vector2D ballPosition = this.universe.getBall().getPosition();

    public RuleBasedBot() {
        this.name = "RuleBasedBot";
        initiate();
    }

    public RuleBasedBot(boolean shootBall, Vector2D targetPosition) {
        this.targetPosition = targetPosition;
        this.shootBall = shootBall;
        this.name = "RuleBasedBot";
        initiate();
    }

    public RuleBasedBot(boolean shootBall, Vector2D targetPosition, Vector2D ballPosition) {
        this.targetPosition = targetPosition;
        this.shootBall = shootBall;
        this.ballPosition = ballPosition;
        this.name = "RuleBasedBot";
        initiate();
    }

    public RuleBasedBot(boolean shootBall) {
        this.shootBall = shootBall;
        this.name = "RuleBasedBot";
        initiate();
    }

    /**
     * @return initial velocity to start random shots, which is direction from ball to the target
     */
    private Vector2D getDirection() {
        Vector2D direction =  new Vector2D(
                this.targetPosition.getX() - this.ballPosition.getX(),
                this.targetPosition.getY() - this.ballPosition.getY()
        );
        direction = direction.getUnitVector();
        return direction;

    }

    private void initiate() {


        Vector2D direction = getDirection();

        // find the correct multiplier for the direction vector based on the distance
        double distance = this.targetPosition.getEuclideanDistance(universe.getBall().getPosition());
        double g = 9.81;
        double c = Math.sqrt(2 * g * universe.getFileReader().getKineticFriction() * distance);

        this.bestVelocity = direction.multiply(c);
        this.bestResult = new TestShot(this.universe, this.bestVelocity, this.targetPosition, Heuristics.finalPosition).getTestResult();
        this.shotCounter = 1;

        if (this.shootBall) {
            shootBall();
        }
    }

    @Override
    public void run() {}
}
