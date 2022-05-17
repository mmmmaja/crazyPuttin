package bot;

import Main.Main;
import Main.Shot;
import Main.Universe;
import physics.Vector2D;


/**
 * template for the bots, using Threads to speed everything up (??)
 */
public abstract class Bot implements Runnable{

    String name = "bot";
    final Universe universe = Main.getUniverse();
    final Heuristics heuristics = Heuristics.allPositions;
    Vector2D targetPosition;
    boolean shootBall = true;

    Vector2D bestVelocity = new Vector2D(0, 0);
    double bestResult = Integer.MAX_VALUE;
    int shotCounter = 0;

    boolean running;
    Thread thread;


    /**
     * start the new Thread that will run the shot simulations
     */
    public synchronized void start() {
        System.out.println("start");
        running = true;
        this.thread = new Thread(this);
        this.thread.start();
    }

    /**
     * kill the thread and shoot the ball with best velocity
     */
    public synchronized void stop() {
        System.out.println("stop");
        if (this.shootBall) {
            shootBall();
        }
        this.running = false;
        notify();
        System.out.println(this);
        try {
            this.thread.join();
        }
        catch (InterruptedException ignored) {}
    }

    /**
     * run shot simulations and update bestVelocity field
     */
    @Override
    public abstract void run();

    /**
     * shoot the ball creating new Shot object with bestVelocity
     */

    public void shootBall() {
        new Shot(this.universe, this.bestVelocity);
    }

    /**
     * @return number of all shot simulations
     */
    public int getShotCounter() {
        return this.shotCounter;
    }


    public String toString() {
        return name + ": "+
                "\nBest velocity: " + this.bestVelocity +
                "\nresult: " + this.bestResult +
                "\nshotCounter: " + this.shotCounter +
                "\nheuristics: " + this.heuristics+ "\n";
    };

    public Vector2D getBestVelocity() {
        return this.bestVelocity;
    }
}
