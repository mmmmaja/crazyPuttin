package physics;

import objects.Ball;
import objects.GameObject;

public class Euler extends PhysicEngine implements Solver {


	public Euler(){

	}

	//FIXME it can return void instead of Vector2D

	public void nextStep(GameObject gameObject ){

		gameObject.setPreviousPosition(gameObject.getPosition());
		gameObject.setState(calculateNextPosition(gameObject ) , calculateNextVelocity(gameObject ));


		System.out.println("------pos " + ( gameObject.getPosition().getX() - gameObject.getPreviousPosition().getX() ) );
		System.out.println("vel " + gameObject.getVelocity());

	}
	public Vector2D calculateNextVelocity(GameObject gameObject ){
		//v1 = v0 + h * a0
		Vector2D velocity = gameObject.getVelocity() ;
		Vector2D acceleration = calculateAcceleration(gameObject) ;
		System.out.println("AAAAAAAAAAAAAAAAAAAAACCCCCCCCCCCCCCCCCCC " + acceleration);
		double aX = acceleration.getX();
		double aY = acceleration.getY();

		double new_vX =  velocity.getX() + getSTEP() * aX ;
		double new_vY =  velocity.getY() + getSTEP() * aY ;

		return new Vector2D( new_vX , new_vY );
	}
	public Vector2D calculateNextPosition(GameObject gameObject){

		double vX = gameObject.getVelocity().getX();
		double vY = gameObject.getVelocity().getY();

		double new_X = gameObject.getPosition().getX() + getSTEP() * vX ;
		double new_Y = gameObject.getPosition().getY() + getSTEP() * vY ;

		Vector2D next_position = new Vector2D( new_X , new_Y);
		return next_position;
	}
}
