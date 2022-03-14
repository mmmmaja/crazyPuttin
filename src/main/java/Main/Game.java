package Main;

//import graphics.Display;
import graphics.Display;
import graphics.SmartGroup;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import objects.FileReader;
import objects.TerrainGenerator;
import physics.Vector2D;

public class Game implements Runnable {

    private Universe universe;
    private boolean running;
    private Thread thread;
    public static final int FRAME_WIDTH = 1100;
    public static final int FRAME_HEIGHT = 600;

    // translate all the objects to the middle of the frame (used just for the display)
    public static final int translateX = FRAME_WIDTH / 3;
    public static final int translateY =  FRAME_HEIGHT / 3;

    public Game( Universe universe ) {
        FileReader fileReader = new FileReader();
        this.universe = new Universe(fileReader);
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
