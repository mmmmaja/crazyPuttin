package bot.maze;

import physics.Vector2D;

import java.util.ArrayList;



public class AstarBot {
    public ArrayList<Vector2D> nextPosition;
    public ArrayList<MyCell> path;
    public Astar star;
    public static int counter;

    public AstarBot() {
        this.nextPosition = new ArrayList<>();
        this.star = new Astar();
        this.path = star.findPath();
        counter=0;
    }
    public void connectUpDownPath(){
        int x=path.get(counter).x;
        int y=path.get(counter).y;
        counter++;
        for (; counter< path.size();counter++){
            if (x==path.get(counter).x){
                y=path.get(counter).y;

            }
            else {
                nextPosition.add(new Vector2D(x, y));
                connectLeftRightPath();
            }
        }

    }

    public void connectLeftRightPath(){
        int x=path.get(AstarBot.counter).x;
        int y=path.get(AstarBot.counter).y;
        counter++;
        for (; counter< path.size();counter++){
            if (y==path.get(counter).y){
                x=path.get(counter).x;
            }
            else {
                nextPosition.add(new Vector2D(x, y));
                connectUpDownPath();
            }
        }

    }


    public void connectCells(){
        int x=path.get(counter).x;
        int y=path.get(counter).y;
        nextPosition.add(new Vector2D(x,y));
        counter++;

        if (x==path.get(counter).x) {
            connectUpDownPath();
        }
        else {
            connectLeftRightPath();
        }
        x=path.get(counter-1).x;
        y=path.get(counter-1).y;
        nextPosition.add(new Vector2D(x,y));
    }

    /**
     *
     * @param from is the initial position
     * @param to is the target position
     * @return the needed initial velocity to shoot the ball from start-->end
     */
//    public Vector2D initialSpeedCalc(Vector2D from, Vector2D to)
//    {
//       Vector2D initialSpeed;
//        Point2D p1 = new Point2D(from.getX(), from.getY());
//        double distance= p1.distance(to.getX(),to.getY());
//
//       return initialSpeed;
//    }
//    public void play(){
//        connectCells();
//    }

    public ArrayList<Vector2D> getNextPosition() {
        return this.nextPosition;
    }
}
