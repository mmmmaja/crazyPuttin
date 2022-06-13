package bot.maze;


import Main.*;
import objects.*;
import physics.Vector2D;

/**
 * class that holds the graph consisting of cells
 * used for creating mazes and for the mazeBot
 */
public class Graph {

    private Cell[][] graphMatrix;

    private final Universe universe = Main.getUniverse();

    private final double STEP = Terrain.STEP;
    private final int WIDTH = (int)(Terrain.TERRAIN_WIDTH/STEP * 2);
    private final int HEIGHT = (int)(Terrain.TERRAIN_HEIGHT/STEP * 2);

    private Vector2D ballPosition;
    private Vector2D target ;

    private Cell startingCell;
    private Cell targetCell;

    public Graph() {

        this.ballPosition = universe.getBall().getPosition();
        this.target = universe.getTarget().getPosition();

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

                if (x == Math.round(ballPosition.getX()) && y == Math.round(ballPosition.getY())) {
                    this.startingCell = cell;
                }

                if ( x == (int)target.getX()  && y == (int)target.getY()) {
                    targetCell = cell;
                }

                cell.setNodeDescription(nodeDescription(x, y));
                cell.setAllCosts(target, ballPosition);
                cell.setIndex(i, j);

                this.graphMatrix[i][j] = cell ;

            }
        }
    }

    /**
     * adds the neighbours to each cell from this.graphMatrix
     */
    public void connectNeighbors() {

        if (this.graphMatrix == null) {
            createGraphMatrix();
        }
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {

                Cell cell = graphMatrix[i][j];
                int min_x = Math.max(0 , i - 1);
                int max_x = Math.min(WIDTH - 1 , i + 1);
                int min_y = Math.max(0 , j - 1);
                int max_y = Math.min(HEIGHT - 1 , j + 1);

                for (int x = min_x; x <= max_x; x++) {
                    for (int y = min_y; y <= max_y; y++) {
                        if ((x==i || y==j) && ( i != x || j != y) ) {
                            cell.addNeighbors( graphMatrix[x][y] );
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * @param x position of the cell
     * @param y position of the cell
     * @return enum describing type of the cell at position (x, y)
     */
    public NodeDescription nodeDescription(double x , double y ){

        if (isInObstacle(x,y))                                      return NodeDescription.obstacle;
        if (isInTree(x,y))                                          return NodeDescription.tree;
        if (TerrainGenerator.getHeight(x,y)<0)                      return NodeDescription.water;
        if (TerrainGenerator.isSand(x,y))                           return NodeDescription.sand;
        if (ballPosition.getX() == x && ballPosition.getY() == y )  return NodeDescription.start;
        if (target.getX() == x && ballPosition.getY() == y)         return NodeDescription.target;

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
                    (x <= obstXPos+obstDim/2+rBall && x >= obstXPos-obstDim/2-rBall) &&
                    (y <= obstYPos+obstDim/2+rBall && y >= obstYPos-obstDim/2-rBall)
            ) {
                return true;
            }
        }
        return false;
    }


    public Cell[][] getGraphMatrix() {
        return graphMatrix;
    }

    public void setGraphMatrix(Cell[][] graphMatrix) {
        this.graphMatrix = graphMatrix;
    }

    public double getSTEP() {
        return STEP;
    }

    public Vector2D getBallPosition() {
        return ballPosition;
    }

    public void setBallPosition(Vector2D ballPosition) {
        this.ballPosition = ballPosition;
    }

    public Vector2D getTarget() {
        return target;
    }

    public void setTarget(Vector2D target) {
        this.target = target;
    }

    public Cell getStartingCell() {
        return startingCell;
    }

    public Cell getTargetCell() {
        return targetCell;
    }

}
