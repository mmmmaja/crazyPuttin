package physics;

import Main.Main;
import objects.GameObject;
import objects.Obstacle;
import objects.TerrainGenerator;
import objects.Tree;

import java.util.ArrayList;

public class PhysicsEngine extends Solver {

	private static final double g = 9.81;
	private static final double STEP = 0.01;
	private static final double STOP = 0.005;

	private final double errorTolerance = 0.01;

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


		if ( velocity.getMagnitude() > 0.001 ) {

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
			return new Vector2D(-vX/STEP, -vY/STEP);

		}
	}

	public Vector2D getCollisionCoordinates(GameObject gameObject){
		double currPosX = gameObject.getPosition().getX();
		double currPosY = gameObject.getPosition().getY();
		double rBall = Main.getUniverse().getBall().getRADIUS();

		// check all trees in universe
		ArrayList<Tree> trees = Main.getUniverse().getTrees();
		for (Tree tree : trees) {
			double treeXPos = tree.getPosition().getX();
			double treeYPos = tree.getPosition().getY();
			double r = tree.getCylinder().getRadius();

			// when ball ends up in tree
			if ((new Vector2D(currPosX - treeXPos, currPosY - treeYPos)).getMagnitude() < r) {
				double bisecBeginX = currPosX;
				double bisecBeginY = currPosY;
				double bisecEndX = gameObject.getPreviousPosition().getX();
				double bisecEndY = gameObject.getPreviousPosition().getY();
				double bisecMidX = (bisecBeginX + bisecEndX) / 2;
				double bisecMidY = (bisecBeginY + bisecEndY) / 2;

				// keep doing bisection until intersecting point is accurate enough
				while (new Vector2D(bisecBeginX - bisecEndX, bisecBeginY - bisecEndY)
						.getMagnitude()> errorTolerance) {
					bisecMidX = (bisecBeginX + bisecEndX) / 2;
					bisecMidY = (bisecBeginY + bisecEndY) / 2;
					// middle point of bisection is on outline of tree
					if (new Vector2D(bisecMidX - treeXPos, bisecMidY - treeYPos).getMagnitude() == r)
						return new Vector2D(bisecMidX, bisecMidY);
					// middle point of bisection is inside tree
					else if (new Vector2D(bisecMidX - treeXPos, bisecMidY - treeYPos).getMagnitude() < r) {
						bisecBeginX = bisecMidX;	bisecBeginY = bisecMidY;
					}
					// middle point of bisection is outside tree
					else {
						bisecEndX = bisecMidX;		bisecEndY = bisecMidY;
					}
				}
				return new Vector2D(bisecMidX, bisecMidY);
			}
		}

		ArrayList<Obstacle> obstacles = Main.getUniverse().getObstacles();
		for (Obstacle obstacle : obstacles) {

			double obstXPos = obstacle.getPosition().getX();
			double obstYPos = obstacle.getPosition().getY();
			// assuming that obstacle is square (length=width=height)
			double obstDim = obstacle.getDimension().getX();

			// when ball ends up in between boundaries of obstacle
			if ((currPosX <= obstXPos+obstDim/2+rBall && currPosX >= obstXPos-obstDim/2-rBall) &&
					(currPosY <= obstYPos+obstDim/2+rBall && currPosY >= obstYPos-obstDim/2-rBall)) {
				System.out.println("currposx: "+currPosX);
				System.out.println("prevposx: "+gameObject.getPreviousPosition().getX());
				System.out.println(obstXPos+(obstDim/2));
				System.out.println(obstXPos-(obstDim/2));
				System.out.println("currposy: "+currPosY);
				System.out.println("prevposy: "+gameObject.getPreviousPosition().getY());
				System.out.println(obstYPos+(obstDim/2));
				System.out.println(obstYPos-(obstDim/2));

				double bisecBeginX = currPosX;
				double bisecBeginY = currPosY;
				double bisecEndX = gameObject.getPreviousPosition().getX();
				double bisecEndY = gameObject.getPreviousPosition().getY();
				double bisecMidX = (bisecBeginX + bisecEndX) / 2;
				double bisecMidY = (bisecBeginY + bisecEndY) / 2;

				// keep doing bisection until intersecting point is accurate enough
				while (new Vector2D(bisecBeginX - bisecEndX, bisecBeginY - bisecEndY)
						.getMagnitude() > errorTolerance) {
					System.out.println(bisecBeginX - bisecEndX);
					System.out.println(bisecBeginY - bisecEndY);
					System.out.println("Length: " + new Vector2D(bisecBeginX - bisecEndX, bisecBeginY - bisecEndY)
							.getMagnitude());
					bisecMidX = (bisecBeginX + bisecEndX) / 2;
					bisecMidY = (bisecBeginY + bisecEndY) / 2;

					// middle point of bisection is on outline of tree
					if ((bisecMidX == obstXPos+obstDim/2+rBall || bisecMidX == obstXPos-obstDim/2-rBall) ||
							(bisecMidY == obstYPos+obstDim/2+rBall || bisecMidY == obstYPos-obstDim/2-rBall))
						return new Vector2D(bisecMidX, bisecMidY);
						// middle point of bisection is inside tree
					else if ((bisecMidX < obstXPos+obstDim/2+rBall && bisecMidX > obstXPos-obstDim/2-rBall) &&
							(bisecMidY < obstYPos+obstDim/2+rBall && bisecMidY > obstYPos-obstDim/2-63-rBall)) {
						bisecBeginX = bisecMidX;
						bisecBeginY = bisecMidY;
					}
					// middle point of bisection is outside tree
					else {
						bisecEndX = bisecMidX;
						bisecEndY = bisecMidY;
					}
				}
				System.out.println(new Vector2D(bisecMidX, bisecMidY));
				return new Vector2D(bisecMidX, bisecMidY);
			}
		}
		return null;
	}

	public double getSTEP() {
		return STEP;
	}
	public double getSTOP() { return STOP; }

}
