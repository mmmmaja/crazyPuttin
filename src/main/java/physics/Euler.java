package physics;

import objects.Ball;

public class Euler {

	private PhysicEngine physicEngine = new PhysicEngine();

	public Euler(){

	}

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
		Vector2D position = ball.getVelocity() ;
		Vector2D velocity = ball.getVelocity() ;
		Vector2D acceleration = physicEngine.calculateAcceleration(ball , position ,  velocity) ;

		double aX = acceleration.getX();
		double aY = acceleration.getY();

		double new_vX =  velocity.getX() + physicEngine.getSTEP() * aX ;
		double new_vY =  velocity.getY() + physicEngine.getSTEP() * aY ;

		return new Vector2D( new_vX , new_vY );
	}
	public Vector2D calculateNextPosition(Ball ball){
		double x = ball.getPositionX() ;
		double y = ball.getPositionY() ;

		double vX = ball.getVelocity().getX();
		double vY = ball.getVelocity().getY();

		double new_X = x + physicEngine.getSTEP() * vX ;
		double new_Y = y + physicEngine.getSTEP() * vY ;

		return new Vector2D( new_X , new_Y);

	}
}
