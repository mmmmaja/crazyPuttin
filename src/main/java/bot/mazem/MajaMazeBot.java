package bot.mazem;

import Main.Shot;
import bot.*;
import physics.Vector2D;

import java.util.ArrayList;
import java.util.Random;

public class MajaMazeBot extends Bot {

    double tolerance = 0.1;
    private final ArrayList<Cell> path;


    public MajaMazeBot() {
        this.path = findPath();
        start();
    }

    @Override
    public void run() {
        Vector2D velocity = new Vector2D();

        for (int i = 1; i < path.size(); i++) {

            Cell cell = path.get(i);
            Vector2D temp = new Vector2D(cell.getX(), cell.getY());
            ImprovedRuleBasedBot bot = new ImprovedRuleBasedBot(false, temp);
            System.out.println("result: "+bot.getBestResult());

            // target was hit!
            if (bot.getBestResult() < tolerance) {
                System.out.println("hit!");
                velocity = bot.getBestVelocity();


            }
            else {
                System.out.println("shot");
                new Shot(velocity);
            }
        }
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
            if( winner.equals(target) ) {
                break;
            }

            winner.setVisited(true);
            toVisit.remove(winner);

            for (Cell neighbor : winner.getNeighbors()) {
                if( !neighbor.isVisited() &&
                        ((neighbor.getNodeDescription() != NodeDescription.water) && (neighbor.getNodeDescription() != NodeDescription.obstacle))){
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
        while(currentCell.getPrevious() != null){
            path.add(0,currentCell.getPrevious() );
            currentCell = currentCell.getPrevious();

        }
        return path;
    }
}
