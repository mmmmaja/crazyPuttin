package bot.mazem;

import physics.Vector2D;

import java.util.ArrayList;


public class Cell {
    private int x;
    private int y;
    private double costToTarget;
    private double costFromStart;
    private double changeDir ;
    private double totalCost;
    private ArrayList<Cell> neighbors;
    private Cell previous;
    private NodeDescription nodeDescription = NodeDescription.grass;
    private boolean visited = false ;
    private Vector2D index ;
    private boolean wall = true ;

    

    Cell(int x, int y){
        this.x = x;
        this.y = y;

        costFromStart = 0; // the cost of the movement from the start cell to the current cell
        costToTarget = 0; // the distance from the target's cell to the current cell
        changeDir = 0; //Favor lines that follow as fewer lines as possible (fewer shots)
        totalCost = 0; // g + h + l
        this.neighbors = new ArrayList<>();

    }
    public void setAllCosts(Vector2D target , Vector2D position){
        this.costToTarget = distanceTo(target);
        this.costFromStart = distanceTo(position);
        this.totalCost = this.costFromStart + this.costToTarget;
    }
    public void setAllCosts(Cell target , Cell position){
        this.costToTarget = distanceTo(target);
        this.costFromStart = distanceTo(position);
        this.totalCost = this.costFromStart + this.costToTarget;
    }

    public double distanceTo(Cell myCell) {
        return Math.sqrt(Math.pow(this.x - myCell.x, 2) + Math.pow(this.y - myCell.y, 2));
    }
    public double distanceTo(Vector2D vector2D) {
        return Math.sqrt(Math.pow(this.x - vector2D.getX(), 2) + Math.pow(this.y - vector2D.getY(), 2));
    }
    public double distanceTo(double x , double y ) {
        return Math.sqrt(Math.pow(this.x - x , 2) + Math.pow(this.y - y, 2));
    }

    public void addNeighbors(Cell myCell){
        neighbors.add(myCell);
    }

    public ArrayList<Cell> getNeighbors() {
        return neighbors;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getCostToTarget() {
        return costToTarget;
    }

    public void setCostToTarget(double costToTarget) {
        this.costToTarget = costToTarget;
    }

    public double getCostFromStart() {
        return costFromStart;
    }

    public void setCostFromStart(double costFromStart) {
        this.costFromStart = costFromStart;
        this.totalCost = costFromStart + costToTarget;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }


    public Cell getPrevious() {
        return previous;
    }

    public void setNeighbors(ArrayList<Cell> neighbors) {
        this.neighbors = neighbors;
    }

    public NodeDescription getNodeDescription() {
        return nodeDescription;
    }

    public void setNodeDescription(NodeDescription nodeDescription) {
        this.nodeDescription = nodeDescription;
    }

    public void setPrevious(Cell previous) {
        this.previous = previous;
    }
    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public double getChangeDir() {
        return changeDir;
    }

    public void setChangeDir(double changeDir) {
        this.changeDir = changeDir;
    }

    public Vector2D getIndex() {
        return index;
    }

    public void setIndex(Vector2D index) {
        this.index = index;
    }

    public void setIndex(double i , double j) {
        this.index = new Vector2D(i,j);
    }

    public boolean isWall() {
        return wall;
    }

    public void setWall(boolean wall) {
        this.wall = wall;
    }
}
