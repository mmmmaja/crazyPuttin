package bot.maze;

import Main.Main;
import objects.Terrain;
import physics.Vector2D;
import java.util.ArrayList;


public class AStarBot {

    private final ArrayList<Vector2D> nextPosition;
    private final ArrayList<MyCell> path;
    private int counter;


    public AStarBot() {
        this.nextPosition = new ArrayList<>();
        Astar star = new Astar();
        this.path = star.findPath();
        this.counter = 0;
    }

    public void connectUpDownPath() {
        int x = path.get(counter).x;
        int y = path.get(counter).y;
        this.counter++;
        for (; counter < path.size(); counter++) {
            if (x == path.get(counter).x) {
                y = path.get(counter).y;
            } else {
                nextPosition.add(new Vector2D(x, y));
                callConnection();
            }
        }

    }

//    public void connectLeftRightPath() {
//        int x = path.get(counter).x;
//        int y = path.get(counter).y;
//        counter++;
//        for (; counter < path.size(); counter++) {
//            if (y == path.get(counter).y) {
//                x = path.get(counter).x;
//            } else {
//                nextPosition.add(new Vector2D(x, y));
//                callConnection();
//            }
//        }
//
//    }

    public void connectLines() {
        int x0 = path.get(counter-1).x;
        int y0 = path.get(counter-1).y;
        int x2;
        int y2;
        double prevSlope;
        double nextSlope;
        counter++;
        for (; counter < path.size(); counter++) {
            int x1 = path.get(counter).x;
            int y1 = path.get(counter).y;
            if (counter < path.size() - 1) {
                x2 = path.get(counter + 1).x;
                y2 = path.get(counter + 1).y;
                prevSlope = (y1 - y0) / (double)(x1 - x0);
                nextSlope = (y2 - y1) / (double)(x2 - x1);
                if (prevSlope == nextSlope) {
                    x0 = path.get(counter + 2).x;
                    y0 = path.get(counter + 2).y;

                } else {
                    nextPosition.add(new Vector2D(x0, y0));

                    callConnection();
                }
            }
            else {
                x2 = path.get(counter - 2).x;
                y2 = path.get(counter - 2).y;
                prevSlope = (y1 - y0) / (double)(x1 - x0);
                nextSlope = (y2 - y1) / (double)(x2 - x1);
                if (prevSlope == nextSlope) {
                    x0 = path.get(counter + 1).x;
                    y0 = path.get(counter + 1).y;

                }
                else {
                    nextPosition.add(new Vector2D(x0, y0));

                    callConnection();
                }
            }


        }

    }




    public void callConnection(){
        int x=path.get(counter).x;
        int y=path.get(counter).y;
        if (x==path.get(counter).x) {
            connectUpDownPath();
        }
        else{
            connectLines();
        }

    }


    public int getShotCounter() {
        return 0;
    }


    public ArrayList<Vector2D> getVelocities() {
        ArrayList<Vector2D> velocities = new ArrayList<>();

        getNextPosition();
        System.out.println(nextPosition.size());
        for (Vector2D position : this.nextPosition) {
            System.out.println(position);
//            HillClimbingBot hillClimbingBot = new HillClimbingBot(Main.getUniverse(), position);
//            Vector2D velocity = hillClimbingBot.getVelocities().get(0);
//            velocities.add(velocity);
        }
        return velocities;
    }

    public ArrayList<Vector2D> getNextPosition() {


        int x=path.get(0).x;
        int y=path.get(0).y;
        nextPosition.add(new Vector2D(x,y));
        counter++;

        callConnection();
        nextPosition.set(0,Main.getUniverse().getFileReader().getInitialPosition());
        for (int i = 1; i<nextPosition.size();i++){
            double realX =nextPosition.get(i).getX()*Astar.STEP- Terrain.TERRAIN_WIDTH;
            double realY =nextPosition.get(i).getY()*Astar.STEP- Terrain.TERRAIN_HEIGHT;
            nextPosition.set(i,new Vector2D(realX,realY));
        }

        nextPosition.add(Main.getUniverse().getTarget().getPosition());
        return nextPosition;
    }

    @Override
    public String toString() {
        return "";
    }
}

