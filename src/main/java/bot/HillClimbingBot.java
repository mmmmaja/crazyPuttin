package bot;

import Main.Shot;
import physics.Vector2D;

import java.util.concurrent.CountDownLatch;


/**
 * iterative algorithm: at each iteration we change the velocity and asses the change by the fitness value
 */
public class HillClimbingBot extends Bot {


    public HillClimbingBot() {
        this.testNumber = 50;
        this.name = "HillClimbingBot";
    }

    @Override
    public void run() {

        double step = 0.01;

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

        this.bestVelocity = bot.getBestVelocity();
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
                double testResult = new TestShot(testVelocity, this.targetPosition,Heuristics.finalPosition).getTestResult();
                System.out.println(testResult);
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

}