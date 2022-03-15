package physics;

import objects.Ball;
import objects.GameObject;
import objects.Terrain;
import objects.TerrainGenerator;

public class PhysicEngine {


	//TODO terrain might be changed.
	//Terrain terrain = new Terrain();
	private final double MAX_SPEED = 5 ;
	private final double g =  9.81;
	private final double MASS = 0.0459;
	private double mu_K = 0.06;
	private double mu_S = 0.15;
	private final double STEP = 0.01667; // 1/60

	public PhysicEngine(){}


	public Vector2D calculateAcceleration(GameObject gameObject){
		double vX = gameObject.getVelocity().getX();
		double vY = gameObject.getVelocity().getY();

		double partial_x = TerrainGenerator.getSlopeX(gameObject.getPosition());
		double partial_y = TerrainGenerator.getSlopeY(gameObject.getPosition());

		Vector2D partials = new Vector2D(partial_x , partial_y);

//		if(!gameObject.isMoving() && gameObject.isOnSlope() && gameObject.willMove() ){
//			double aX = mu_K * g * partial_x / partials.getMagnitude();
//			double aY = mu_K * g * partial_y / partials.getMagnitude();
//			return new Vector2D( aX , aY);
//		}
		double aX = (-1 * g * partial_x) -  (mu_K * g * vX / gameObject.getVelocity().getMagnitude());
		double aY = (-1 * g * partial_y) -  (mu_K * g * vY / gameObject.getVelocity().getMagnitude());

		return new Vector2D( aX , aY );
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



	public double getG() {return g;}
	public double getMASS() {return MASS;}
	public double getSTEP() { return STEP;}
	public double getMAX_SPEED(){ return MAX_SPEED; }

	public double getMu_K() {return mu_K;}
	public double getMu_S() {return mu_S;}

	public void setMu_K( double mu_K){ this.mu_K = mu_K ; }
	public void setMu_S( double mu_S){ this.mu_S = mu_S ; }

}
