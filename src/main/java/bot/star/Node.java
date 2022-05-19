package bot.star;

import Main.Main;
import bot.maze.Astar;
import objects.Obstacle;
import objects.TerrainGenerator;
import objects.Tree;
import physics.Vector2D;
import java.util.ArrayList;


public class Node {

    private final Vector2D gridPosition;
    private final Vector2D realPosition;
    private ArrayList<Node> neighbours;

    private double distanceToTarget;
    private double costFromStartCell = Integer.MAX_VALUE;

    private NodeType nodeType = NodeType.grass;


    public Node(Vector2D gridPosition, Vector2D realPosition) {
        this.gridPosition = gridPosition;
        this.realPosition = realPosition;
        createNode();

    }

    public Node(double gridX, double gridY, double realX, double realY) {
        this.gridPosition = new Vector2D(gridX, gridY);
        this.realPosition = new Vector2D(realX, realY);
        createNode();
    }

    private void createNode() {
        this.neighbours = new ArrayList<>();
        setNodeType();
        this.distanceToTarget = this.realPosition.getEuclideanDistance(Main.getUniverse().getTarget().getPosition());
    }


    private void setNodeType() {
        if (TerrainGenerator.getHeight(this.realPosition) < 0) {
            this.nodeType = NodeType.water;
        }
        for (Obstacle obstacle : Main.getUniverse().getObstacles()) {
            if (isInside(obstacle.getPosition(), obstacle.SIDE_LENGTH)) {
                this.nodeType = NodeType.rock;
            }
        }
        for (Tree tree : Main.getUniverse().getTrees()) {
            if (isInside(tree.getPosition(), tree.getDimension().getX())) {
                this.nodeType = NodeType.tree;
            }
        }
    }


    private boolean isInside(Vector2D objectPosition, double side) {
        return this.realPosition.getX() > objectPosition.getX()
                && this.realPosition.getX() < objectPosition.getX() + side
                && this.realPosition.getY() > objectPosition.getY()
                && this.realPosition.getY() < objectPosition.getY() + side;
    }


    public void addNeighbours(Node[][] grid) {
        if (this.gridPosition.getX() < grid[0].length - 1) {
            this.neighbours.add(grid[(int) (this.gridPosition.getX() + 1)][(int) this.gridPosition.getY()]);
        }
        if (this.gridPosition.getX() > 0) {
            this.neighbours.add(grid[(int) (this.gridPosition.getX() - 1)][(int) this.gridPosition.getY()]);
        }
        if (this.gridPosition.getY() < grid.length - 1) {
            this.neighbours.add(grid[(int) (this.gridPosition.getX())][(int) this.gridPosition.getY() + 1]);
        }
        if (this.gridPosition.getY() > 0) {
            this.neighbours.add(grid[(int) (this.gridPosition.getX())][(int) this.gridPosition.getY() + 1]);
        }

        // DIAGONALS
        if(this.gridPosition.getX() < grid[0].length - 1 && this.gridPosition.getY() < grid.length - 1) {
            this.neighbours.add(grid[(int) (this.gridPosition.getX() + 1)][(int) (this.gridPosition.getY() + 1)]);
        }
        if(this.gridPosition.getX() > 0 && this.gridPosition.getY() > 0){
            this.neighbours.add(grid[(int) (this.gridPosition.getX() - 1)][(int) (this.gridPosition.getY() - 1)]);
        }
        if(this.gridPosition.getX() < grid[0].length - 1 && this.gridPosition.getY() > 0){
            this.neighbours.add(grid[(int) (this.gridPosition.getX() + 1)][(int) (this.gridPosition.getY() - 1)]);
        }
        if(this.gridPosition.getX()>0 && this.gridPosition.getY()<Astar.rows-1){
            this.neighbours.add(grid[(int) (this.gridPosition.getX() - 1)][(int) (this.gridPosition.getY() + 1)]);
        }
    }



    public NodeType getNodeType() {
        return this.nodeType;
    }


    public double getDistanceToTarget() {
        return this.distanceToTarget;
    }

    public ArrayList<Node> getNeighbours() {
        return this.neighbours;
    }

    public void setCostFromStartCell(double cost) {
        this.costFromStartCell = cost;
    }
}
