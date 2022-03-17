package physics;

import objects.GameObject;
import objects.TerrainGenerator;

public class PhysicEngine extends Solver {

	private final double MAX_SPEED = 5 ;
	private final double g =  9.81;

	private final double STEP = 0.008;

	public PhysicEngine(){}


	public Vector2D calculateAcceleration(GameObject gameObject) {
		double mu_K = TerrainGenerator.getKineticFrictionCoefficient(gameObject.getPosition());
		double vX = gameObject.getVelocity().getX();
		double vY = gameObject.getVelocity().getY();

		double partial_x = TerrainGenerator.getSlopeX(gameObject.getPosition());
		double partial_y = TerrainGenerator.getSlopeY(gameObject.getPosition());

		Vector2D partials = new Vector2D(partial_x, partial_y);
		if (gameObject.isMoving()) {
			double aX = - g * partial_x - mu_K * g * vX / gameObject.getVelocity().getMagnitude();
			double aY = - g * partial_y - mu_K * g * vY / gameObject.getVelocity().getMagnitude();
			return new Vector2D(aX, aY);

		} else if ( gameObject.getWillMove()) {
			double aX = mu_K * g * partial_x / partials.getMagnitude();
			double aY = mu_K * g * partial_y / partials.getMagnitude();

			return new Vector2D(aX, aY);
		} else {
			System.out.println("stable");
			System.out.println(gameObject.getPosition());
			return new Vector2D(0, 0);

		}
	}

//	public Vector gravitation_force( GameObject gameObject ){
//		double m = gameObject.getMass();
//
//		double dHdX = TerrainGenerator.getSlopeX( gameObject.getPosition() );
//		double dHdY = TerrainGenerator.getSlopeY( gameObject.getPosition() );
//
//		return new Vector2D(  g * dHdX ,  g * dHdY ).reverseVector();
//	}
//	public Vector friction_force(GameObject gameObject) {
//		double m = gameObject.getMass();
//
//		double x = gameObject.getPosition().getX();
//		double y = gameObject.getPosition().getY();
//
//		double vX = gameObject.getVelocity().getX();
//		double vY = gameObject.getVelocity().getY();
//
//		Vector2D v = gameObject.getVelocity();
//
//		double dHdX = TerrainGenerator.getSlopeX( gameObject.getPosition() );
//		double dHdY = TerrainGenerator.getSlopeY( gameObject.getPosition() );
//		Vector2D partials = new Vector2D( dHdX , dHdY);
//
//		Vector velocity_unit = gameObject.getVelocity().getUnitVector();
//		Vector acceleration_unit = calculateAcceleration(gameObject).getUnitVector();
//
//		if(!gameObject.isMoving()){
//			double fX = m * mu_K_grass * g * dHdX / partials.getMagnitude() ;
//			double fY = m * mu_K_grass * g * dHdY / partials.getMagnitude() ;
//			return new Vector2D( fX , fY);
//		}
//		double fX =  m * mu_K_grass * g * vX / v.getMagnitude() ;
//		double fY =  m * mu_K_grass * g * vY / v.getMagnitude() ;
//		return new Vector2D( fX , fY );
//	}
//	public Vector2D f ( GameObject gameObject){
//		double m = gameObject.getMass();
//
//		double x = gameObject.getPosition().getX();
//		double y = gameObject.getPosition().getY();
//
//		double vX = gameObject.getVelocity().getX();
//		double vY = gameObject.getVelocity().getY();
//
//		Vector2D v = gameObject.getVelocity();
//
//		double dHdX = TerrainGenerator.getSlopeX( gameObject.getPosition() );
//		double dHdY = TerrainGenerator.getSlopeY( gameObject.getPosition() );
//		Vector2D partials = new Vector2D( dHdX , dHdY);
//
//		Vector acceleration = calculateAcceleration(gameObject);
//
//		return new Vector2D( acceleration.getX() * m , acceleration.getY()*m );
//	}


	public double getSTEP() { return STEP;}
	public double getMAX_SPEED(){ return MAX_SPEED; }

}
