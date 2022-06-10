package bot.mazem;

import Main.Main;
import objects.Obstacle;
import objects.Target;
import objects.Terrain;
import physics.Vector2D;
import java.util.ArrayList;
import java.util.Random;


public class RecursiveMaze {

    public static int step = 8; // how thick the walls are, to be changed by the EventHandler

    private final Graph graph;
    private final Random random = new Random();

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
        Cell[][] cellGraph = this.graph.getGraph();
        for (Cell[] line : cellGraph) {
            for (Cell cell : line) {

                // if this is an obstacle set the wall to false (do not let it be covered)
                switch (cell.getNodeDescription()) {
                    case tree, obstacle, water -> cell.setWall(false);
                }
                // disable the borders of the map to be covered with walls
                // border of the map without the maze
                int BORDER = 2;
                if (
                        cell.getIndex().getY() < BORDER
                        || cell.getIndex().getY() > this.graph.getGraph()[0].length - 2 * BORDER
                        || cell.getIndex().getX() < BORDER
                        || cell.getIndex().getX() > this.graph.getGraph().length - 2 * BORDER
                ) {
                    cell.setWall(false);
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
                current = nextStep(current);
            }
            while (current != null);
        }
    }


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
                graph.getGraph()[(int) i][(int) j].setWall(false);
            }
        }
    }

    /**
     * @return array of obstacles to be added to the display and the universe
     */
    public ArrayList<Obstacle> getObstacles() {
        double SIDE_LENGTH = 0.5; // side of each obstacle

        ArrayList<Obstacle> obstacles = new ArrayList<>();
        for (int i = 0; i < graph.getGraph().length; i++) {
            for (int j = 0; j < graph.getGraph()[0].length; j++) {
                Cell cell = graph.getGraph()[i][j];
                if (cell.isWall()) {
                    double x = cell.getX();
                    double y = cell.getY();

                    obstacles.add(new Obstacle(new Vector2D(x, y), SIDE_LENGTH));

                    // connect this obstacle with the one on the right
                    if (i < graph.getGraph().length - 1) {
                        if (graph.getGraph()[i + 1][j].isWall()) {
                            for (double addition = SIDE_LENGTH; addition <= 1; addition += SIDE_LENGTH) {
                                obstacles.add(new Obstacle(new Vector2D(x + addition, y), SIDE_LENGTH));
                            }
                        }
                    }
                    // connect this obstacle with the one on the bottom
                    if (j < graph.getGraph()[0].length - 1) {
                        if (graph.getGraph()[i][j + 1].isWall()) {
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
     * @return array of the neighbours of the cell (not the diagonal ones!)
     */
    private ArrayList<Cell> findNeighbours(Cell cell) {
        ArrayList<Cell> neighbours = new ArrayList<>();
        Vector2D index = cell.getIndex();
        int minX = (int) (index.getX() - step);
        int minY = (int) (index.getY() - step);
        int maxX = (int) (index.getX() + step);
        int maxY = (int) (index.getY() + step);

        if (minX >= 0) {
            neighbours.add(graph.getGraph()[minX][(int) index.getY()]);
        }
        if (minY >= 0) {
            neighbours.add(graph.getGraph()[(int) index.getX()][minY]);
        }
        if (maxX < graph.getGraph().length) {
            neighbours.add(graph.getGraph()[maxX][(int) index.getY()]);
        }
        if (maxY < graph.getGraph()[0].length) {
            neighbours.add(graph.getGraph()[(int) index.getX()][maxY]);
        }
        return neighbours;
    }
}
