package bot.maze;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static java.lang.Math.abs;


public class MyCell {
    int x;
    int y;
    int width;
    int height;
    int f;
    int g;
    int h;
    ArrayList<MyCell> neighbors;
    MyCell previous;
    boolean wall;
    /**
     * each cell is a node with the position of the left most, upper corner
     * of the square in the grid it is build upon
     *
     *  0___ ___ ___
     *  |___|___|___|
     *  |___|___|___|
     *  |___|___|___|
     *
     *  where 0 represents the node of grid(0,0)
     */


    MyCell(int x,int y){
        this.x = x;
        this.y = y;
        /**
         * g --> the cost of the movement from the start cell to the current cell
         * h -->the distance from the target's cell to the current cell
         * f=g+h
         *
         */
        f = 0;
        g = 0;
        h = 0;
        this.width = Astar.WIDTH/Astar.cols;
        this.height = Astar.HEIGHT/Astar.rows;
        this.neighbors = new ArrayList<>(Astar.cols*Astar.rows);
        /**
         * wall represents a side of the cell the faces an obstacle
         */
        this.wall = false;



    }

    public Rectangle draw(Color color){

        Rectangle rectangle = new Rectangle(this.x*width,this.y*height,width,height);
        if (wall) {
            rectangle.fillProperty().set(Color.BLACK);
            rectangle.strokeProperty().setValue(Color.BLACK);

        }
        else{
            rectangle.setFill(color);
            rectangle.strokeProperty().setValue(Color.BLACK);
        }
        return rectangle;
    }
    public void addNeighbors(MyCell[][] grid){
        /**
         * if a cell is on the grid's boundaries we do not need to add 4 neighbors
         * since some of them will be outside the terrain
         * this way, each cell ca have at most 4 neighbors and at least 2
         *
         *
         *
         *        0___ ___ ___
         *        |___|___|___|
         *        |___|___|___|
         *        |___|___|___|
         *
         */

        /**
         * as log as the cell is not on the right-most part of the terrain we can add a right neighbor
         */

        if(x<Astar.cols-1){
            this.neighbors.add(grid[x+1][y]);
        }
        /**
         * as log as the cell is not on the left-most part of the terrain we can add a left neighbor
         */
        if(x>0){
            this.neighbors.add(grid[x-1][y]);
        }
        /**
         * as log as the cell is not on the bottom-boundary of the terrain we can add a bottom neighbor
         */
        if(y<Astar.rows-1){
            this.neighbors.add(grid[x][y+1]);
        }
        /**
         * as log as the cell is not on the top-boundary of the terrain we can add a top neighbor
         */
        if(y>0){
            this.neighbors.add(grid[x][y-1]);
        }
    }

    /**
     *
     *distance between to cells- will be used to calculate the g and h value's
     */
    public static int euclidianDistance(MyCell from,MyCell to){
            int distance = (to.x-from.x)^2 + (to.y-from.y)^2;
        return distance;
    }

//    public double dist (MyCell from, MyCell to){
//
//        Point2D p1 = new Point2D(from.location.getX(), from.location.getY());
//        return p1.distance(to.location.getX(),to.location.getY());
//    }
}
