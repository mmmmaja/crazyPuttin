package bot.mazes;


import Main.*;
import objects.*;
import physics.Vector2D;

import java.util.ArrayList;


/**
 * class that holds the graph consisting of cells used for creating mazes and for the mazeBot and A* algorithm
 */
public class Graph {

    private final Universe universe = Main.getUniverse();
    private Cell[][] graphMatrix;

    // distance between each cell
    private final double STEP = Terrain.STEP;

    // dimensions of the matrix
    private final int WIDTH = (int) (Terrain.TERRAIN_WIDTH / STEP * 2 - 1);
    private final int HEIGHT = (int) (Terrain.TERRAIN_HEIGHT / STEP * 2);

    private final Vector2D ballPosition;
    private final Vector2D targetPosition;

    // cell that is on the position where the ball is
    private Cell startingCell;

    // cell that is on the position where the target is
    private Cell targetCell;

    public Graph() {

        this.ballPosition = universe.getBall().getPosition();
        this.targetPosition = universe.getTarget().getPosition();

        createGraphMatrix();
    }


    /**
     * fill this.graphMatrix with cells
     */
    public void createGraphMatrix() {

        this.graphMatrix = new Cell[this.WIDTH][this.HEIGHT];

        for (int i = 0 , x = -Terrain.TERRAIN_WIDTH ; i < WIDTH ; i++ ,x+=STEP) {
            for (int j = 0, y = -Terrain.TERRAIN_HEIGHT ; j < HEIGHT; j++ , y+=STEP) {
                Cell cell = new Cell(x, y);

                // specify type of the cell
                cell.setNodeDescription(nodeDescription(cell));
                // indices of this cell in the graphMatrix
                cell.setIndex(i, j);
                cell.setAllCosts(targetPosition, ballPosition);

                this.graphMatrix[i][j] = cell ;

            }
        }
    }


    /**
     * adds the neighbours of each cell from this.graphMatrix
     */
    public void connectNeighbors() {

        if (this.graphMatrix == null) {
            createGraphMatrix();
        }

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {

                Cell cell = graphMatrix[i][j];
                int min_x = Math.max(0 , i - 1);
                int max_x = Math.min(this.WIDTH - 1 , i + 1);
                int min_y = Math.max(0 , j - 1);
                int max_y = Math.min(this.HEIGHT - 1 , j + 1);

                for (int x = min_x; x <= max_x; x++) {
                    for (int y = min_y; y <= max_y; y++) {
                        if ( ( i != x || j != y) ) {
                        if (x == i || y == j)
                            cell.addNeighbors( graphMatrix[x][y] );
                        }
                    }
                }
            }
        }
    }

    /**
     * @return cell that has a description grass and is adjacent to the one with the ball
     */
    public Cell recalculateStartingCell() {

        int x = (int) ballPosition.getX();
        int y = (int) ballPosition.getY();

        ArrayList<Cell> cellsAroundBall = new ArrayList<>();
        cellsAroundBall.add(fetCellFromTerrain(x, y));
        cellsAroundBall.add(fetCellFromTerrain(x, y + 1));
        cellsAroundBall.add(fetCellFromTerrain(x + 1, y));
        cellsAroundBall.add(fetCellFromTerrain(x + 1, y + 1));

        for (Cell cell : cellsAroundBall) {
            if (cell.getNodeDescription() == NodeDescription.grass || cell.getNodeDescription() == NodeDescription.sand) {
                return cell;
            }
        }
        return null;
    }

    /**
     * @return enum describing type of the cell at position (x, y)
     */
    public NodeDescription nodeDescription(Cell cell){
        int x = cell.getX();
        int y = cell.getY();

        if (isInObstacle(x, y)) {
            return NodeDescription.obstacle;
        }
        if (isInTree(x, y)) {
            return NodeDescription.tree;
        }
        if (TerrainGenerator.getHeight(x, y) < 0) {
            return NodeDescription.water;
        }
        if (TerrainGenerator.isSand(x,y)) {
            return NodeDescription.sand;
        }
        if (x == Math.round(ballPosition.getX()) && y == Math.round(ballPosition.getY())) {
            this.startingCell = cell;
            return NodeDescription.start;
        }
        if ( x == (int) targetPosition.getX()  && y == (int) targetPosition.getY()) {
            targetCell = cell;
            return NodeDescription.target;
        }

        return NodeDescription.grass;
    }


    /**
     * @param x position of the cell
     * @param y position of the cell
     * @return true if the cell at the position (x, y) is contained in the tree
     */
    public boolean isInTree(double x , double y){
        for (Tree tree : universe.getTrees()) {
            double treeXPos = tree.getPosition().getX();
            double treeYPos = tree.getPosition().getY();
            double r = tree.getCylinder().getRadius() * 2;
            if ((new Vector2D(x - treeXPos, y - treeYPos)).getMagnitude() < r)  return true;
        }
        return false ;
    }


    /**
     * @param x position of the cell
     * @param y position of the cell
     * @return true if the cell at the position (x, y) is contained in the obstacle (rock)
     */
    public boolean isInObstacle(double x , double y ){
        double rBall = universe.getBall().getRADIUS();
        for (Obstacle obstacle : universe.getObstacles()) {
            double obstXPos = obstacle.getPosition().getX();
            double obstYPos = obstacle.getPosition().getY();
            double obstDim = obstacle.getDimension();
            if(
                    (x <= obstXPos+obstDim/2 + rBall && x >= obstXPos-obstDim/2 - rBall) &&
                    (y <= obstYPos+obstDim/2 + rBall && y >= obstYPos-obstDim/2 - rBall)
            ) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return array filled with cells corresponding to actual terrain read from file
     */
    public Cell[][] getGraphMatrix() {
        return graphMatrix;
    }

    /**
     * @return cell that is on the position where the ball is
     */
    public Cell getStartingCell() {
        return startingCell;
    }

    /**
     * @return cell that is on the position where the target is
     */
    public Cell getTargetCell() {
        return targetCell;
    }

    /**
     * @return cell that is equivalent to position (x, y) from the terrain
     */
    private Cell fetCellFromTerrain(int x , int y){
        return getGraphMatrix()
                [Terrain.TERRAIN_WIDTH + x]
                [Terrain.TERRAIN_HEIGHT + y];
    }

    /**
     * @param startingCell cell on which position ball is placed
     */
    public void setStartingCell(Cell startingCell) {
        this.startingCell = startingCell;
    }
}
