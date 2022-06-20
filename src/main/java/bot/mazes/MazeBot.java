package bot.mazes;

import Main.Main;
import Main.Shot;
import bot.*;
import graphics.Display;
import objects.Terrain;
import physics.Vector2D;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;


/**
 * Bot that finds the path in the maze using Graph object
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

        // while target has not been hit
        while (!universe.getBall().isOnTarget(universe.getTarget())) {

            int endIndex = Math.min(path.size() - 1, 10);
            while (evaluate(endIndex)) {
                endIndex++;
            }
            setShotCounter(getShotCounter() + 1);

            // update the path to the target after shooting the ball
            this.path = findPath();
        }

        stop();

    }

    /**
     * @param index of the cell from the path list
     * @return true if it's possible to make shot to the cell at given index
     */
    public boolean evaluate(int index) {

        // how close the ball has to reach the target
        double TOLERANCE = 0.75;

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

            // for curved terrain choose on of the advanced bot: the best one: hillClimbing
            else {
                bot = new HillClimbingBot();
                bot.setTestNumber(1500);
            }
            bot.setShootBall(false);
            bot.setTargetPosition(temp);
            bot.setBotLatch(botLatch);
            bot.start();

            try {
                // wait for the response from the Thread
                botLatch.await();
            }
            catch (InterruptedException ignored) {}

            // make final shot more accurate: lower the tolerance
            if (cell.getNodeDescription().equals(NodeDescription.target)){
                TOLERANCE = Main.getUniverse().getTarget().getCylinder().getRadius() ;
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
                }
                catch (InterruptedException ignored) {}

                // update panel from Display class
                Display.shotCounter++;
                Display.updatePanel();

                // not possible to reach cell at the given index
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
        if (graph.getStartingCell() == null){
            graph.setStartingCell(graph.recalculateStartingCell());
        }

        // find neighbours for each cell
        graph.connectNeighbors();

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
                [Terrain.TERRAIN_WIDTH + (int) getTargetPosition().getX()]
                [Terrain.TERRAIN_WIDTH + (int) getTargetPosition().getY()];
        path.add(targetPosition);
        return path;
    }

    /**
     * @return list of positions that lead to the target
     */
    public ArrayList<Cell> getPath() {
        return path;
    }


}
