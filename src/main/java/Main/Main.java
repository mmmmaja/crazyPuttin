package Main;


import objects.FileReader;

public class Main {
    private static Universe universe ;
    public static void main(String[] args) {
        universe = new Universe(new FileReader());
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
