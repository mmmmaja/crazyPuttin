package bot.mazes;

import Main.Main;
import objects.Obstacle;
import objects.Terrain;
import physics.Vector2D;
import java.util.ArrayList;
import java.util.Random;


/**
 * creates the maze with the given difficulty for the MazeBot
 * maze is random
 * triggering the maze changes position of the target since we wanted to make sure each time path can be found
 * levels from 1 to 7
 */
public class RecursiveMaze {

    private final Random random = new Random();

    // how thick the walls are, to be changed by the EventHandler in order to adjust levels
    public static int step = 4;

    // representation of the terrain
    private final Graph graph;

    // list of all the cells visited, used for backtracking
    private final ArrayList<Cell> path;

    // target will change its position to make sure there is always a path from the ball to the target
    private Vector2D newTargetPosition = new Vector2D(Terrain.TERRAIN_WIDTH, Terrain.TERRAIN_HEIGHT);


    public RecursiveMaze() {
        this.graph = new Graph();
        this.path = new ArrayList<>();

        // dig the way through the walls
        dig();

        // change the position of the target and update objects on the Display
        Main.getUniverse().getTarget().setPosition(newTargetPosition);
        Main.getUniverse().getPole().setPosition(newTargetPosition);
        Main.getUniverse().getFlag().setPosition(newTargetPosition);
    }

    /**
     * start with the graph all covered with wall and dig the way out
     * starting from the cell with the ball
     */
    private void dig() {

        // for all the cells boolean wall is set to true at the beginning
        Cell[][] cellGraph = this.graph.getGraphMatrix();
        for (Cell[] line : cellGraph) {
            for (Cell cell : line) {

                // if this is an obstacle set the wall to false (do not let it be covered)
                switch (cell.getNodeDescription()) {
                    case tree, obstacle, water -> cell.setWall(false);
                }
            }
        }
        // start with the cell being the position of the ball
        Cell current = graph.getStartingCell();

        // loop until there is no way to backtrack
        if (current != null) {
            this.path.add(current);
            do {
                if (
                        (current.getX() != graph.getStartingCell().getX() || current.getY() != graph.getStartingCell().getY())
                                && (graph.getStartingCell().distanceTo(current) > Terrain.TERRAIN_WIDTH * 0.3)
                ) {
                    this.newTargetPosition = new Vector2D(current.getX(), current.getY());
                }

                // find new cell and start digging again
                current = nextStep(current);
            }
            while (current != null);
        }
    }

    /**
     * @param current cell that became the grass
     * @return next Cell that from which we will look for next connections for the maze
     */
    private Cell nextStep(Cell current) {

        ArrayList<Cell> neighbors = findNeighbours(current);
        // remove all the neighbours that are walls, so we can not visit them
        neighbors.removeIf(neighbor -> !neighbor.isWall());

        Cell cell; // next cell to move from the current

        // there is a possibility to move from the current cell
        if (neighbors.size() != 0) {

            // choose random next cell
            int nextCellIndex = random.nextInt(neighbors.size());
            cell = neighbors.get(nextCellIndex);
            cell.setWall(false);
            this.path.add(cell);
        }

        // backtrack because no way possible from current, we are stuck
        else {
            this.path.remove(current);
            if (this.path.size() > 0) {
                cell = this.path.get(this.path.size() - 1);
            }
            else {
                // stop
                cell = null;
            }
        }
        if (cell != null) {
            connectPoints(cell, current);
        }
        return cell;
    }

    /**
     * dig the path through the walls from the target to the start
     * set the cells not to be the walls
     */
    private void connectPoints(Cell target, Cell start) {
        for (double i = start.getIndex().getX(); i <= target.getIndex().getX(); i++) {
            for (double j = start.getIndex().getY(); j <= target.getIndex().getY(); j++) {
                graph.getGraphMatrix()[(int) i][(int) j].setWall(false);
            }
        }
    }

