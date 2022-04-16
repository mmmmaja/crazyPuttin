package physics;

import objects.GameObject;
import objects.TerrainGenerator;

public class PhysicsEngine extends Solver {

	private static final double g = 9.81;
	private static final double STEP = 0.032;
	private static final double STOP = 0.005;



	public Vector2D calculateAcceleration(GameObject gameObject) {
		double mu_K = TerrainGenerator.getKineticFrictionCoefficient(gameObject.getPosition());
		double vX = gameObject.getVelocity().getX();
		double vY = gameObject.getVelocity().getY();

		double partialX = TerrainGenerator.getSlopeX(gameObject.getPosition());
		double partialY = TerrainGenerator.getSlopeY(gameObject.getPosition());

		Vector2D partials = new Vector2D(partialX, partialY);

		if (gameObject.isMoving()) {
			double aX = -g * partialX - mu_K * g * vX / gameObject.getVelocity().getMagnitude();
			double aY = -g * partialY - mu_K * g * vY / gameObject.getVelocity().getMagnitude();
			return new Vector2D(aX, aY);

		} else if (gameObject.getWillMove()) {
			double aX = -mu_K * g * partialX / partials.getMagnitude();
			double aY = -mu_K * g * partialY / partials.getMagnitude();

			return new Vector2D(aX, aY);
		} else {
			return new Vector2D(0, 0);

		}
	}

	public Vector2D calculateAcceleration(Vector2D position , Vector2D velocity) {
		double mu_K = TerrainGenerator.getKineticFrictionCoefficient(position);
		double mu_S = TerrainGenerator.getStaticFrictionCoefficient(position);

		double vX = velocity.getX();
		double vY = velocity.getY();

		double partialX = TerrainGenerator.getSlopeX(position);
		double partialY = TerrainGenerator.getSlopeY(position);
		Vector2D partials = new Vector2D(partialX, partialY);

		double slope = (Math.sqrt(Math.pow(partialX, 2) + Math.pow(partialY, 2)));


		if ( velocity.getMagnitude() >0.001 ) {
			double aX = - g * partialX - mu_K * g * vX / velocity.getMagnitude();
			double aY = - g * partialY - mu_K * g * vY / velocity.getMagnitude();
			return new Vector2D(aX, aY);

		}
		else
		if ( slope > mu_S) {
			double aX = -mu_K * g * partialX / partials.getMagnitude();
			double aY = -mu_K * g * partialY / partials.getMagnitude();

			return new Vector2D(aX, aY);
		}
		else {
			System.out.println("stable");
			return new Vector2D(vX/STEP, vY/STEP);

		}
	}


	public double getSTEP() {
		return STEP;
	}
	public double getSTOP() { return STOP; }

}
