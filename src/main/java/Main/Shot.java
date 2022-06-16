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

    // used to inform other users of the Thread that the action is finished
    private CountDownLatch latch = null;

    public static int SPEED = 60; // speed for the ball animation

    private final Universe universe = Main.getUniverse();
    private final Ball ball;
    public boolean running;
    private Thread thread;


    public Shot(Vector2D velocity) {
        this.ball = universe.getBall();
        this.ball.setVelocity(velocity);

        if (velocity.getMagnitude() > 5) {
            Vector2D unit_vector = velocity.getUnitVector();
            this.ball.setVelocity(new Vector2D(unit_vector.getX() * 5, unit_vector.getY() * 5)) ;
        }
        start();
    }

    public Shot(Vector2D velocity, CountDownLatch latch) {
        this.latch = latch;
        this.ball = universe.getBall();
        this.ball.setVelocity(velocity);

        if (velocity.getMagnitude() > 5) {
            Vector2D unit_vector = velocity.getUnitVector();
            this.ball.setVelocity(new Vector2D(unit_vector.getX() * 5, unit_vector.getY() * 5)) ;
        }
        start();
    }


    /**
     * starts the animation and creates a new Thread object
     */
    public synchronized void start() {

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
                    Display.pointCounter++;
                    ball.setVelocity(new Vector2D(0,0));
                    ball.setWillMove(false);
                    stop();
                }
                delta--;
            }
        }
        stop();
    }

}