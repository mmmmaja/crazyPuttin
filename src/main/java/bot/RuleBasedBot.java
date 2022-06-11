package bot;

import Main.Main;
import Main.Universe;
import physics.Vector2D;


public class RuleBasedBot extends Bot {


    public RuleBasedBot() {
        this.name = "RuleBasedBot";
        start();
    }

    public RuleBasedBot(boolean shootBall, Vector2D targetPosition) {
        this.targetPosition = targetPosition;
        this.shootBall = shootBall;
        this.name = "RuleBasedBot";
        start();
    }

    public RuleBasedBot(boolean shootBall) {
        this.shootBall = shootBall;
        this.name = "RuleBasedBot";
        start();
    }

    /**
     * @return initial velocity to start random shots, which is direction from ball to the target
     */
    private Vector2D getDirection() {
        Vector2D direction =  new Vector2D(
                this.targetPosition.getX() - this.universe.getBall().getPosition().getX(),
                this.targetPosition.getY() - this.universe.getBall().getPosition().getY()
        );
        direction = direction.getUnitVector();
        return direction;

    }

    @Override
    public void run() {
        Vector2D direction = getDirection();
        double distance = this.targetPosition.getEuclideanDistance(universe.getBall().getPosition());
        double c = Math.min(5, distance);
        this.bestVelocity = direction.multiply(c);
        this.bestResult = new TestShot(this.universe, this.bestVelocity, this.targetPosition).getTestResult();
        this.shotCounter = 1;
        stop();
    }
}
