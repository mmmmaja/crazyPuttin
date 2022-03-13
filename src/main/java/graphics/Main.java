package graphics;

import javafx.application.Application;
import objects.FileReader;
import objects.Universe;


public class Main {

    private static Universe universe;

    public static void main(String[] args) {
        universe = new Universe(new FileReader());
        Application.launch(Display.class, args);
    }

    public static Universe getUniverse() {
        return universe;
    }
}
