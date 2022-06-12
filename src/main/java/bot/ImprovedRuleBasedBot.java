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
        Vector2D direction = getDirection();
        this.bestVelocity = new Vector2D(direction.getX(), direction.getY());

        double step = 0.01;
        double c = 0.1;
        while (true){
            Vector2D newVelocity = direction.multiply(c);
            if (newVelocity.getMagnitude() > 5) {
                break;
            }
            double result = new TestShot(this.universe, newVelocity, this.targetPosition, Heuristics.finalPosition).getTestResult();
            if (result < this.bestResult) {
                this.bestResult = result;
                this.bestVelocity = newVelocity;
                if (bestResult < 0.025) {
                    break;
                }
            }
            c+= step;
        }
        this.shotCounter = 1;

        // looking for : C
        // having direction
        double distance = this.targetPosition.getEuclideanDistance(this.universe.getBall().getPosition());
//        System.out.println("distance: "+distance);
//        System.out.println("c: "+c);
//        double goldenRation = c / (distance * 10);
        // c = (distance * sth) / goldenRatio THE BIGGER THE DISTANCE THE BIGGER C IS

        if (this.shootBall) {
            shootBall();
        }

//        System.out.println("GR: "+goldenRation);
//        System.out.println(this.bestResult);
    }

    @Override
    public void run() {}
}
