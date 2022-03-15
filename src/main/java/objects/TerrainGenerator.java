package objects;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import physics.Vector2D;


public class TerrainGenerator {

    private static final double STEP_SIZE = 0.01;
    private static FileReader fileReader;
    private static Expression expression;

    public TerrainGenerator(FileReader fileReader) {
        TerrainGenerator.fileReader = fileReader;
        TerrainGenerator.expression = fileReader.getExpression();
    }

    public static double getHeight( Vector2D position) {

        double x = position.getX();
        double y = position.getY();

        return Math.sin(x/6 + y / 6);

        // FIXME troubles with evaluating the expression
       // return fileReader.calculator(expression, (float) x, (float) y);
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
