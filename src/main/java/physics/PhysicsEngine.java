package physics;

import Main.Main;
import objects.GameObject;
import objects.Obstacle;
import objects.TerrainGenerator;
import objects.Tree;

import java.util.ArrayList;


public class PhysicsEngine {

	private static final double g = 9.81;
	public static double STEP = 0.016;

	// stopping condition for the ball
	private static final double STOP = STEP;


	/**
	 * @param position of the ball
	 * @param velocity of the ball
	 * @return acceleration of the ball
	 */
	public Vector2D calculateAcceleration(Vector2D position , Vector2D velocity) {

		double mu_K = TerrainGenerator.getKineticFrictionCoefficient(position);
		double mu_S = TerrainGenerator.getStaticFrictionCoefficient(position);

		double vX = velocity.getX();
		double vY = velocity.getY();

		double partialX = TerrainGenerator.getSlopeX(position);
		double partialY = TerrainGenerator.getSlopeY(position);
		Vector2D partials = new Vector2D(partialX, partialY);

		double slope = (Math.sqrt(Math.pow(partialX, 2) + Math.pow(partialY, 2)));


		if ( velocity.getMagnitude() > STOP ) {

			double aX = - g * partialX - mu_K * g * vX / velocity.getMagnitude();
			double aY = - g * partialY - mu_K * g * vY / velocity.getMagnitude();
			return new Vector2D(aX, aY);

		}

		else if ( slope > mu_S) {

			double aX = -mu_K * g * partialX / partials.getMagnitude();
			double aY = -mu_K * g * partialY / partials.getMagnitude();

			return new Vector2D(aX, aY);
		}
		else {
			return new Vector2D(-vX/STEP, -vY/STEP);
		}
	}

