package bot.maze;

import Main.Main;
import Main.Shot;
import bot.*;
import physics.Vector2D;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;


public class MazeBot extends Bot {

    // list of positions that will lead to the target
    private final ArrayList<Cell> path;

    public MazeBot() {
        this.path = findPath();
        start();
    }

    @Override
    public void run() {

        Vector2D velocity = new Vector2D();

        for (int i = 0; i<path.size(); i++) {

            Cell cell = path.get(i);
            Vector2D temp = new Vector2D(cell.getX(), cell.getY());

            // choose a bot depending on the equation of the terrain
            Bot bot;
            CountDownLatch botLatch = new CountDownLatch(1);
            String heightFunction = Main.getUniverse().getFileReader().getHeightFunction();
            if (heightFunction.equals("0")) {
                bot = new RuleBasedBot(false, temp, botLatch);
            }
            else {
                bot = new HillClimbingBot(720,false, temp, botLatch);
            }
            try {
                // wait for the response from the Thread
                botLatch.await();
            } catch (InterruptedException ignored) {}
            double TOLERANCE = 0.25;
            if(cell.getNodeDescription().equals(NodeDescription.target)){
                TOLERANCE = 0.1;
            }
            // target was hit!
            if (bot.getBestResult() < TOLERANCE) {
                velocity = bot.getBestVelocity();
            }

            // not possible to reach this point, shoot the ball to the previous point
            else {
                // latch is used to wait for the Thread from the Shot class to stop before going further in this class
                CountDownLatch latch = new CountDownLatch(1);
                new Shot(velocity, latch);
                try {
                    // wait for the response from the Thread
                    latch.await();
                } catch (InterruptedException ignored) {}
                i--;
            }
        }
        CountDownLatch latch = new CountDownLatch(1);
        new Shot(velocity, latch);
        try {
            latch.await();
        } catch (InterruptedException ignored) {}
        stop();
    }



    /**
     * @return list of Cells on the way from the ball to target
     */
    public ArrayList<Cell> findPath(){

        ArrayList<Cell> path = new ArrayList<>();
        ArrayList<Cell> toVisit = new ArrayList<>() ;

        Graph graph = new Graph();
        graph.connectNeighbors();

        Cell start = graph.getStartingCell();
        Cell target= graph.getTargetCell();

        Cell currentCell = start ;
        toVisit.add(start);

        while( !toVisit.isEmpty() && !currentCell.equals(target)) {
            int indexOfWinner = 0 ;
            for (int i = 0 ; i < toVisit.size() ; i++) {
                if( toVisit.get(i).getTotalCost() < toVisit.get(indexOfWinner).getTotalCost()  )

                    indexOfWinner = i ;
            }
            Cell winner = toVisit.get(indexOfWinner) ;
            currentCell= winner;
            if ( winner.equals(target) ) {
                break;
            }

            winner.setVisited(true);
            toVisit.remove(winner);

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

        while (currentCell.getPrevious() != null) {
            path.add(0, currentCell.getPrevious() );
            currentCell = currentCell.getPrevious();
        }

        path.remove(0);
        Cell targetPosition = new Cell(
                (int) this.targetPosition.getX(),
                (int) this.targetPosition.getY()
        );
        path.add(targetPosition);
        return path;
    }

}
