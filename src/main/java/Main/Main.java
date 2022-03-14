package Main;


import objects.FileReader;

import java.io.File;

public class Main {
    private static Universe universe ;

    public static void main(String[] args) {

        File file = new File("C:\\Users\\bpk_e\\Desktop\\crazyPuttin2\\src\\main\\java\\example_inputfile.txt");
        universe = new Universe(new FileReader(file));

        Game game = new Game(universe);
        game.start();
        // TODO here call the new Thread and wait for the action

    }
    public static Universe getUniverse(){
//        if(universe == null )
//            return
    return universe;
    }

}
