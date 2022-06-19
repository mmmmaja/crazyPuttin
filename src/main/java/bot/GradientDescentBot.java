package bot;


import physics.Vector2D;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;


public class GradientDescentBot extends Bot {

    // how fast the bot adapts and changes velocity
    private double learningRate = 0.9;


    public GradientDescentBot() {
        setName("Gradient Descent Bot");
    }

    /**
     * @return initial velocity that was found using improvedRandomBot
     * initial velocity will be used before triggering GradientDescent
     */
    private Vector2D findInitialVelocity() {

        // will inform about execution of the Thread
        CountDownLatch improvedBotLatch = new CountDownLatch(1);

        // run improvedRandomBot to find initial velocity, wait for the response
        ImprovedRandomBot bot = new ImprovedRandomBot();
        bot.setTestNumber(getTestNumber());
        bot.setShootBall(false);
        bot.setTargetPosition(getTargetPosition());
        bot.setBotLatch(improvedBotLatch);
        bot.start();

        try {
            // wait for the response from the Thread
            improvedBotLatch.await();
        } catch (InterruptedException ignored) {}

        // initial velocity was found
        Vector2D velocity = bot.getBestVelocity();
        setShotCounter(getShotCounter() + 1);
        setBestVelocity(velocity);
        setBestResult(new TestShot(velocity, this.getTargetPosition(), Heuristics.finalPosition).getTestResult());
        return velocity;
    }


    @Override
    public void run() {

        int power = -8;
        double stepSize = 0.01;

        Vector2D velocity = findInitialVelocity();
        boolean play = true;
        ArrayList<Vector2D> testVelocities = new ArrayList<>();
        testVelocities.add(0, velocity);

        while (play) {

            // target was hit
            if (getBestResult() == 0) {
                stop();
            }
            play = false;
            setShotCounter(getShotCounter() + 1);
            double derivativeX = derivativeX(velocity, stepSize) * learningRate;
            double derivativeY = derivativeY(velocity, stepSize) * learningRate;

            Vector2D testVelocity = new Vector2D(
                    velocity.getX() - derivativeX,
                    velocity.getY() - derivativeY
            ).scaleDown(5);

            testVelocities.add(0, testVelocity);
            double testResult = new TestShot(testVelocity, getTargetPosition(), Heuristics.finalPosition).getTestResult();

            // target was hit
            if (testResult == 0) {
                setBestVelocity(testVelocity);
                setBestResult(testResult);
                stop();
            }
            // better velocity was found
            else if (testResult < getBestResult()) {
                velocity = testVelocities.get(0);
                setBestResult(testResult);
                setBestVelocity(velocity);
                play = true;
            }
            // local minimum - step back
            if (getBestResult() != 0 && testResult > getBestResult() && learningRate > Math.pow(10, power)) {
                testVelocities.remove(0);
                velocity = testVelocities.get(0);
                stepSize *= 0.5;
                learningRate *= 0.5;
                play = true;
            }

            setBestVelocity(getBestVelocity().scaleDown(5));
        }
        setBestResult(new TestShot(getBestVelocity(), this.getTargetPosition(),Heuristics.finalPosition).getTestResult());

        stop();
    }

    /**
     * @param velocity to shoot the ball with
     * @param step difference between adjacent velocities
     * @return derivativeX of position of the ball
     */
    public double derivativeX(Vector2D velocity , double step){
        Vector2D position = this.universe.getBall().getPosition();
        Vector2D velocityPlus = velocity.add(step , 0 );
        Vector2D velocityMinus = velocity.add(-1 * step , 0 );
        double testShotMinus = this.universe.getSolver().calculateNext(position, velocityMinus, step)[0].getX();
        double testShotPlus = this.universe.getSolver().calculateNext(position, velocityPlus, step)[0].getX();
        return (testShotPlus - testShotMinus) / ( 2 * step) ;
    }

    /**
     * @param velocity to shoot the ball with
     * @param step difference between adjacent velocities
     * @return derivativeY of position of the ball
     */
    public double derivativeY(Vector2D velocity, double step){
        Vector2D position = this.universe.getBall().getPosition();

        Vector2D velocityPlus = velocity.add(0 , step );
        Vector2D velocityMinus = velocity.add( 0 , -1.d * step );
        double testShotMinus = this.universe.getSolver().calculateNext(position,velocityMinus,step)[0].getX();
        double testShotPlus = this.universe.getSolver().calculateNext(position,velocityPlus,step)[0].getX();

        return (testShotPlus - testShotMinus) / ( 2 * step) ;
    }

}