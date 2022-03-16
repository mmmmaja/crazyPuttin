package objects;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import physics.Vector;
import physics.Vector2D;


/**
 * TODO make it not static: problem getSlope methods
 */
public class TerrainGenerator {

    private static final double STEP_SIZE = 0.0001;
    private static final FileReader fileReader = new FileReader();
    private static final Expression expression = fileReader.getExpression();
    private static final Vector2D sandPitX = fileReader.getSandPitX();
    private static final Vector2D sandPitY = fileReader.getSandPitY();
    public TerrainGenerator() {
    }

    public static double getHeight( Vector2D position) {
//        expression.setVariable("x" , position.getX());
//        expression.setVariable("y" , position.getY());

//        return  1/100.0 * x * x + 1/100.0 * y*y;
//        return Math.sin(x/6 + y/10) + Math.cos(Math.exp(y/100 + x/1000) + 0.8);
//        return Math.pow(1.05, x)+ Math.pow(1.08, y);
//        if(x > 100 && y > 100) {
//            return   Math.pow(x, 2) + Math.pow(y, 2);
//        }else
//            return 0;
        return fileReader.calculator( position.getX() , position.getY()) ;
    }


    public static double getSlopeX( Vector2D currentPosition){
        double x = currentPosition.getX() ;
        double y = currentPosition.getY() ;
        return ( getHeight( new Vector2D( x + STEP_SIZE , y ) ) - getHeight(new Vector2D( x - STEP_SIZE , y ) ) ) / ( 2*STEP_SIZE) ;
    }

    public static double getSlopeY( Vector2D currentPosition){
        double x = currentPosition.getX() ;
        double y = currentPosition.getY() ;
        return ( getHeight( new Vector2D( x , y + STEP_SIZE ) ) - getHeight(  new Vector2D( x , y - STEP_SIZE ) ) ) / ( 2*STEP_SIZE) ;
    }
    public static boolean isSand(double i, double j){
        if (i >= sandPitX.getX() && i <= sandPitX.getY()) {
            return j >= sandPitY.getX() && j <= sandPitY.getY();
        }
        return false;
    }
    public static double getKineticFrictionCoefficient(Vector2D position){
        if( isSand( position.getX() , position.getY() ) ){
            return fileReader.getSandPitKineticFriction();
        }
        return fileReader.getKineticFriction();
    }
    public static double getStaticFrictionCoefficient(Vector2D position){
        if( isSand( position.getX() , position.getY() ) ){
            return fileReader.getSandPitStaticFriction();
        }
        return fileReader.getStaticFriction();
    }



}
