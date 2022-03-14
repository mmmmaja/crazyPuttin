package objects;

import physics.Vector2D;

public class TerrainGenerator {
    private static final double STEP_SIZE = 0.01;

    public TerrainGenerator(){}
    // TODO
    private static final double STEP = 0.167 ;
    public static double getHeight(Vector2D position) {

        double x = position.getX();
        double y = position.getY();

//        return  1/100.0 * x * x + 1/100.0 * y*y;
//        return Math.sin(x/6 + y / 6);
//        return Math.pow(1.05, x)+ Math.pow(1.08, y);
//        return Math.sin(x/6 + y/10) + Math.cos(Math.exp(y/100 + x/1000) + 0.8;
        return 0.5 * ( Math.sin( x - y ) / 7 + 0.9 ) ;
    }
    public static double getSlopeX( Vector2D currentPosition){
        double x = currentPosition.getX() ;
        double y = currentPosition.getY() ;
        return ( getHeight( new Vector2D( x + STEP , y ) ) - getHeight(new Vector2D( x - STEP , y ) ) ) / ( 2*STEP) ;
    }
    public static double getSlopeY( Vector2D currentPosition){
        double x = currentPosition.getX() ;
        double y = currentPosition.getY() ;
        return ( getHeight( new Vector2D( x , y + STEP ) ) - getHeight(  new Vector2D( x , y - STEP ) ) ) / ( 2*STEP) ;
    }

    public double getStepSize(){
        return STEP_SIZE;
    }



}
