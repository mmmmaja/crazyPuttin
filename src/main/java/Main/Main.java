package Main;


import graphics.Display;
import javafx.application.Application;
import objects.FileReader;


public class Main {

    private static Universe universe ;
    //FIXME updatePanel
    //FIXME Terrain Wacky Mesh Separation

    //TODO Merge water-target detection
    //TODO Add Arrow
    public static void main(String[] args) {

        universe = new Universe(new FileReader());
        Application.launch(Display.class);

    }

    public static Universe getUniverse(){
        return universe;
    }

}
