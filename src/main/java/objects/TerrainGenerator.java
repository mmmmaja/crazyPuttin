package objects;

import physics.Vector2D;


public class TerrainGenerator {

    private static final double STEP_SIZE = 0.0001;
    private static final FileReader fileReader = new FileReader();
    private static final Vector2D sandPitX = fileReader.getSandPitX();
    private static final Vector2D sandPitY = fileReader.getSandPitY();

    /**
     *
     * @param position of the point to be checked
     * @return height of the terrain at the given position based on the function passed in the InputFile
     */
    public static double getHeight(Vector2D position) {
        return fileReader.calculator(position.getX() , position.getY()) ;
    }


    public static double getSlopeX( Vector2D currentPosition){
        double x = currentPosition.getX() ;
        double y = currentPosition.getY() ;
        return -( getHeight( new Vector2D( x + STEP_SIZE , y ) ) - getHeight(new Vector2D( x - STEP_SIZE , y ) ) ) / ( 2*STEP_SIZE) ;
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
        if (isSand( position.getX() , position.getY() ) ){
            return fileReader.getSandPitKineticFriction();
        }
        return fileReader.getKineticFriction();
    }

    public static double getStaticFrictionCoefficient(Vector2D position){
        if(isSand( position.getX() , position.getY() ) ){
            return fileReader.getSandPitStaticFriction();
        }
        return fileReader.getStaticFriction();
    }

}
