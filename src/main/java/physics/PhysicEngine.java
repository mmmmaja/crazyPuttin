package physics;

import objects.GameObject;
import objects.TerrainGenerator;

public class PhysicEngine extends Solver {

	private static final double MAX_SPEED = 5 ;
	private static final double g =  9.81;
	private static final double STEP = 0.008;


	public Vector2D calculateAcceleration(GameObject gameObject) {
		double mu_K = TerrainGenerator.getKineticFrictionCoefficient(gameObject.getPosition());
		double vX = gameObject.getVelocity().getX();
		double vY = gameObject.getVelocity().getY();

		double partialX = TerrainGenerator.getSlopeX(gameObject.getPosition());
		double partialY = TerrainGenerator.getSlopeY(gameObject.getPosition());

		Vector2D partials = new Vector2D(partialX, partialY);

		if (gameObject.isMoving()) {
			double aX = - g * partialX - mu_K * g * vX / gameObject.getVelocity().getMagnitude();
			double aY = - g * partialY - mu_K * g * vY / gameObject.getVelocity().getMagnitude();
			return new Vector2D(aX, aY);

		}
		else if ( gameObject.getWillMove()) {
			double aX = mu_K * g * partialX / partials.getMagnitude();
			double aY = mu_K * g * partialY / partials.getMagnitude();

			return new Vector2D(aX, aY);
		}
		else {
			System.out.println("stable");
			System.out.println(gameObject.getPosition());
			return new Vector2D(0, 0);

		}
	}


	public double getSTEP() {return STEP;}
	public double getMAX_SPEED(){ return MAX_SPEED;}

}