	/**
	 *
	 * @param gameObject from the universe
	 * @return array of bisection point and reflectedVelocity
	 */
	public Vector2D[] getCollisionCoordinates(GameObject gameObject) {

		double currPosX = gameObject.getPosition().getX();
		double currPosY = gameObject.getPosition().getY();
		double rBall = Main.getUniverse().getBall().getRADIUS() * 2 ;

		// check all TREES in universe
		ArrayList<Tree> trees = Main.getUniverse().getTrees();
		double errorTolerance = 0.01;
		for (Tree tree : trees) {
			double treeXPos = tree.getPosition().getX();
			double treeYPos = tree.getPosition().getY();
			double d = tree.getCylinder().getRadius() * 2;

			// when ball ends up in tree
			if ((new Vector2D(currPosX - treeXPos, currPosY - treeYPos)).getMagnitude() < d) {
				double bisectBeginX = currPosX;
				double bisectBeginY = currPosY;
				double bisectEndX = gameObject.getPreviousPosition().getX();
				double bisectEndY = gameObject.getPreviousPosition().getY();
				double bisectMidX = (bisectBeginX + bisectEndX) / 2;
				double bisectMidY = (bisectBeginY + bisectEndY) / 2;

				// keep doing bisection until intersecting point is accurate enough
				while (new Vector2D(bisectBeginX - bisectEndX, bisectBeginY - bisectEndY).getMagnitude()> errorTolerance) {
					bisectMidX = (bisectBeginX + bisectEndX) / 2;
					bisectMidY = (bisectBeginY + bisectEndY) / 2;

					// middle point of bisection is on outline of tree
					if (new Vector2D(bisectMidX - treeXPos, bisectMidY - treeYPos).getMagnitude() == d) {
						Vector2D normal_vector = new Vector2D(-(treeXPos - bisectMidX), -(treeYPos - bisectMidY)).getUnitVector();
						Vector2D velocity = gameObject.getVelocity();
						velocity.reverseVector();
						Vector2D reflectedVelocity = velocity.reflectAroundVector(normal_vector);

						return new Vector2D[]{new Vector2D(bisectMidX, bisectMidY), reflectedVelocity};

					}
					// middle point of bisection is inside tree
					else if (new Vector2D(bisectMidX - treeXPos, bisectMidY - treeYPos).getMagnitude() < d) {
						bisectBeginX = bisectMidX;
						bisectBeginY = bisectMidY;
					}
					// middle point of bisection is outside tree
					else {
						bisectEndX = bisectMidX;
						bisectEndY = bisectMidY;
					}
				}

				Vector2D normal_vector = new Vector2D(-(treeXPos - bisectMidX), -(treeYPos - bisectMidY)).getUnitVector();
				Vector2D velocity = gameObject.getVelocity();
				velocity.reverseVector();
				Vector2D reflectedVelocity = velocity.reflectAroundVector(normal_vector);

				return new Vector2D[] {
						new Vector2D(bisectMidX, bisectMidY),
						reflectedVelocity};
			}
		}

		// check all the OBSTACLES
		ArrayList<Obstacle> obstacles = Main.getUniverse().getObstacles();

		for (Obstacle obstacle : obstacles) {

			double obstXPos = obstacle.getPosition().getX();
			double obstYPos = obstacle.getPosition().getY();

			// assuming that obstacle is square (length = width = height)
			double obstDim = obstacle.getDimension();

			// when ball ends up in between boundaries of obstacle
			if ((currPosX <= obstXPos+obstDim/2+rBall && currPosX >= obstXPos-obstDim/2-rBall) &&
					(currPosY <= obstYPos+obstDim/2+rBall && currPosY >= obstYPos-obstDim/2-rBall)) {

				double bisectBeginX = currPosX;
				double bisectBeginY = currPosY;
				double bisectEndX = gameObject.getPreviousPosition().getX();
				double bisectEndY = gameObject.getPreviousPosition().getY();
				double bisectMidX = (bisectBeginX + bisectEndX) / 2;
				double bisectMidY = (bisectBeginY + bisectEndY) / 2;

				// keep doing bisection until intersecting point is accurate enough
				while (new Vector2D(bisectBeginX - bisectEndX, bisectBeginY - bisectEndY).getMagnitude() > errorTolerance) {
					bisectMidX = (bisectBeginX + bisectEndX) / 2;
					bisectMidY = (bisectBeginY + bisectEndY) / 2;

					// middle point of bisection is on outline of tree
					if (
							(bisectMidX == obstXPos+obstDim/2+rBall || bisectMidX == obstXPos-obstDim/2-rBall) ||
							(bisectMidY == obstYPos+obstDim/2+rBall || bisectMidY == obstYPos-obstDim/2-rBall))
					{

//
						Vector2D normal_vector = new Vector2D(-(obstXPos - bisectMidX) , -(obstYPos - bisectMidY)).convertToNormalVectorObstacles();
						Vector2D velocity = gameObject.getVelocity();
						velocity.reverseVector();
						Vector2D reflectedVelocity = velocity.reflectAroundVector(normal_vector);

						return new Vector2D[]{new Vector2D(bisectMidX, bisectMidY), reflectedVelocity};

					}

					// middle point of bisection is inside tree
					else if (
							(bisectMidX < obstXPos+obstDim/2+rBall && bisectMidX > obstXPos-obstDim/2-rBall) &&
							(bisectMidY < obstYPos+obstDim/2+rBall && bisectMidY > obstYPos-obstDim/2-rBall))
					{
						bisectBeginX = bisectMidX;
						bisectBeginY = bisectMidY;
					}
					// middle point of bisection is outside tree
					else {
						bisectEndX = bisectMidX;
						bisectEndY = bisectMidY;
					}
				}

				Vector2D normal_vector = new Vector2D(-(obstXPos - bisectMidX) , -(obstYPos - bisectMidY)).convertToNormalVectorObstacles().getUnitVector();
				Vector2D velocity = gameObject.getVelocity();
				velocity.reverseVector();
				Vector2D reflectedVelocity = velocity.reflectAroundVector(normal_vector);

				return new Vector2D[]{
						new Vector2D(bisectMidX, bisectMidY),
						reflectedVelocity
				};

			}
		}
		return null;
	}


	public double getSTEP() {
		return STEP;
	}
	public double getSTOP() { return STOP; }
    public void setSTEP(double step) {
		STEP = step;
    }
}
