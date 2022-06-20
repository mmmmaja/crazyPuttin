package bot.mazes;

import Main.Main;
import Main.Shot;
import bot.*;
import graphics.Display;
import graphics.SmartGroup;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import objects.Terrain;
import objects.TerrainGenerator;
import physics.Vector2D;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Bot that find the path in the maze
 */
public class MazeBot extends Bot {


    // list of positions that will lead to the target
    // is updated within the game loop
    private ArrayList<Cell> path;


    public MazeBot() {
        this.path = findPath();
    }


    @Override
    public void run() {

        int index = 0;
        while (!universe.getBall().isOnTarget(universe.getTarget())) {
            setShotCounter(getShotCounter()+1);
            int endIndex = Math.min(path.size() - 1, index + 20);
            while (evaluate(endIndex)) {
                endIndex++;
                index++;
            }
            index--;
        }
        test();
        stop();

    }

    /**
     * @param index of the cell from the path list
     * @return true if it's possible to make shot to the cell at given index
     */
    public boolean evaluate(int index) {

        // how close the ball has to reach the target
        double TOLERANCE = 0.5;

        Vector2D velocity;

        for (int i = index; i >= 0; i--) {

            Cell cell = path.get(i);
            Vector2D temp = new Vector2D(cell.getX(), cell.getY());

            // choose a bot depending on the equation of the terrain
            Bot bot;
            CountDownLatch botLatch = new CountDownLatch(1);
            String heightFunction = Main.getUniverse().getFileReader().getHeightFunction();

            // for flat surface use Rule Based bot: no simulations
            if (!heightFunction.contains("x") && !heightFunction.contains("y")) {
                bot = new RuleBasedBot();
                TOLERANCE = Main.getUniverse().getTarget().getCylinder().getRadius();
            }

            // for curved terrain choose on of the advanced bot
            else {
                bot = new HillClimbingBot();
                bot.setTestNumber(500);
            }
            bot.setShootBall(false);
            bot.setTargetPosition(temp);
            bot.setBotLatch(botLatch);
            bot.start();

            try {
                // wait for the response from the Thread
                botLatch.await();
            } catch (InterruptedException ignored) {}

            // make final shot more accurate: lower the tolerance
            if (cell.getNodeDescription().equals(NodeDescription.target)){
                TOLERANCE = Main.getUniverse().getTarget().getCylinder().getRadius() / 2.d;
            }

            // target was hit
            if (bot.getBestResult() < TOLERANCE) {
                if (i == index && (index != path.size() - 1)) {
                    return true;
                }
                velocity = bot.getBestVelocity();

                // latch is used to wait for the Thread from the Shot class to stop before going further in this class
                CountDownLatch latch = new CountDownLatch(1);

                // shoot the ball with bestVelocity
                Shot shot = new Shot(velocity);
                shot.setLatch(latch);
                shot.start();

                try {
                    // wait for the response from the Thread
                    latch.await();
                } catch (InterruptedException ignored) {}

                // update panel from Display class
                Display.shotCounter++;
                Display.updatePanel();
                return false;
            }
        }
        return false ;
    }



    /**
     * @return list of Cells on the way from the ball to target
     * Uses A* algorithm to find the path
     */
    public ArrayList<Cell> findPath(){

        // path leading from starting cell to the target cell
        ArrayList<Cell> path = new ArrayList<>();

        // list of cells left to still be visited
        ArrayList<Cell> toVisit = new ArrayList<>() ;

        Graph graph = new Graph();

        // find neighbours for each cell
        graph.connectNeighbors();
        if(graph.getStartingCell() == null){
            System.out.println("No starting cell");
            return null ;
        }
        Cell start = graph.getStartingCell();
        Cell target = graph.getTargetCell();

        Cell currentCell = start ;
        toVisit.add(start);

        while ( !toVisit.isEmpty() && !currentCell.equals(target)) {

            // find index of the closest cell
            int indexOfWinner = 0 ;
            for (int i = 0 ; i < toVisit.size() ; i++) {
                if ( toVisit.get(i).getTotalCost() < toVisit.get(indexOfWinner).getTotalCost()  )
                    indexOfWinner = i ;
            }

            Cell winner = toVisit.get(indexOfWinner) ;
            currentCell= winner;

            // target was found
            if ( winner.equals(target) ) {
                break;
            }

            winner.setVisited(true);
            toVisit.remove(winner);

            // find the best neighbour and continue looping
            for (Cell neighbor : winner.getNeighbors()) {
                if (
                        !neighbor.isVisited() &&
                        ((neighbor.getNodeDescription() != NodeDescription.water) && (neighbor.getNodeDescription() != NodeDescription.obstacle))
                )
                {
                    double tempScore = currentCell.getCostFromStart() + currentCell.distanceTo(neighbor);
                    neighbor.setCostFromStart( tempScore );
                    if (!toVisit.contains(neighbor) || (tempScore <= neighbor.getTotalCost())) {
                        toVisit.add(neighbor);
                    }
                    neighbor.setPrevious(currentCell);

                }
                neighbor.setVisited(true);
            }
        }

        // loop back and find entire path
        while (currentCell.getPrevious() != null) {
            path.add(0, currentCell.getPrevious() );
            currentCell = currentCell.getPrevious();
        }

        // do not include cell with the ball
        path.remove(0);

        Cell targetPosition = graph.getGraphMatrix()
                [Terrain.TERRAIN_WIDTH+ (int) getTargetPosition().getX()]
                [Terrain.TERRAIN_WIDTH+ (int) getTargetPosition().getY()];
        path.add(targetPosition);
        return path;
    }

    /**
     * @return list of positions that lead to the target
     */
    public ArrayList<Cell> getPath() {
        return path;
    }


    private void test() {
        System.out.println("mazeBot");
        System.out.println("time: " + getTime());
        System.out.println("shotCounter: " + getShotCounter());
    }


}
