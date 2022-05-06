package bot.maze;

import Main.Main;
import physics.Vector2D;

import java.util.ArrayList;
import Main.Universe;



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
        System.out.println("updown");
        int x=path.get(counter).x;
        int y=path.get(counter).y;
        counter++;
        for (; counter< path.size();counter++){
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


    public ArrayList<Vector2D> getNextPosition(){
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
        System.out.println("can  get a heeya");
        nextPosition.set(0,Main.getUniverse().getFileReader().getInitialPosition());
        nextPosition.add(Main.getUniverse().getTarget().getPosition());
        return nextPosition;
    }



//    public ArrayList<Vector2D> getNextPosition() {
//        return this.nextPosition;
//    }
}
