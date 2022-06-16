package bot;

import physics.Vector2D;


public class ImprovedRandomBot extends Bot {

    private double range = 0; // the range we can rotate the angle of the velocity vector in each iteration


    public ImprovedRandomBot() {
        this.testNumber = 360;
        this.name = "Improved Random Bot";
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

        this.bestVelocity = direction.multiply(5);
        this.bestResult = new TestShot(bestVelocity, this.targetPosition, Heuristics.finalPosition).getTestResult();
        this.shotCounter++;

        // target was hit
        if (this.bestResult == 0) {
            stop();
        }
        int rangeToSearch = 88;

        int amountOfShotEachAngle = testNumber/rangeToSearch;
        if(amountOfShotEachAngle < 1 ) amountOfShotEachAngle = 1;

        for (int i = 0; i < testNumber; i++) {
            // widen the range of the rotation angle
            if (range < rangeToSearch && i % amountOfShotEachAngle == 0 ) {
                range++;
            }
            this.shotCounter++;

            Vector2D velocity = direction.
                    multiply(getRandomDoubleBetween(1, 5)).
                    rotate(getRandomWithinTwoRanges(-range, -range, range, range));
//                    rotate(range);
            // Euclidean distance between the ball and the target
            double result = new TestShot(velocity, this.targetPosition,Heuristics.finalPosition).getTestResult();
//            System.out.println("Improved->  "   + result);

            if (result < this.bestResult) {
                this.bestResult = result;
                this.bestVelocity = velocity;
            }
            // ball was hit
            if (this.bestResult == 0.0) {
                stop();
            }
        }
        stop();
    }


}