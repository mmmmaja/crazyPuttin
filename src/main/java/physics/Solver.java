package physics;

import objects.Ball;
import objects.GameObject;
import objects.TerrainGenerator;

import java.util.ArrayList;

public abstract class Solver {
	public ArrayList<Vector2D> positions = new ArrayList<>();
	public ArrayList<Vector2D> velocities = new ArrayList<>();
	public ArrayList<Vector2D> accelerations = new ArrayList<>();

	public PhysicsEngine PHYSICS = new PhysicsEngine();
	public double H = PHYSICS.getSTEP();

	public void nextStep(GameObject gameObject) {


		Vector2D[] next = calculateNext(gameObject.getPosition() , gameObject.getVelocity(), H );
		Vector2D nextPosition = next[0];
		Vector2D nextVelocity = next[1];


		if (TerrainGenerator.getHeight(nextPosition) >= 0 ) {

			if ( PHYSICS.getCollisionCoordinates(gameObject) != null){
				clearPreviousSteps();
				Vector2D[] collision_state = PHYSICS.getCollisionCoordinates(gameObject);
//				System.out.println(collision_state[1]);
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

	public void clearPreviousSteps(){
		velocities = new ArrayList<>();
		positions = new ArrayList<>();
		accelerations = new ArrayList<>() ;
	}

	public PhysicsEngine getPHYSICS() {
		return PHYSICS;
	}

	public void setPHYSICS(PhysicsEngine PHYSICS) {
		this.PHYSICS = PHYSICS;
	}
}

