package bot;

import physics.Vector2D;

/**
 * used for the mazes
 */
public class ImprovedRuleBasedBot extends Bot{



    public ImprovedRuleBasedBot() {
        this.name = "ImprovedRuleBasedBot";
        initiate();
    }

    public ImprovedRuleBasedBot(boolean shootBall, Vector2D targetPosition) {
        this.targetPosition = targetPosition;
        this.shootBall = shootBall;
        this.name = "ImprovedRuleBasedBot";
        initiate();
    }

    public ImprovedRuleBasedBot(boolean shootBall) {
        this.shootBall = shootBall;
        this.name = "ImprovedRuleBasedBot";
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
        this.setHeuristics(Heuristics.finalPosition);
        Vector2D direction = getDirection();
        this.bestVelocity = new Vector2D(direction.getX(), direction.getY());

        double step = 0.1;
        double c = 0.1;
        while (true){
            Vector2D newVelocity = direction.multiply(c);
            if (newVelocity.getMagnitude() > 5) {
                break;
            }
            double result = new TestShot(this.universe, newVelocity, this.targetPosition, this.heuristics).getTestResult();
            if (result < this.bestResult) {
                this.bestResult = result;
                this.bestVelocity = newVelocity;
                if (bestResult < 0.05) {
                    break;
                }
            }
            c+= step;
        }
        this.shotCounter = 1;
//        System.out.println(bestVelocity);
//        System.out.println(bestVelocity.getMagnitude());
    }

    @Override
    public void run() {}
}
