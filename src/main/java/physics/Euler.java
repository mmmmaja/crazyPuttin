package physics;

import objects.GameObject;
import objects.TerrainGenerator;


public class Euler extends PhysicEngine {


	@Override
	public void nextStep(GameObject gameObject) {

		Vector2D nextPosition = calculateNextPosition(gameObject);
		Vector2D nextVelocity = calculateNextVelocity(gameObject);

		if (TerrainGenerator.getHeight(nextPosition) >= 0) {
			gameObject.setPreviousPosition(gameObject.getPosition());
			gameObject.setState(nextPosition,nextVelocity);
		}
		else {
			gameObject.setState(gameObject.getPreviousPosition(),new Vector2D(0,0));
			gameObject.setWillMove(false);
		}
	}

	public Vector2D calculateNextVelocity(GameObject gameObject ){
		//v1 = v0 + h * a0
		Vector2D velocity = gameObject.getVelocity() ;
		Vector2D acceleration = calculateAcceleration(gameObject) ;
		if(gameObject.isMoving() && velocity.getUnitVector().equals(acceleration.getUnitVector())){
			acceleration.reverseVector();
		}

		double aX = acceleration.getX();
		double aY = acceleration.getY();

		double new_vX =  velocity.getX() + getSTEP() * aX ;
		double new_vY =  velocity.getY() + getSTEP() * aY ;

		return new Vector2D( new_vX , new_vY );
	}

	/**
	 *
	 * @param gameObject of the game; for this phase: the ball
	 * @return next position of the given object
	 */
	public Vector2D calculateNextPosition(GameObject gameObject){

		double vX = gameObject.getVelocity().getX();
		double vY = gameObject.getVelocity().getY();

		double new_X = gameObject.getPosition().getX() + getSTEP() * vX;
		double new_Y = gameObject.getPosition().getY() + getSTEP() * vY;
		return new Vector2D(new_X, new_Y);
	}

}
