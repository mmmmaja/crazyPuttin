package objects;

import physics.Vector2D;

public class Terrain {

    private double staticFrictionCoefficient = 0.15;
    private double kineticFrictionCoefficient = 0.06;
    TerrainGenerator terrain ;
    public Terrain() {
        terrain = new TerrainGenerator();
    }
    public double getSlopeX( Vector2D currentPosition){
        double x = currentPosition.getX() ;
        double y = currentPosition.getY() ;
        return ( terrain.getHeight( new Vector2D( x + terrain.getStepSize() , y ) ) - terrain.getHeight( currentPosition ) ) / terrain.getStepSize() ;
    }
    public double getSlopeY( Vector2D currentPosition){
        double x = currentPosition.getX() ;
        double y = currentPosition.getY() ;
        return ( terrain.getHeight( new Vector2D( x , y + terrain.getStepSize() ) ) - terrain.getHeight( currentPosition ) ) / terrain.getStepSize() ;
    }


    public double getKineticFrictionCoefficient() {
        return kineticFrictionCoefficient;
    }

    public double getStaticFrictionCoefficient() {
        return staticFrictionCoefficient;
    }
}
