package Main;

import graphics.Display;
import objects.Ball;
import objects.TerrainGenerator;
import physics.Vector2D;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;


/**
 * instance of this class is created each time the movement of the ball is triggered
 * performs an animation of the ball movement in the new thread
 * updates the position of the ball in the Universe!
 */
public class Shot extends Display implements Runnable {

    private final Universe universe = Main.getUniverse();

    // used to inform other users of the Thread that the action is finished
    private CountDownLatch latch = null;

    // speed for the ball animation
    public static int SPEED = 40;

    // ball object from the Universe class
    private final Ball ball;

    // if thread is running or not
    public boolean running;

    // thread on which all the computations are executed
    private Thread thread;

    // velocity to shoot the ball with
    private Vector2D velocity;

    /**
     * @param velocity to shoot the ball with
     */
    public Shot(Vector2D velocity) {
        this.ball = universe.getBall();
        this.velocity = velocity;
    }


    /**
     * starts the animation and creates a new Thread object
     */
    public synchronized void start() {

        if (this.velocity.getMagnitude() > 5) {
            Vector2D unit_vector = velocity.getUnitVector();
            this.ball.setVelocity(new Vector2D(unit_vector.getX() * 5, unit_vector.getY() * 5)) ;
        }
        else {
            this.ball.setVelocity(this.velocity);
        }

        running = true;
        this.thread = new Thread(this);
        this.thread.start();
    }

    /**
     * kills the thread when the ball is in the resting position
     */
    public synchronized void stop() {

        Display.updatePanel();

        this.ball.setWillMove(false);
        this.running = false;

        if (this.latch != null) {
            latch.countDown();
        }
        try {
            this.thread.join();
        }
        catch (InterruptedException ignored) {}
    }


    @Override
    public void run() {

        this.ball.setWillMove(true);

        double delta = 0;
        long lastTime = System.nanoTime();
        final double nanos = Math.pow(10, 9) / SPEED ;
        // number of nanoseconds between each update: SPEED times per second

        while (running) {

            long now = System.nanoTime();
            delta+= (now - lastTime) / nanos;
            lastTime = now;
            while (delta >= 1) {
                universe.getSolver().nextStep(ball);
                universe.updateBallPosition();

                // when ball is in the resting position
                if ((!ball.isMoving() && !ball.getWillMove()) ) {
                    stop();
                }

                // target was hit
                if(ball.isOnTarget(universe.getTarget())) {
                    // Display.pointCounter++;
                    ball.setVelocity(new Vector2D(0,0));
                    ball.setWillMove(false);
                    stop();
                }
                delta--;
            }
        }
        stop();
    }

    /**
     * @param latch to inform other object if the thread is finished
     */
    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }
}