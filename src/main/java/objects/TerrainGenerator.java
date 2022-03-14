package objects;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import physics.Vector2D;


/**
 * TODO make it not static: problem getSlope methods
 */
public class TerrainGenerator {

    private static final double STEP_SIZE = 0.01;

    public TerrainGenerator(){
//        Expression expression = new ExpressionBuilder(equation)
//                .variables("x", "y")
//                .build()
//                .setVariable("x", this.xt)
//                .setVariable("y", this.yt);
//
//        float result = (float) expression.evaluate();

        //Assertions.assertEquals(1, result);

    }

    public static double getHeight( Vector2D position) {

        double x = position.getX();
        double y = position.getY();

        return Math.sin(x/6 + y / 6)*10;
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

    public double getStepSize(){
        return STEP_SIZE;
    }


}
