package Main;


import graphics.Display;
import javafx.application.Application;
import objects.FileReader;
import physics.Euler;


public class Main {
    private static Universe universe ;

    public static void main(String[] args) {

        //FIXME SHOTS
        //TODO target detection

        universe = new Universe(new FileReader()  );

        Application.launch(Display.class);

    }
    public static Universe getUniverse(){
        return universe;
    }

}
