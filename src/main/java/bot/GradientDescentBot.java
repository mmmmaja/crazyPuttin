package bot;


import physics.Vector2D;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * not to be included in this phase
 */
public class GradientDescentBot extends Bot {

    private double learningRate = 0.9;


    public GradientDescentBot() {
        this.name = "Gradient Descent Bot";
    }


    @Override
    public void run() {

        int power = -8;
        double stepSize = 0.01;

        CountDownLatch improvedBotLatch = new CountDownLatch(1);

        ImprovedRandomBot bot = new ImprovedRandomBot();
        bot.setTestNumber(this.testNumber);
        bot.setShootBall(false);
        bot.setTargetPosition(this.targetPosition);
        bot.setBotLatch(improvedBotLatch);
        bot.start();
        try {
            // wait for the response from the Thread
            improvedBotLatch.await();
        } catch (InterruptedException ignored) {}
        Vector2D velocity = bot.getBestVelocity();
        this.shotCounter++;
        this.bestVelocity = velocity;
        this.bestResult = new TestShot(velocity, this.targetPosition, Heuristics.finalPosition).getTestResult();
//        System.out.println("FIRST -> " + bestResult);
        boolean play = true;
        ArrayList<Vector2D> testVelocities = new ArrayList<>();
        testVelocities.add(0, velocity);

        while (play) {

            if (bestResult == 0) {
                stop();
            }
            play = false;
            this.shotCounter++;
            double derivativeX = derivativeX(velocity, stepSize) * learningRate;
            double derivativeY = derivativeY(velocity, stepSize) * learningRate;

            Vector2D testVelocity = new Vector2D(
                    velocity.getX() - derivativeX,
                    velocity.getY() - derivativeY
            ).scaleDown(5);

            testVelocities.add(0, testVelocity);
            double testResult = new TestShot(testVelocity, this.targetPosition, Heuristics.finalPosition).getTestResult();
//            System.out.println("Test -> " + bestResult);

            // target was hit
            if (testResult == 0) {
                this.bestVelocity = testVelocity;
                this.bestResult = testResult;
                stop();
            } else if (testResult < this.bestResult) {
                velocity = testVelocities.get(0);
                this.bestResult = testResult;
                this.bestVelocity = velocity;
                play = true;
            }

            if (bestResult != 0 && testResult > bestResult && learningRate > Math.pow(10, power)) {
                testVelocities.remove(0);
                velocity = testVelocities.get(0);
                stepSize *= 0.5;
                learningRate *= 0.5;
                play = true;
            }
            bestVelocity = bestVelocity.scaleDown(5);
        }
        bestResult = new TestShot(bestVelocity, this.targetPosition,Heuristics.finalPosition).getTestResult();

        stop();
    }


    public double derivativeX(Vector2D velocity , double step){
        Vector2D position = this.universe.getBall().getPosition();
        Vector2D velocityPlus = velocity.add(step , 0 );
        Vector2D velocityMinus = velocity.add(-1 * step , 0 );
//        double testShotPlus = new TestShot(velocityPlus, this.targetPosition).getTestResult();
//        double testShotMinus = new TestShot(velocityMinus, this.targetPosition).getTestResult();
        double testShotMinus = this.universe.getSolver().calculateNext(position,velocityMinus,step)[0].getX();
        double testShotPlus = this.universe.getSolver().calculateNext(position,velocityPlus,step)[0].getX();
        return (testShotPlus - testShotMinus) / ( 2 * step) ;
    }

    public double derivativeY(Vector2D velocity, double step){
        Vector2D position = this.universe.getBall().getPosition();

        Vector2D velocityPlus = velocity.add(0 , step );
        Vector2D velocityMinus = velocity.add( 0 , -1.d * step );
//        double testShotPlus = new TestShot(velocityPlus, this.targetPosition).getTestResult();
//        double testShotMinus = new TestShot(velocityMinus, this.targetPosition).getTestResult();
        double testShotMinus = this.universe.getSolver().calculateNext(position,velocityMinus,step)[0].getX();
        double testShotPlus = this.universe.getSolver().calculateNext(position,velocityPlus,step)[0].getX();

        return (testShotPlus - testShotMinus) / ( 2 * step) ;
    }



}