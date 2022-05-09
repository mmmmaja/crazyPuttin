package bot.maze;

import Main.Main;
import bot.Bot;
import physics.Vector2D;

import java.util.ArrayList;


public class AStarBot implements Bot {

    private final ArrayList<Vector2D> nextPosition;
    private final ArrayList<MyCell> path;
    private  int counter;


    public AStarBot() {
        this.nextPosition = new ArrayList<>();
        Astar star = new Astar();
        this.path = star.findPath();
        this.counter = 0;
    }

    public void connectUpDownPath(){
        System.out.println("upDown");
        int x = path.get(counter).x;
        int y = path.get(counter).y;
        this.counter++;
        for (; counter < path.size(); counter++){
            if (x==path.get(counter).x){
                y=path.get(counter).y;
            }
            else {
                nextPosition.add(new Vector2D(x, y));
                for (Vector2D nextPosition : nextPosition) {
                    System.out.println("x is: "+nextPosition.getX()+" y is: "+nextPosition.getY());
                }
                connectLeftRightPath();
            }
        }

    }

    public void connectLeftRightPath(){
        System.out.println("sides");
        int x=path.get(counter).x;
        int y=path.get(counter).y;
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

    @Override
    public int getShotCounter() {
        return 0;
    }

    @Override
    public ArrayList<Vector2D> getVelocities() {
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
        nextPosition.set(0,Main.getUniverse().getFileReader().getInitialPosition());
        nextPosition.add(Main.getUniverse().getTarget().getPosition());
        return nextPosition;
    }

    @Override
    public String toString() {
        return "";
    }
}

