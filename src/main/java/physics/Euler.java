package physics;

import objects.Ball;
import objects.GameObject;

public class Euler extends PhysicEngine {


	public Euler(){

	}

	@Override
	public void nextStep(GameObject gameObject ){

		gameObject.setPreviousPosition(gameObject.getPosition());
//		System.out.println("next pos : " + calculateNextPosition(gameObject));
		System.out.println("prev vel : " + calculateNextVelocity(gameObject));
		Vector2D next_pos = calculateNextPosition(gameObject);

		Vector2D next_vel = calculateNextVelocity(gameObject);
		System.out.println("next vel : " + calculateNextVelocity(gameObject));

		gameObject.setState( next_pos , next_vel );


//		System.out.println("------pos " + ( gameObject.getPosition().getX() - gameObject.getPreviousPosition().getX() ) );
//		System.out.println("vel " + gameObject.getVelocity());

	}
	public Vector2D calculateNextVelocity(GameObject gameObject ){
		//v1 = v0 + h * a0
		Vector2D velocity = gameObject.getVelocity() ;
		Vector2D acceleration = calculateAcceleration(gameObject) ;
//		System.out.println("accel: " + acceleration);
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

	@Override
	public Vector2D multiply(Vector2D vector1, Vector2D vector2) {
		return new Vector2D( vector1.getX() * vector2.getX() , vector1.getY() * vector2.getY());
	}

	public static void main(String[] args) {
		Euler e = new Euler();
		Ball ball = new Ball(new Vector2D(0,0) );
		ball.setVelocity( new Vector2D(1,0));

	}
}
