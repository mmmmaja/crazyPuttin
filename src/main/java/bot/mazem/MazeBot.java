package bot.mazem;

import bot.Bot;
import bot.Heuristics;

import java.util.ArrayList;

public class MazeBot extends Bot {

    // use constructor with heuristics! for the testShot

    private Graph graph ;
    private ArrayList<Cell> path = new ArrayList<>();
    private ArrayList<Cell> visited = new ArrayList<>();
    private ArrayList<Cell> toVisit = new ArrayList<>() ;
    private Cell start ;
    private Cell target;
    private Cell currentCell ;
    public MazeBot(){
        this.setHeuristics(Heuristics.finalPosition);
    }


    public ArrayList<Cell> findPath(){
        graph = new Graph();
        graph.createGraph();
        graph.connectNeighbors();
        start = graph.getStartingCell();
        target= graph.getTargetCell();
        currentCell = start ;
//        graph.getStartingCell().setVisited(true);
//        visited.add(start);
//        toVisit.addAll(start.neighbors);
        toVisit.add(start);
        //FIXME NodeDescriptions.OBSTACLE
        while( !toVisit.isEmpty() && !isTarget(currentCell)){
            int indexOfWinner = 0 ;
            for (int i = 0 ; i < toVisit.size() ; i++) {
                if( toVisit.get(i).getTotalCost() < toVisit.get(indexOfWinner).getTotalCost()  )

                    indexOfWinner = i ;
            }
            Cell winner = toVisit.get(indexOfWinner) ;
            System.out.println(winner.getX() + " // " + winner.getY());
            currentCell= winner;
            if( winner.equals(target) ) break;

            winner.setVisited(true);
            visited.add(winner);
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
        System.out.println(currentCell.getNodeDescription());
        while(currentCell.getPrevious() != null){
            path.add(0,currentCell.getPrevious() );
            currentCell = currentCell.getPrevious();

        }
        return this.path;
    }
    private boolean isTarget( Cell cell ){
        return cell.equals(target);
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public ArrayList<Cell> getPath() {
        return path;
    }

    public void setPath(ArrayList<Cell> path) {
        this.path = path;
    }

    public ArrayList<Cell> getVisited() {
        return visited;
    }

    public void setVisited(ArrayList<Cell> visited) {
        this.visited = visited;
    }

    public ArrayList<Cell> getToVisit() {
        return toVisit;
    }

    public void setToVisit(ArrayList<Cell> toVisit) {
        this.toVisit = toVisit;
    }

    public Cell getStart() {
        return start;
    }

    public void setStart(Cell start) {
        this.start = start;
    }

    public Cell getTarget() {
        return target;
    }

    public void setTarget(Cell target) {
        this.target = target;
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }

    @Override
    public void run() {

    }

    public static void main(String[] args) {
        MazeBot mazeBot = new MazeBot();
        mazeBot.findPath();

    }
}
