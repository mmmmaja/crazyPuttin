package objects;

import physics.Vector2D;

public class TerrainGenerator {
    private double STEP_SIZE = 0.01;
    // TODO
    public double getHeight(Vector2D position) {

        double x = position.getX();
        double y = position.getY();

        return Math.sin(x/6 + y / 6);
//        return Math.sin(x/6 + y/10) + Math.cos(Math.exp(y/100 + x/1000) + 0.8;
    }
    public double getStepSize(){
        return STEP_SIZE;
    }



}
