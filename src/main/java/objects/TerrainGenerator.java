package objects;

import org.controlsfx.tools.ValueExtractor;
import physics.Vector2D;

/**
 * class holding the information about the terrain profile
 */
public class TerrainGenerator {

    private static final double STEP_SIZE = 0.0001;
    private static final FileReader fileReader = new FileReader();
    private static final Vector2D sandPitX = fileReader.getSandPitX();
    private static final Vector2D sandPitY = fileReader.getSandPitY();


    /**
     * @param position of the point to be checked
     * @return height of the terrain at the given position based on the function passed in the InputFile
     */
    public static double getHeight(Vector2D position) {
        return fileReader.calculator(position.getX() , position.getY()) ;
    }
    public static double f(double x, double y ) {
        return fileReader.calculator(x , y) ;
    }


    /**
     *
     * @param currentPosition of the ball
     * @return x value of the slope at the given position
     */
    public static double getSlopeX(Vector2D currentPosition){
        double h =STEP_SIZE;
        double x = currentPosition.getX() ;
        double y = currentPosition.getY() ;
        return (f(x - 2*h, y) - 8*f(x-h ,y) + 8*f(x+h, y)-f(x+2*h, y)) / (12 * h);
    }
    public static double getSlopeX(double x , double y ){
        double h =STEP_SIZE;
        return (f(x - 2*h, y) - 8*f(x-h ,y) + 8*f(x+h, y)-f(x+2*h, y)) / (12 * h);
    }

    /**
     * @param currentPosition of the ball
     * @return y value of the slope at the given position
     */
    public static double getSlopeY(Vector2D currentPosition){
        double h = STEP_SIZE;
        double x = currentPosition.getX() ;
        double y = currentPosition.getY() ;
        return (f(x , y- 2*h) - 8 * f(x ,y-h) + 8*f(x, y+h)-f(x, y+2*h)) / (12 * h);
    }


   public static double getSlopeY(double x, double y ){
        double h = STEP_SIZE;
        return (f(x , y- 2*h) - 8 * f(x ,y-h) + 8*f(x, y+h)-f(x, y+2*h)) / (12 * h);
    }
    public static Vector2D getSlopes(Vector2D position ){
        double slopeX = getSlopeX(position);
        double slopeY = getSlopeY(position);
        return new Vector2D(slopeX , slopeY);
    }

    /**
     * @param i x index of the checked point
     * @param j y index of the checked point
     * @return true if point at the given index is a sandPit
     */
    public static boolean isSand(double i, double j) {
        if (i >= sandPitX.getX() && i <= sandPitX.getY()) {
            return j >= sandPitY.getX() && j <= sandPitY.getY();
        }
        return false;
    }

    /**
     *
     * @param position of the ball
     * @return kinetic friction coefficient at the given position
     */
    public static double getKineticFrictionCoefficient(Vector2D position){
        if (isSand(position.getX(), position.getY())) {
            return fileReader.getSandPitKineticFriction();
        }
        return fileReader.getKineticFriction();
    }

    /**
     * @param position of the ball
     * @return static friction coefficient at the given position
     */
    public static double getStaticFrictionCoefficient(Vector2D position){
        if (isSand(position.getX(), position.getY())) {
            return fileReader.getSandPitStaticFriction();
        }
        return fileReader.getStaticFriction();
    }

}