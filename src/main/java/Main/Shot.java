package Main;

import graphics.Display;
import objects.Ball;
import objects.TerrainGenerator;
import physics.Vector2D;


/**
 * instance of this class is created each time the movement of the ball is triggered
 * performs an animation of the ball movement in the new thread
 */
public class Shot extends Display implements Runnable {

    private final Ball ball;
    private final Universe universe;
    private boolean running;
    private Thread thread;


    public Shot(Universe universe, Vector2D velocity) {
        this.universe = universe;
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
        Display.updatePanel(ball.getPosition().getX(), ball.getPosition().getY());
        this.ball.setWillMove(false);
        this.running = false;
        try {
            this.thread.join();
        }
        catch (InterruptedException ignored) {}
    }


    @Override
    public void run() {
        ball.setWillMove(true);

        int SPEED = 60;
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