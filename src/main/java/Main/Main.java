package Main;


import graphics.Display;
import javafx.application.Application;
import objects.FileReader;
import physics.Vector2D;


public class Main {

    private static Universe universe ;

    public static void main(String[] args) {

        universe = new Universe(new FileReader());
        Application.launch(Display.class);
//        Experiments experiments = new Experiments( new Vector2D( 2, 0 ));
//        experiments.run();
    }

    public static Universe getUniverse(){
        return universe;
    }

}
