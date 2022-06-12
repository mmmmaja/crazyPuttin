package bot;

import Main.Main;
import Main.Shot;
import Main.Universe;
import objects.FileReader;
import physics.Vector2D;
import java.util.Random;


/**
 * template for the bots, using Threads when simulating shots
 */
public abstract class Bot implements Runnable {

    final Universe universe = Main.getUniverse();

    public Vector2D targetPosition = universe.getTarget().getPosition();
    boolean shootBall = true; // if shot is just simulation do not shoot the ball

    String name; // name to be set for each bot type
    Vector2D bestVelocity = new Vector2D(0, 0); // velocity that gives the closest position to the target
    double bestResult = Integer.MAX_VALUE; // distance between the target and the ball
    int shotCounter = 0; // number of simulations run

    protected long start; // start of the simulations
    public boolean running;
    public Thread thread;

    public Heuristics heuristics = Heuristics.allPositions;


    /**
     * starts the new Thread that runs the shot simulations
     */
    public synchronized void start() {
        this.start = System.nanoTime(); // used to measure the performance of the bot
        this.running = true;
        this.thread = new Thread(this);
        this.thread.start();
    }

    /**
     * kill the thread and shoot the ball with best velocity
     */
    public synchronized void stop() {
//        System.out.println(this);

        this.running = false;
        if (this.shootBall) {
            shootBall(); // show an animation and update the position of the Ball object
        }
        try {
            this.thread.join();
        }
        catch (InterruptedException ignored) {}
    }

    /**
     * run shot simulations and update bestVelocity
     */
    @Override
    public abstract void run();

    /**
     * shoot the ball creating new Shot() object with bestVelocity
     */
    public void shootBall() {
        new Shot(this.bestVelocity);
    }

    /**
     * @return number of all bot simulations
     */
    public int getShotCounter() {
        return this.shotCounter;
    }


    public String toString() {
        return name + ": " +
                "\nBest velocity: " + this.bestVelocity +
                "\nresult: " + this.bestResult +
                "\nshotCounter: " + this.shotCounter;
    }

    /**
     * @return final velocity to shoot the ball
     */
    public Vector2D getBestVelocity() {
        return this.bestVelocity;
    }

    /**
     * @return distance from the ball to the target when shot with bestVelocity
     */
    public double getBestResult() {
        return bestResult;
    }

    /**
     * @return time in seconds needed for simulation
     */
    public double getTime() {
        return (System.nanoTime() - this.start) * Math.pow(10, -9) ;
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
            rand = getRandomDoubleBetween(a1,a2);
        }
        else {
            rand = getRandomDoubleBetween(b1,b2);
        }
        return rand;
    }

    public void setHeuristics(Heuristics heuristics) {
        this.heuristics = heuristics;
    }

    public Heuristics getHeuristics() {
        return heuristics;
    }
}
