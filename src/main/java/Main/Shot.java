package Main;

import objects.Ball;
import physics.Vector2D;


/**
 * creates a Thread when displaying the animation of the moving ball
 */
public class Shot implements Runnable {

    private final Ball ball;
    private final Universe universe;
    private boolean running = false;
    private Thread thread;

    public Shot(Universe universe, Vector2D velocity) {
        this.universe = universe;
        this.ball = universe.getBall();
        this.ball.setVelocity(velocity);

        if (velocity.getMagnitude() > universe.getMAX_SPEED()) {
            Vector2D unit_vector = new Vector2D(velocity.getX() / velocity.getMagnitude(), velocity.getY() / velocity.getMagnitude());
           this.ball.setVelocity( new Vector2D(unit_vector.getX() * universe.getMAX_SPEED(), unit_vector.getY() * universe.getMAX_SPEED()) ) ;
        }
        start();
    }


    public synchronized void start() {
        running = true;
        this.thread = new Thread(this);
        this.thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            this.thread.join();
        }
        catch (InterruptedException ignored) {}
    }


    @Override
    public void run() {

        double delta = 0;
        long lastTime = System.nanoTime();

        // number of nanoseconds between each update: SPEED times per second
        int SPEED = 900;
        final double nanos = Math.pow(10, 9) / SPEED;

        while (running) {

            long now = System.nanoTime();
            delta+= (now - lastTime) / nanos;
            lastTime = now;
            while (delta >= 1) {
                if (!ball.isMoving()) {
                    stop();
                }
                universe.nextStep(ball);
                universe.updateBallsPosition();
                delta--;
            }
        }
        stop();
    }

}
