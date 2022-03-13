package physics;

import objects.Ball;

public class Euler extends PhysicEngine{
	public Euler(){}

	//FIXME it can return void instead of Vector2D

	public void nextStep(Ball ball ){

		Vector2D nextVelocity = calculateNextVelocity(ball);
		Vector2D nextPosition = calculateNextPosition(ball);

		ball.setPreviousPosition(ball.getPosition());
		ball.setVelocity(nextVelocity);
		ball.setPosition(nextPosition);

	}
	public Vector2D calculateNextVelocity(Ball ball ){
		//v1 = v0 + h * a0
		Vector2D velocity = ball.getVelocity() ;
		Vector2D acceleration = calculateAcceleration(velocity) ;

		double aX = acceleration.getX();
		double aY = acceleration.getY();

		double new_vX =  velocity.getX() + getSTEP() * aX ;
		double new_vY =  velocity.getY() + getSTEP() * aY ;

		return new Vector2D( new_vX , new_vY );
	}
	public Vector2D calculateNextPosition(Ball ball){
		double x = ball.getPositionX() ;
		double y = ball.getPositionY() ;

		double vX = ball.getVelocity().getX();
		double vY = ball.getVelocity().getY();

		double new_X = x + getSTEP() * vX ;
		double new_Y = y + getSTEP() * vY ;

		return new Vector2D( new_X , new_Y);

	}
}
