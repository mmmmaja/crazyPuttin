package tests;

import Main.Main;
import Main.Universe;
import bot.Bot;
import bot.mazes.MazeBot;
import bot.mazes.RecursiveMaze;
import javafx.scene.shape.Sphere;
import objects.FileReader;
import objects.Obstacle;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * works just for universe not UI
 *
 * number of shots
 * time
 */
public class MazeExperiments {

    Universe universe = Main.getUniverse();

    // generate maze with one level n times
    int terrainGenerationCounter = 3;

    public MazeExperiments() {
        testLevel(1);
    }


    public void testLevel(int level) {

        double averageShotCounter = 0;
        double averageTimeMeasured = 0;

        for (int i = 0; i < terrainGenerationCounter; i++) {
            //System.out.println("iteration: " + i);

            createMaze(level);

            int shotCounter = 0;
            double timeTook = 0;


            while (!universe.getBall().isOnTarget(universe.getTarget())) {
                CountDownLatch botLatch = new CountDownLatch(1);
                Bot bot = new MazeBot();
                bot.setBotLatch(botLatch);
                bot.start();
                try {
                    // wait for the response from the Thread
                    botLatch.await();
                } catch (InterruptedException ignored) {}
                shotCounter++;
                timeTook+= bot.getTime();
            }

            clearMaze();
//            System.out.println("time: " + timeTook);
//            System.out.println("shot counter: " + shotCounter);

            averageShotCounter+= shotCounter;
            averageTimeMeasured+= timeTook;

            System.out.println();
        }

        averageShotCounter/= terrainGenerationCounter;
        averageTimeMeasured/= terrainGenerationCounter;

        System.out.println("shots: " + averageShotCounter);
        System.out.println("time: " + averageTimeMeasured);

    }


    private void createMaze(int level) {
        RecursiveMaze.step = 8 - level + 1;
        RecursiveMaze recursiveMaze = new RecursiveMaze();
        ArrayList<Obstacle> obstacles = recursiveMaze.getObstacles();
        for (Obstacle obstacle : obstacles) {
            universe.addObstacle(obstacle);
        }
    }

    private void clearMaze() {
        universe.deleteObstacles();
        universe.deletePathVisualizations();
    }

}
