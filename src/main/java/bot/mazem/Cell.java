package bot.mazem;

import physics.Vector2D;

import java.util.ArrayList;


public class Cell {
    private int x;
    private int y;
    private double costToTarget;
    private double costToPos;
    private double cost;
    ArrayList<Cell> neighbors;
    Cell previous;
    NodeDescription nodeDescription = NodeDescription.grass;


    Cell(int x, int y){
        this.x = x;
        this.y = y;

        costToPos = 0; // the cost of the movement from the start cell to the current cell
        costToTarget = 0; // the distance from the target's cell to the current cell
        cost = 0; // g + h + l
        //l = 0; //Favor lines that follow as fewer lines as possible (fewer shots)
        this.neighbors = new ArrayList<>();

    }
    public void setAllCosts(Vector2D target , Vector2D position){
        this.costToTarget = distance(target);
        this.costToPos = distance(position);
        this.cost = this.costToPos + this.costToTarget;
    }
    public void setAllCosts(Cell target , Cell position){
        this.costToTarget = distance(target);
        this.costToPos = distance(position);
        this.cost = this.costToPos + this.costToTarget;
    }

    public double distance(Cell myCell) {
        return Math.sqrt(Math.pow(this.x - myCell.x, 2) + Math.pow(this.y - myCell.y, 2));
    }
    public double distance(Vector2D vector2D) {
        return Math.sqrt(Math.pow(this.x - vector2D.getX(), 2) + Math.pow(this.y - vector2D.getY(), 2));
    }
    public double distance(double x , double y ) {
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

    public double getCostToPos() {
        return costToPos;
    }

    public void setCostToPos(double costToPos) {
        this.costToPos = costToPos;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
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


}
