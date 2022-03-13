package Main;

import graphics.Display;
import javafx.application.Application;

public class Game implements Runnable {

    private Universe universe;
    private boolean running;
    private Thread thread;

    public Game(Universe universe) {
        this.universe = universe;
    }



    public synchronized void start() {
        running = true;
        this.thread = new Thread(this, "Graphics.Display");
        this.thread.start();
    }

    @Override
    public void run() {
        Application.launch(Display.class);

        double delta = 0;
        long lastTime = System.nanoTime();
        final double nanos = Math.pow(10, 9) / 60;
        // number of nanoseconds between each update: 60 times per second

        while (running) {
            long now = System.nanoTime();
            delta+= (now - lastTime) / nanos;
            lastTime = now;
            while (delta >= 1) {
                update();
                delta--;
            }
        }
        stop();
    }

    /**
     * calculate physics here
     */
    private void update() {

    }


    public synchronized void stop() {
        running = false;
        try {
            this.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
