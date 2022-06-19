package bot;

import physics.Vector2D;


public class ImprovedRandomBot extends Bot {

    // the range we can rotate the angle of the velocity vector in each iteration
    private double range = 0;


    public ImprovedRandomBot() {
        setTestNumber(360);
        setName("Improved Random Bot");
    }


    /**
     * @return initial velocity to start random shots (direction from ball to the target)
     */
    private Vector2D analiseCourse() {
        Vector2D direction =  new Vector2D(
                this.universe.getTarget().getPosition().getX() - this.universe.getBall().getPosition().getX(),
                this.universe.getTarget().getPosition().getY() - this.universe.getBall().getPosition().getY()
        );
        direction = direction.getUnitVector();
        return direction;
    }

    @Override
    public void run() {

        // direction from ball to target
        Vector2D direction = analiseCourse();

        setBestVelocity(direction.multiply(5));
        setBestResult(new TestShot(getBestVelocity(), getTargetPosition(), Heuristics.finalPosition).getTestResult());
        setShotCounter(getShotCounter() + 1);

        // target was hit
        if (getBestResult() == 0) {
            stop();
        }
        int rangeToSearch = 88;

        int amountOfShotEachAngle = getTestNumber() / rangeToSearch;
        if (amountOfShotEachAngle < 1 ) {
            amountOfShotEachAngle = 1;
        }
        for (int i = 0; i < getTestNumber(); i++) {
            // widen the range of the rotation angle
            if (range < rangeToSearch && i % amountOfShotEachAngle == 0 ) {
                range++;
            }
            setShotCounter(getShotCounter() + 1);

            Vector2D velocity = direction.
                    multiply(getRandomDoubleBetween(1, 5)).
                    rotate(getRandomWithinTwoRanges(-range, -range, range, range));
            double result = new TestShot(velocity, this.getTargetPosition(),Heuristics.finalPosition).getTestResult();

            // better velocity was found
            if (result < getBestResult()) {
                setBestResult(result);
                setBestVelocity(velocity);
            }
            // ball was hit
            if (getBestResult() == 0) {
                stop();
            }
        }
        stop();
    }


}