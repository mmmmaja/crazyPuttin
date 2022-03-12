package objects;

import physics.Vector2D;

public class TerrainGenerator {
    private double STEP_SIZE = 0.01;
    // TODO
    private double getHeight(Vector2D position) {

        double x = position.getX();
        double y = position.getY();

        return Math.sin(x/6 + y / 6);
//        return Math.sin(x/6 + y/10) + Math.cos(Math.exp(y/100 + x/1000) + 0.8;
    }
    private double getSlopeX( Vector2D currentPosition){
        double x = currentPosition.getX() ;
        double y = currentPosition.getY() ;
        return ( getHeight( new Vector2D( x + STEP_SIZE , y ) ) - getHeight( currentPosition ) ) / STEP_SIZE ;
    }
    private double getSlopeY( Vector2D currentPosition){
        double x = currentPosition.getX() ;
        double y = currentPosition.getY() ;
        return ( getHeight( new Vector2D( x , y + STEP_SIZE ) ) - getHeight( currentPosition ) ) / STEP_SIZE ;
    }


}
