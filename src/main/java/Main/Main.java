package Main;


import graphics.Display;
import javafx.application.Application;
import objects.FileReader;


public class Main {

    private static Universe universe ;

    public static void main(String[] args) {

        universe = new Universe(new FileReader());
        Application.launch(Display.class);
//        new BotExperiments();

    }

    public static Universe getUniverse(){
        return universe;
    }

}
