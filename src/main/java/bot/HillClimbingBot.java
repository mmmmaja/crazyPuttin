package bot;

import Main.Shot;
import physics.Vector2D;

import java.util.concurrent.CountDownLatch;


/**
 * iterative algorithm: at each iteration we change the velocity and asses the change by the fitness value
 */
public class HillClimbingBot extends Bot {

    private int testNumber = 200;


    public HillClimbingBot() {
        this.targetPosition = universe.getTarget().getPosition();
        start();
    }

    /**
     * extended to fit AStarBot properties (specify the target position)
     */
    public HillClimbingBot(Vector2D targetPosition) {
        this.targetPosition = targetPosition;
        start();
    }

    public HillClimbingBot(boolean shootBall) {
        this.shootBall = shootBall;
        start();
    }

    public HillClimbingBot(boolean shootBall, Vector2D targetPosition) {
        this.targetPosition = targetPosition;
        this.shootBall = shootBall;
        start();
    }

    public HillClimbingBot(boolean b, Vector2D temp, CountDownLatch botLatch) {
        this.targetPosition = temp;
        this.shootBall = b;
        this.botLatch = botLatch;
        start();
    }

    public HillClimbingBot(int testNumber, boolean b, Vector2D temp, CountDownLatch botLatch) {
        this.targetPosition = temp;
        this.testNumber = testNumber;
        this.shootBall = b;
        this.botLatch = botLatch;
        start();
    }

    @Override
    public void run() {

        double step = 0.1;

        CountDownLatch improvedBotLatch = new CountDownLatch(1);
        Bot bot = new ImprovedRandomBot(this.testNumber, false, this.targetPosition, improvedBotLatch);
        try {
            // wait for the response from the Thread
            improvedBotLatch.await();
        } catch (InterruptedException ignored) {}

        Vector2D direction =  bot.getBestVelocity();
        this.bestVelocity = direction;
        this.bestResult = new TestShot(this.bestVelocity, this.targetPosition, Heuristics.finalPosition).getTestResult();
        double[][] stepArray = {
                {step, -step},
                {-step, +step},
                {step, +step},
                {-step, -step},
                {step, 0},
                {-step, 0},
                {0, step},
                {0, -step},
        };
        boolean play = true;
        while (play && running) {
            play = false;
            this.shotCounter++;
            for (double[] stepCase : stepArray) {

                Vector2D testVelocity = new Vector2D(this.bestVelocity.getX() + stepCase[0], this.bestVelocity.getY() + stepCase[1]);
                double testResult = new TestShot(testVelocity, this.targetPosition).getTestResult();

                // target was reached: break all
                if (testResult == 0) {
                    this.bestVelocity = testVelocity;
                    this.bestResult = testResult;
                    play = false;
                    stop();
                }
                // climb the hill
                else if (testResult <= this.bestResult) {
                    this.bestVelocity = testVelocity;
                    this.bestResult = testResult;
                    play = true;
                }
            }
        }
        stop();
    }


    @Override
    public void shootBall() {
        new Shot(this.bestVelocity);
        System.out.println(this);
    }
}