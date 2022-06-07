package bot.mazem;


import Main.*;
import javafx.scene.Node;
import objects.FileReader;
import objects.Terrain;
import objects.TerrainGenerator;
import physics.Vector2D;

public class Graph {
    Cell[][] graph ;
    private final double STEP = Terrain.STEP;
    private final int TERRAIN_WIDTH = Terrain.TERRAIN_WIDTH ;
    private final int TERRAIN_HEIGHT= Terrain.TERRAIN_HEIGHT;
    private final int WIDTH = (int)(Terrain.TERRAIN_WIDTH/STEP * 2);
    private final int HEIGHT =(int)(Terrain.TERRAIN_HEIGHT/STEP * 2);
    private final Universe universe =  new Universe(new FileReader());
    private Vector2D position ;
    private Vector2D target ;

    public Graph(){
        this.position = universe.getBall().getPosition();
        this.target = universe.getTarget().getPosition();
        createGraph();
        connectNeighbors();
    }
    public void createGraph(){
        graph = new Cell[WIDTH][HEIGHT];
        for(int i = 0 , x = -TERRAIN_WIDTH ; i< WIDTH ; i++ ,x+=STEP){
            for (int j = 0, y = -TERRAIN_HEIGHT; j < HEIGHT; j++ , y+=STEP) {
                Cell cell = new Cell(x,y);
                NodeDescription nodeDescription = nodeDescription(x,y);
                cell.setNodeDescription(nodeDescription);
                cell.setAllCosts(target,position);
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
        Universe universe = Main.getUniverse();
        if(TerrainGenerator.getHeight(x,y)<0){return NodeDescription.water;}
        if(TerrainGenerator.isSand(x,y)){ return NodeDescription.sand;}

        return NodeDescription.grass;
    }


    public Cell[][] getGraph() {
        return graph;
    }

    public void setGraph(Cell[][] graph) {
        this.graph = graph;
    }

    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.getGraph();
    }

}