    /**
     * @return array of obstacles to be added to the display and the universe
     */
    public ArrayList<Obstacle> getObstacles() {
        double SIDE_LENGTH = 0.5; // side of each obstacle

        ArrayList<Obstacle> obstacles = new ArrayList<>();
        for (int i = 0; i < graph.getGraphMatrix().length; i++) {
            for (int j = 0; j < graph.getGraphMatrix()[0].length; j++) {
                Cell cell = graph.getGraphMatrix()[i][j];
                if (cell.isWall()) {
                    double x = cell.getX();
                    double y = cell.getY();

                    obstacles.add(new Obstacle(new Vector2D(x, y), SIDE_LENGTH));

                    // connect this obstacle with the one on the right
                    if (i < graph.getGraphMatrix().length - 1) {
                        if (graph.getGraphMatrix()[i + 1][j].isWall()) {
                            for (double addition = SIDE_LENGTH; addition <= 1; addition += SIDE_LENGTH) {
                                obstacles.add(new Obstacle(new Vector2D(x + addition, y), SIDE_LENGTH));
                            }
                        }
                    }
                    // connect this obstacle with the one on the bottom
                    if (j < graph.getGraphMatrix()[0].length - 1) {
                        if (graph.getGraphMatrix()[i][j + 1].isWall()) {
                            for (double addition = SIDE_LENGTH; addition <= 1; addition += SIDE_LENGTH) {
                                obstacles.add(new Obstacle(new Vector2D(x, y + addition), SIDE_LENGTH));
                            }
                        }
                    }
                }
            }
        }
        return obstacles;
    }

    /**
     * @param cell current cell
     * @return array of the neighbours of the cell (excluding diagonal ones)
     */
    private ArrayList<Cell> findNeighbours(Cell cell) {
        ArrayList<Cell> neighbours = new ArrayList<>();
        Vector2D index = cell.getIndex();
        int minX = (int) (index.getX() - step);
        int minY = (int) (index.getY() - step);
        int maxX = (int) (index.getX() + step);
        int maxY = (int) (index.getY() + step);

        if (minX >= 0) {
            if (!isBorder(minX, (int) index.getY())) {
                neighbours.add(graph.getGraphMatrix()[minX][(int) index.getY()]);
            }
        }
        if (minY >= 0) {
            if (!isBorder((int) index.getX(), minY)) {
                neighbours.add(graph.getGraphMatrix()[(int) index.getX()][minY]);
            }
        }
        if (maxX < graph.getGraphMatrix().length) {
            if (!isBorder(maxX, (int) index.getY())) {
                neighbours.add(graph.getGraphMatrix()[maxX][(int) index.getY()]);
            }
        }
        if (maxY < graph.getGraphMatrix()[0].length) {
            if (!isBorder((int) index.getX(), maxY)) {
                neighbours.add(graph.getGraphMatrix()[(int) index.getX()][maxY]);
            }
        }
        return neighbours;
    }

    /**
     * @return true if Cell on the position (i, j) is a border of the map or water
     * then it has to be covered with wall
     */
    private boolean isBorder(int i, int j) {
        if (i - 1 >= 0) {
            if (graph.getGraphMatrix()[i - 1][j].getNodeDescription().equals(NodeDescription.water)) {
                return true;
            }
        }
        else {
            return true;
        }
        if (i + 1 < graph.getGraphMatrix().length) {
            if (graph.getGraphMatrix()[i + 1][j].getNodeDescription().equals(NodeDescription.water)) {
                return true;
            }
        }
        else {
            return true;
        }
        if (j - 1 >= 0) {
            if (graph.getGraphMatrix()[i][j - 1].getNodeDescription().equals(NodeDescription.water)) {
                return true;
            }
        }
        else {
            return true;
        }
        if (j + 1 < graph.getGraphMatrix()[0].length) {
            return graph.getGraphMatrix()[i][j + 1].getNodeDescription().equals(NodeDescription.water);
        }
        else {
            return true;
        }
    }
}
