package physics;

import objects.Ball;
import objects.GameObject;

public class Euler extends PhysicEngine implements Solver {


	public Euler(){

	}

	//FIXME it can return void instead of Vector2D

	public void nextStep(GameObject gameObject){

		Vector2D nextVelocity = calculateNextVelocity(gameObject);
		Vector2D nextPosition = calculateNextPosition(gameObject);

		gameObject.setPreviousPosition(gameObject.getPosition());
		gameObject.setVelocity(nextVelocity);
		gameObject.setPosition(nextPosition);

	}
	public Vector2D calculateNextVelocity(GameObject gameObject ){
		//v1 = v0 + h * a0
		Vector2D position = gameObject.getVelocity() ;
		Vector2D velocity = gameObject.getVelocity() ;
		Vector2D acceleration = calculateAcceleration(gameObject) ;

		double aX = acceleration.getX();
		double aY = acceleration.getY();

		double new_vX =  velocity.getX() + getSTEP() * aX ;
		double new_vY =  velocity.getY() + getSTEP() * aY ;

		return new Vector2D( new_vX , new_vY );
	}
	public Vector2D calculateNextPosition(GameObject gameObject){
		double x = gameObject.getPosition().getX();
		double y = gameObject.getPosition().getY();

		double vX = gameObject.getVelocity().getX();
		double vY = gameObject.getVelocity().getY();

		double new_X = x + getSTEP() * vX ;
		double new_Y = y + getSTEP() * vY ;

		return new Vector2D( new_X , new_Y);

	}
}
