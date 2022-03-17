package physics;

import objects.Ball;
import objects.GameObject;
import objects.TerrainGenerator;

public class Euler extends PhysicEngine {


	public Euler(){

	}

	@Override
	public void nextStep(GameObject gameObject ){

		Vector2D next_pos = calculateNextPosition(gameObject);
		Vector2D next_vel = calculateNextVelocity(gameObject);

		if (TerrainGenerator.getHeight(next_pos)>=0){
			gameObject.setPreviousPosition(gameObject.getPosition());
			gameObject.setState(next_pos,next_vel);
		}
		else{
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
	public Vector2D calculateNextPosition(GameObject gameObject){

		double vX = gameObject.getVelocity().getX();
		double vY = gameObject.getVelocity().getY();

		double new_X = gameObject.getPosition().getX() + getSTEP() * vX ;
		double new_Y = gameObject.getPosition().getY() + getSTEP() * vY ;
		return new Vector2D( new_X , new_Y);
	}


}
