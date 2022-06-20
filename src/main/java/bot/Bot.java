package bot;

import Main.Main;
import Main.Shot;
import Main.Universe;
import objects.FileReader;
import physics.Vector2D;
import java.util.Random;
import java.util.concurrent.CountDownLatch;


/**
 * Abstract class for all the bots, uses Threads when computing the velocities
 */
public abstract class Bot implements Runnable {

    public final Universe universe = Main.getUniverse();

    // use to track if the Thread is still running or not
    private CountDownLatch botLatch;

    // number of simulations to run
    private int testNumber;

    // where to shoot the ball (could be Target or cell on the path to target)
    private Vector2D targetPosition = universe.getTarget().getPosition();

    // if shot is just simulation do not shoot the ball
    private boolean shootBall = true;

    // name to be set for each bot type
    private String name;

    // velocity that gives the closest position to the target
    private Vector2D bestVelocity = new Vector2D();

    // closest distance between the target and the ball
    private double bestResult = universe.getBall().getPosition().getEuclideanDistance(targetPosition);

    // number of simulations run
    private int shotCounter = 0;

    // time indicating start of the simulations
    private long start;

    // if thread is running or not
    private boolean running;

    // thread on which all the computations are executed
    private Thread thread;


    /**
     * starts the new Thread that runs the shot simulations
     */
    public synchronized void start() {

        this.start = System.nanoTime();
        this.running = true;
        this.thread = new Thread(this);
        this.thread.start();

    }

    /**
     * kill the thread and shoot the ball with best velocity
     */
    public synchronized void stop() {

        this.running = false;

        if (this.shootBall) {
            // show an animation and update the position of the Ball object
            shootBall();
        }
        // inform the owner of the botLatch of the termination of the Thread
        if (this.botLatch != null) {
            botLatch.countDown();
        }
        try {
            this.thread.join();
        }
        catch (InterruptedException ignored) {}
    }

    /**
     * run shot simulations and update bestVelocity
     * to be implemented for each bot
     */
    @Override
    public abstract void run();

    /**
     * shoot the ball creating new Shot() object with bestVelocity
     */
    public void shootBall() {
        new Shot(this.bestVelocity).start();
    }

    /**
     * @return number of all bot simulations
     */
    public int getShotCounter() {
        return this.shotCounter;
    }


    public String toString() {
        return name + ": " +
                "\nBest velocity: " + this.bestVelocity +  " " + "Magnitude: " + this.bestVelocity.getMagnitude() +
                "\nresult: " + this.bestResult +
                "\nshotCounter: " + this.shotCounter;
    }


    /**
     * @return random Double between minimum and maximum value
     */
    public double getRandomDoubleBetween(double minimum, double maximum) {
        Random random = new Random();
        return random.nextDouble() * (maximum - minimum) + minimum;
    }

    /**
     * @return random Double between in either range [a1, a2] or [b1, b2]
     */
    public double getRandomWithinTwoRanges(double a1, double a2, double b1, double b2) {
        Random random = new Random();
        double r = random.nextDouble();
        double rand;
        if (r < 0.5) {
            rand = getRandomDoubleBetween(a1 , a2);
        }
        else {
            rand = getRandomDoubleBetween(b1 , b2);
        }
        return rand;
    }

    /**
     * @param name of the bot that extends this class
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param botLatch to inform other object if the thread is finished
     */
    public void setBotLatch(CountDownLatch botLatch) {
        this.botLatch = botLatch;
    }

    /**
     * @param targetPosition where to shoot the ball (could be Target or cell on the path to target)
     */
    public void setTargetPosition(Vector2D targetPosition) {
        this.targetPosition = targetPosition;
    }

    /**
     * @return where to shoot the ball (could be Target or cell on the path to target)
     */
    public Vector2D getTargetPosition() {
        return this.targetPosition;
    }

    /**
     * @return true if we have to trigger the animation for shooting the ball and update the Universe
     */
    public boolean getShootBall() {
        return this.shootBall;
    }

    /**
     * @param shootBall  if we have to trigger the animation for shooting the ball and update the Universe
     */
    public void setShootBall(boolean shootBall) {
        this.shootBall = shootBall;
    }

    /**
     * @param testNumber number of simulations to run
     */
    public void setTestNumber(int testNumber) {
        this.testNumber = testNumber;
    }

    /**
     * @return number of simulations to run
     */
    public int getTestNumber() {
        return this.testNumber;
    }

    /**
     * @return velocity that gives the closest position to the target
     */
    public Vector2D getBestVelocity() {
        return this.bestVelocity;
    }

    /**
     * @param bestVelocity velocity that gives the closest position to the target
     */
    public void setBestVelocity(Vector2D bestVelocity) {
        this.bestVelocity = bestVelocity;
    }

    /**
     * @return closest distance between the target and the ball
     */
    public double getBestResult() {
        return bestResult;
    }

    /**
     * @param bestResult closest distance between the target and the ball
     */
    public void setBestResult(double bestResult) {
        this.bestResult = bestResult;
    }

    /**
     * @return time in seconds needed for simulation
     */
    public double getTime() {
        return (System.nanoTime() - this.start) * Math.pow(10, -9) ;
    }

    /**
     * @param shotCounter number of simulations run
     */
    public void setShotCounter(int shotCounter) {
        this.shotCounter = shotCounter;
    }

    /**
     * @return if the thread of this class is still running
     */
    public boolean isRunning() {
        return running;
    }
}
