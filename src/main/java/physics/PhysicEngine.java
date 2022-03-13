package physics;

import objects.Terrain;
import objects.TerrainGenerator;

public abstract class PhysicEngine {


	//TODO terrain might be changed.
	Terrain terrain = new Terrain();
	private final double g =  9.81;
	private final double MASS = 0.0459;
	private double mu_K = 0.06;
	private double mu_S = 0.15;
	private final double STEP = 0.01667; // 1/60

	public PhysicEngine(){}


	public Vector2D calculateAcceleration(Vector2D velocity){
		double vX = velocity.getX();
		double vY = velocity.getY();
		double sqrt = Math.sqrt( Math.pow( vX , 2 ) + Math.pow( vY , 2 ));
		return new Vector2D( -1 * g -  mu_K * g * vX/ sqrt , -1 * g - mu_K * vY / sqrt );
	}

	public Vector3D gravitation_force(){
		return new Vector3D( 0 , 0 , -1 * MASS * g);
	}
//	public Vector3D friction_force( Vector2D position , Vector2D velocity) {
//		double vX = velocity.getX();
//		double vY = velocity.getY();
//		double dHdX = terrain.getSlopeX(position);
//		double dHdY = terrain.getSlopeY(position);
//		return (-1 * mu_K * getMASS() *  )
//	}
	public double getSlopeX( Vector2D currentPosition){
		double x = currentPosition.getX() ;
		double y = currentPosition.getY() ;
		return ( TerrainGenerator.getHeight( new Vector2D( x + STEP , y ) ) - TerrainGenerator.getHeight( currentPosition ) ) / STEP ;
	}
	public double getSlopeY( Vector2D currentPosition){
		double x = currentPosition.getX() ;
		double y = currentPosition.getY() ;
		return ( TerrainGenerator.getHeight( new Vector2D( x , y + STEP ) ) - TerrainGenerator.getHeight( currentPosition ) ) / STEP ;
	}


	public double getG() {return g;}
	public double getMASS() {return MASS;}
	public double getSTEP() { return STEP;}

	public double getMu_K() {return mu_K;}
	public double getMu_S() {return mu_S;}

	public void setMu_K( double mu_K){ this.mu_K = mu_K ; }
	public void setMu_S( double mu_S){ this.mu_S = mu_S ; }

}
