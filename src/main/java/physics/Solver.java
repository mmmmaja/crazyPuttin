package physics;

import objects.Ball;
import objects.GameObject;
import objects.TerrainGenerator;

public abstract class Solver {

	public final PhysicsEngine PHYSICS = new PhysicsEngine();
	public final double H = PHYSICS.getSTEP();

	public void nextStep(GameObject gameObject) {
		GameObject imaginary = new Ball( gameObject.getPosition()) ;
		imaginary.setVelocity(gameObject.getVelocity());

		Vector2D[] next = calculateNext(gameObject.getPosition() , gameObject.getVelocity(), H );
		Vector2D nextPosition = next[0];
		Vector2D nextVelocity = next[1];


		if (TerrainGenerator.getHeight(nextPosition) >= 0 ) {

			if ( PHYSICS.getCollisionCoordinates(gameObject) != null){
				Vector2D[] collision_state = PHYSICS.getCollisionCoordinates(gameObject);

				gameObject.setState(collision_state[0] , collision_state[1]);
				next = calculateNext(gameObject.getPosition() , gameObject.getVelocity(), H );
				nextPosition = next[0];
				nextVelocity = next[1];
			}

			gameObject.setPreviousPosition(gameObject.getPosition());
			gameObject.setState(nextPosition,nextVelocity);

		}
		else {
			gameObject.setState(gameObject.getPreviousPosition(),new Vector2D(0,0));
			gameObject.setWillMove(false);
		}
	}


	public double average(double[] coefficients , double[] k , double denominator){

		double sum = 0;
		for (int i = 0; i < k.length ; i++) {
			sum+= coefficients[i] * k[i];
		}
		return sum / denominator;
	}

	public Vector2D[] calculateNext(Vector2D position , Vector2D velocity , double H) {
		return null;
	}

}

