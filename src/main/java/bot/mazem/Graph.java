package bot.mazem;


import Main.*;
import objects.*;
import physics.Vector2D;

public class Graph {
    Cell[][] graph ;
    private final double STEP = Terrain.STEP;
    private final int TERRAIN_WIDTH = Terrain.TERRAIN_WIDTH ;
    private final int TERRAIN_HEIGHT= Terrain.TERRAIN_HEIGHT;
    private final int WIDTH = (int)(Terrain.TERRAIN_WIDTH/STEP * 2);
    private final int HEIGHT =(int)(Terrain.TERRAIN_HEIGHT/STEP * 2);
    private Vector2D position ;
    private Vector2D target ;
    private Universe universe = Main.getUniverse();
    private Cell startingCell;
    private Cell targetCell;

    public Graph(){
        this.position = universe.getBall().getPosition();
        this.target = universe.getTarget().getPosition();
        createGraph();

    }

    public void createGraph(){
        graph = new Cell[WIDTH][HEIGHT];
        for(int i = 0 , x = -TERRAIN_WIDTH ; i< WIDTH ; i++ ,x+=STEP){
            for (int j = 0, y = -TERRAIN_HEIGHT; j < HEIGHT; j++ , y+=STEP) {
                Cell cell = new Cell(x,y);
                NodeDescription nodeDescription = nodeDescription(x,y);


                if (x == Math.round(position.getX()) && y == Math.round(position.getY())) {

                    startingCell = cell;
                }
                if( x == (int)target.getX()   && y == (int)target.getY())
                    targetCell = cell;
                cell.setNodeDescription(nodeDescription);
                cell.setAllCosts(target,position);
                cell.setIndex(i,j);
                graph[i][j] = cell ;

            }
        }

    }
    public void connectNeighbors() {
        if (graph == null) createGraph();
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {

                Cell cell = graph[i][j];
                int min_x = Math.max(0 , i-1);
                int max_x = Math.min(WIDTH-1 , i+1);
                int min_y = Math.max(0 , j-1);
                int max_y = Math.min(HEIGHT-1 , j+1);

                for (int x = min_x; x <= max_x; x++) {
                    for (int y = min_y; y <= max_y; y++) {
                        if (i != x || j != y) {
                            cell.addNeighbors( graph[x][y] );
                        }
                    }
                }
            }
        }
    }
    public NodeDescription nodeDescription(double x , double y ){

        if(isInObstacle(x,y)) return NodeDescription.obstacle;
        if(isInTree(x,y)) return NodeDescription.tree;
        if(TerrainGenerator.getHeight(x,y)<0){return NodeDescription.water;}
        if(TerrainGenerator.isSand(x,y)){ return NodeDescription.sand;}
        if(position.getX() == x && position.getY() == y )return NodeDescription.start;
        if(target.getX() == x && position.getY() == y) return NodeDescription.target;

        return NodeDescription.grass;
    }

    public boolean isInTree(double x , double y){
        for (Tree tree : universe.getTrees()) {
            double treeXPos = tree.getPosition().getX();
            double treeYPos = tree.getPosition().getY();
            double r = tree.getCylinder().getRadius() * 2;
            if ((new Vector2D(x - treeXPos, y - treeYPos)).getMagnitude() < r)  return true;
        }
        return false ;
    }

    public boolean isInObstacle(double x , double y ){
        double rBall = universe.getBall().getRADIUS();
        for (Obstacle obstacle : universe.getObstacles()) {
            double obstXPos = obstacle.getPosition().getX();
            double obstYPos = obstacle.getPosition().getY();
            double obstDim = obstacle.getDimension();
            if((x <= obstXPos+obstDim/2+rBall && x >= obstXPos-obstDim/2-rBall) &&
                    (y <= obstYPos+obstDim/2+rBall && y >= obstYPos-obstDim/2-rBall)) {
                return true;
            }
        }
        return false;
    }


    public Cell[][] getGraph() {
        return graph;
    }

    public void setGraph(Cell[][] graph) {
        this.graph = graph;
    }

    public double getSTEP() {
        return STEP;
    }

    public int getTERRAIN_WIDTH() {
        return TERRAIN_WIDTH;
    }

    public int getTERRAIN_HEIGHT() {
        return TERRAIN_HEIGHT;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
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

    public void setStartingCell(Cell startingCell) {
        this.startingCell = startingCell;
    }

    public Cell getTargetCell() {
        return targetCell;
    }

    public void setTargetCell(Cell targetCell) {
        this.targetCell = targetCell;
    }


}
