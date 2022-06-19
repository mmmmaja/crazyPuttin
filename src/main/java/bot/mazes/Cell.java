package bot.mazes;

import physics.Vector2D;

import java.util.ArrayList;


public class Cell {

    // position on the terrain
    private int x, y;

    // the distance from the target's cell to the current cell
    private double costToTarget;

    // the cost of the movement from the start cell to the current cell
    private double costFromStart;

    // costFromStart + costToTarget
    private double totalCost;

    // list of all neighbours of this (including diagonal ones)
    private final ArrayList<Cell> neighbors = new ArrayList<>();

    // cell that leads to this
    private Cell previous;

    // type of the cell, see enum options
    private NodeDescription nodeDescription = NodeDescription.grass;

    // used for generating mazes, indicates if this cell will be a wall or not
    private boolean wall = true ;

    // used for A* algorithm
    private boolean visited = false ;

    // index of this cell in the matrix
    private Vector2D index ;


    Cell(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @param target ball of the target
     * @param ball position of the ball
     */
    public void setAllCosts(Vector2D target , Vector2D ball){
        this.costToTarget = distanceTo(target);
        this.costFromStart = distanceTo(ball);
        this.totalCost = this.costFromStart + this.costToTarget;
    }


    /**
     * @return distance from this to myCell
     */
    public double distanceTo(Cell myCell) {
        return distanceTo(myCell.x, myCell.y);
    }

    /**
     * @return from this to cell in the position of vector2D
     */
    public double distanceTo(Vector2D vector2D) {
        return vector2D.getEuclideanDistance(new Vector2D(this.x, this.y));
    }

    /**
     * @return from this to cell in the position (x, y)
     */
    public double distanceTo(double x, double y ) {
        return new Vector2D(x, y).getEuclideanDistance(new Vector2D(this.x, this.y));
    }

    /**
     * @param myCell cell adjacent to this
     */
    public void addNeighbors(Cell myCell){
        neighbors.add(myCell);
    }

    /**
     * @return array of all adjacent cells to this
     */
    public ArrayList<Cell> getNeighbors() {
        return neighbors;
    }

    /**
     * @return position x of this cell
     */
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return position y of this cell
     */
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return distance between position of the ball (start) to this cell
     */
    public double getCostFromStart() {
        return costFromStart;
    }

    public void setCostFromStart(double costFromStart) {
        this.costFromStart = costFromStart;
        this.totalCost = costFromStart + costToTarget;
    }

    /**
     * @return costFromStart + costToTarget
     */
    public double getTotalCost() {
        return totalCost;
    }

    /**
     * @return cell that leads to this
     */
    public Cell getPrevious() {
        return previous;
    }

    /**
     * @return type of this cell (see enum options)
     */
    public NodeDescription getNodeDescription() {
        return nodeDescription;
    }

    /**
     * @param nodeDescription type of this cell (see enum options)
     */
    public void setNodeDescription(NodeDescription nodeDescription) {
        this.nodeDescription = nodeDescription;
    }

    /**
     * @param previous cell that leads to this
     */
    public void setPrevious(Cell previous) {
        this.previous = previous;
    }

    /**
     * @return true if cell was already visited during A* search
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * @param visited if cell was already visited during A* search
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * @return index of this cell in the graphMatrix
     */
    public Vector2D getIndex() {
        return index;
    }

    /**
     * @param i index of row in the graphMatrix
     * @param j index of column in the graphMatrix
     */
    public void setIndex(double i , double j) {
        this.index = new Vector2D(i,j);
    }

    /**
     * @return true if cell will be the wall when maze uw created
     */
    public boolean isWall() {
        return wall;
    }

    /**
     * @param wall if cell will be the wall when maze uw created
     */
    public void setWall(boolean wall) {
        this.wall = wall;
    }
}
