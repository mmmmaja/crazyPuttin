package bot.maze;

import java.util.ArrayList;

import Main.Main;
//import Main.Universe;
import objects.Terrain;
import objects.TerrainGenerator;
import physics.Vector2D;


public class Astar {

    public static int STEP= 1; //todo:decide step size
    public static int cols;           //for grid division of the terrain
    public static int rows;
    public Vector2D targetPosition;
    public Vector2D startPosition;
    MyCell[][] grid;
    ArrayList<MyCell> toVisit;
    ArrayList<MyCell> visited;
    MyCell start;
    MyCell end;
    ArrayList<MyCell> path;
    boolean solutionFound;


    public Astar() {
        cols = 2*Terrain.TERRAIN_WIDTH/STEP;
        rows = 2*Terrain.TERRAIN_HEIGHT/STEP;
        targetPosition=Main.getUniverse().getTarget().getPosition();
        startPosition=Main.getUniverse().getFileReader().getInitialPosition();
        grid = new MyCell[cols][rows];
        toVisit = new ArrayList<MyCell>(cols*rows);
        visited = new ArrayList<MyCell>(cols*rows);
        path = new ArrayList<MyCell>(cols*rows);
        solutionFound=false;
    }


    public void createGrid() {
        /**
         * creating a new cell for each of the grid's square
         */
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = new MyCell(i, j);
                //if the cell is underwater or an obstacle the cell is a "wall"
                grid[i][j].wall=(addObstacles(grid[i][j])) || (addWater(grid[i][j]));

            }
        }
        /**
         * since our start and target position won't necessarily be in one of or cell's positions,
         * we will let the closest cells to this point serve as our temporary start and end points.
         */
        start = grid[(int) Math.round((startPosition.getX()+Terrain.TERRAIN_WIDTH)/STEP)][(int) Math.round((startPosition.getY()+Terrain.TERRAIN_HEIGHT)/STEP)];
        end = grid[(int) Math.round((targetPosition.getX()+Terrain.TERRAIN_WIDTH)/STEP)][(int) Math.round((targetPosition.getY()+Terrain.TERRAIN_HEIGHT)/STEP)];
        toVisit.add(start);
    }
    public void createNeighbors() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j].addNeighbors(grid);
            }
        }

    }

    public boolean addObstacles(MyCell cell){
        for (int k=0; k<Main.getUniverse().getObstacles().size();k++) {
            if (cell.x == (int) Math.round((Main.getUniverse().getObstacles().get(k).getPosition().getX()+Terrain.TERRAIN_WIDTH)/STEP) &&
                    cell.y == (int) Math.round((Main.getUniverse().getObstacles().get(k).getPosition().getY()+Terrain.TERRAIN_HEIGHT))/STEP) {
                return true;
            }


        }
        return false;
    }
    public boolean addWater(MyCell cell){
        if (TerrainGenerator.getHeight(new Vector2D( cell.x, cell.y )) < 0) {
            return true;
        }
        return false;

    }
    //todo : sand..changes f?

    public ArrayList<MyCell> findPath(){
        createGrid();
        createNeighbors();
        /**
         *  as long as there are elements we need to visit, and we didn't reach the target --> we continue
         *  if openSet is empty, and we didn't get to the target -->no solution, the algorithm is finished
         */
        while (!toVisit.isEmpty() && !solutionFound){
            //Keep going
            int winner = 0;//lowest index
            for (int i = 0; i < toVisit.size(); i++) {
                if (toVisit.get(i).f<toVisit.get(winner).f) {
                    winner = i;
                }
            }
            MyCell current = toVisit.get(winner);
            if (current==end) {  //--> found target
                MyCell temp = current;
                path.add(temp);
                while(temp.previous !=null){//exist previous
                    path.add(temp.previous);
                    temp = temp.previous;
                }
                System.out.println(" we are done");
                solutionFound=true;
                System.out.println("start x: "+start.x+"start y: "+start.y);
                System.out.println("target x: "+end.x+"target y: "+end.y);
                for (MyCell cell : path) {
                    System.out.println("x is: "+cell.x+"y is: "+cell.y+"is a wall"+cell.wall);}
                return path;

            }
            //if current cell is not the target
            toVisit.remove(current);
            visited.add(current);

            ArrayList<MyCell> neighbors = current.neighbors;
            for (int i = 0; i < neighbors.size(); i++) {
                MyCell neighbor = neighbors.get(i);//checking every neighbor
                if (!visited.contains(neighbor) && !neighbor.wall) {
                    int temp = current.g+ MyCell.euclidianDistance(neighbor, current);
                    if(toVisit.contains(neighbor)){//neighbor has not been visited
                        if(temp<neighbor.g){//check if this g is a better cost
                            neighbor.g = temp;//update g
                        }
                    }
                    else{//if the neighbor is not in the open set, that means he was not yet visited
                        neighbor.g = temp;
                        toVisit.add(neighbor);
                    }
                    neighbor.h = MyCell.euclidianDistance(neighbor, end);
                    neighbor.f = neighbor.g + neighbor.h;
                    neighbor.previous = current;
                }
            }


        }
        //if we have no more cells in the open set, but we did not get to the target,
        // then there is no solution;

        throw new RuntimeException("couldn't find a solution");

    }



}
