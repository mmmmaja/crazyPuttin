package Main;

import bot.TestShot;
import physics.*;

public class Experiments {
    Universe universe ;
    Vector2D velocity ;
    public Experiments(Vector2D velocity){
        this.universe = Main.getUniverse();
        this.velocity = velocity;
    }
    public void run(){

        Solver[] solvers = { new RK4() , new Heuns3(), new RK2() , new Euler()};
        String[] solverNames = {"RK4", "Heuns3" , "RK2" , "Euler"};
        double i = 0.0001 ;
        for( double k = 0.0001 ; k < 0.01 ; k+= 0.0001  ){
            System.out.print(k + ",");
        }
        System.out.println();
        for (int j = 0; j < solvers.length; j++) {
            universe.setSolver( solvers[j] );
            System.out.println(solverNames[j]);
            for(  i  = 0.0001 ; i < 0.01 ; i+= 0.0001  ){
                universe.getSolver().setH(i);
                TestShot testShot = new TestShot(this.universe,velocity, this.universe.getTarget().getPosition() );
                System.out.print(String.format("%.17f ," , testShot.getFinalPosition().getX()));
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Experiments experiments = new Experiments( new Vector2D(2,0));
        experiments.run();
    }
}
