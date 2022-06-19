package bot;

import Main.Shot;
import physics.Vector2D;

import java.util.concurrent.CountDownLatch;


/**
 * iterative algorithm: at each iteration we change the velocity and asses the change by the fitness value
 */
public class HillClimbingBot extends Bot {


    public HillClimbingBot() {
        setTestNumber(50);
        setName("HillClimbingBot");
    }


    @Override
    public void run() {

        double step = 0.01;

        // find the starting velocity
        CountDownLatch improvedBotLatch = new CountDownLatch(1);
        ImprovedRandomBot bot = new ImprovedRandomBot();
        bot.setTestNumber(this.getTestNumber());
        bot.setShootBall(false);
        bot.setTargetPosition(this.getTargetPosition());
        bot.setBotLatch(improvedBotLatch);
        bot.start();
        try {
            // wait for the response from the Thread
            improvedBotLatch.await();
        } catch (InterruptedException ignored) {}

        setBestResult(new TestShot(getBestVelocity(), getTargetPosition(), Heuristics.finalPosition).getTestResult());

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
        while (play && isRunning()) {
            play = false;
            setShotCounter(getShotCounter() + 1);
            for (double[] stepCase : stepArray) {

                Vector2D testVelocity = new Vector2D(getBestVelocity().getX() + stepCase[0], getBestVelocity().getY() + stepCase[1]);
                double testResult = new TestShot(testVelocity, this.getTargetPosition(),Heuristics.finalPosition).getTestResult();
                // target was reached: break all
                if (testResult == 0) {
                    setBestVelocity(testVelocity);
                    setBestResult(testResult);
                    play = false;
                    stop();
                }
                // climb the hill
                else if (testResult <= getBestResult()) {
                    setBestVelocity(testVelocity);
                    setBestResult(testResult);
                    play = true;
                }
            }
        }
        stop();
    }

}