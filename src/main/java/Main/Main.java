package Main;


import graphics.Display;
import javafx.application.Application;
import objects.FileReader;
import tests.MazeExperiments;


public class Main {

    private static Universe universe ;

    public static void main(String[] args) {

        universe = new Universe(new FileReader());
        Application.launch(Display.class);
    }

    public static Universe getUniverse(){
        return universe;
    }

}
