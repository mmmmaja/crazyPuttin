package Main;


import bot.TestShot;
import graphics.Display;
import javafx.application.Application;
import objects.FileReader;
import objects.Obstacle;
import physics.Vector2D;
import tests.BotExperiments;


public class Main {

    private static Universe universe ;

    public static void main(String[] args) {

        universe = new Universe(new FileReader());
//        Application.launch(Display.class);
        new BotExperiments();

    }

    public static Universe getUniverse(){
        return universe;
    }

}
