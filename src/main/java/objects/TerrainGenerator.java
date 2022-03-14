package objects;

import physics.Vector2D;

public class TerrainGenerator {

    private static final double STEP_SIZE = 0.01;


    public static double getHeight(Vector2D position) {

        double x = position.getX();
        double y = position.getY();

        return -Math.sin(x/6 + y / 6);
    }

    public double getStepSize(){
        return STEP_SIZE;
    }


}
