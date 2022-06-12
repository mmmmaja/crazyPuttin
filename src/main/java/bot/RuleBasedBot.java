package bot;

import Main.Main;
import Main.Universe;
import physics.Vector2D;


public class RuleBasedBot extends Bot {


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
                this.targetPosition.getX() - this.universe.getBall().getPosition().getX(),
                this.targetPosition.getY() - this.universe.getBall().getPosition().getY()
        );
        direction = direction.getUnitVector();
        return direction;

    }

    private void initiate() {


        Vector2D direction = getDirection();

        double distance = this.targetPosition.getEuclideanDistance(universe.getBall().getPosition());
        double g = 9.81;
        double c = Math.sqrt(2 * g * universe.getFileReader().getKineticFriction() * distance);
        System.out.println(c);

        this.bestVelocity = direction.multiply(c);
        this.bestResult = new TestShot(this.universe, this.bestVelocity, this.targetPosition, Heuristics.finalPosition).getTestResult();
        this.shotCounter = 1;
        System.out.println("bestResult: "+this.bestResult);

        if (this.shootBall) {
            shootBall();
        }
    }

    @Override
    public void run() {}
}
