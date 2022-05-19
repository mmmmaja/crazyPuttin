package bot.star;


import graphics.Display;
import objects.Terrain;
import physics.Vector2D;


public class AStarAlgorithm {

    public static double STEP = 0.5;
    private Node[][] grid;

    public AStarAlgorithm() {
        createGrid();
        start();
    }

    private void createGrid() {
        this.grid = new Node[(int) ((1 / STEP) * 2 * Terrain.TERRAIN_HEIGHT)][(int) ((1 / STEP) * 2 * Terrain.TERRAIN_WIDTH)];
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[0].length; j++) {
                this.grid[i][j] = new Node(new Vector2D(i, j), convertPositionsGrid2Real(new Vector2D(i, j)));
            }
        }
        // add the neighbours for each node
        for (Node[] nodes : this.grid) {
            for (int j = 0; j < this.grid[0].length; j++) {
                nodes[j].addNeighbours(grid);
            }
        }
    }

    private void start() {

    }

    public Node[][] getGrid() {
        return this.grid;
    }


    /**
     * TODO!!!
     * @param realPosition
     * @return
     */
    private Vector2D convertPositionsReal2Grid(Vector2D realPosition) {
        return realPosition;
    }

    /**
     * TODO!!!
     * @param gridPosition
     * @return
     */
    private Vector2D convertPositionsGrid2Real(Vector2D gridPosition) {
        return gridPosition;
    }
}
