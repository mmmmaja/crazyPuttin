package Main;


import graphics.Display;
import javafx.application.Application;
import objects.FileReader;


public class Main {
    private static Universe universe ;

    public static void main(String[] args) {

        FileReader fileReader = new FileReader();
        System.out.println(fileReader.getHeightProfile());

        universe = new Universe(new FileReader());

        Application.launch(Display.class);

    }
    public static Universe getUniverse(){
        return universe;
    }

}
