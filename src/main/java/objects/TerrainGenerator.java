package objects;

import physics.Vector2D;

public class TerrainGenerator {
    private static final double STEP_SIZE = 0.01;


    // TODO
    public static double getHeight(Vector2D position) {

        double x = position.getX();
        double y = position.getY();

//        return  1/100.0 * x * x + 1/100.0 * y*y;
//        return Math.sin(x/6 + y / 6);
        return Math.pow(1.05, x)+ Math.pow(1.08, y);
//        return Math.sin(x/6 + y/10) + Math.cos(Math.exp(y/100 + x/1000) + 0.8;
    }

    public double getStepSize(){
        return STEP_SIZE;
    }



}
