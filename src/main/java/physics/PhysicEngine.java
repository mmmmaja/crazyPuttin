package physics;

import objects.FileReader;
import objects.GameObject;
import objects.TerrainGenerator;

public class PhysicEngine extends Solver {


	//TODO terrain might be changed.
	//Terrain terrain = new Terrain();
	private final double MAX_SPEED = 5 ;
	private final double g =  9.81;

	private final double STEP = 0.02; // 1/60

	public PhysicEngine(){}


	public Vector2D calculateAcceleration(GameObject gameObject) {
		double mu_K = TerrainGenerator.getKineticFrictionCoefficient(gameObject.getPosition());
		double vX = gameObject.getVelocity().getX();
		double vY = gameObject.getVelocity().getY();

		double partial_x = TerrainGenerator.getSlopeX(gameObject.getPosition());
		double partial_y = TerrainGenerator.getSlopeY(gameObject.getPosition());

		Vector2D partials = new Vector2D(partial_x, partial_y);
		if (gameObject.isMoving()) {
			double aX = (- g * partial_x) - (mu_K * g * vX / gameObject.getVelocity().getMagnitude());
			double aY = (- g * partial_y) - (mu_K * g * vY / gameObject.getVelocity().getMagnitude());
			return new Vector2D(aX, aY);

		} else if ( gameObject.willMove()) {
			double aX = -mu_K * g * partial_x / partials.getMagnitude();
			double aY = -mu_K * g * partial_y / partials.getMagnitude();

			return new Vector2D(aX, aY);
		} else {
			return new Vector2D(0, 0);

		}
	}
	public double getSTEP() { return STEP;}
}
