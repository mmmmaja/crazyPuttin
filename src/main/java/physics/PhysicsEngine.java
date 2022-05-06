package physics;

import Main.Main;
import objects.GameObject;
import objects.Obstacle;
import objects.TerrainGenerator;
import objects.Tree;

import java.util.ArrayList;

public class PhysicsEngine extends Solver {

	private static final double g = 9.81;
	private static final double STEP = 0.004;
	private static final double STOP = 0.005;

	private final double errorTolerance = 0.001;

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
			return new Vector2D(-vX/STEP, -vY/STEP);

		}
	}

	public Vector2D[] getCollisionCoordinates(GameObject gameObject){
		double currPosX = gameObject.getPosition().getX();
		double currPosY = gameObject.getPosition().getY();
		double rBall = Main.getUniverse().getBall().getRADIUS() * 2 ;

		// check all trees in universe
		ArrayList<Tree> trees = Main.getUniverse().getTrees();
		for (Tree tree : trees) {
			double treeXPos = tree.getPosition().getX();
			double treeYPos = tree.getPosition().getY();
			double r = tree.getCylinder().getRadius()*2;

			// when ball ends up in tree
			if ((new Vector2D(currPosX - treeXPos, currPosY - treeYPos)).getMagnitude() < r) {
				double bisecBeginX = currPosX;
				double bisecBeginY = currPosY;
				double bisecEndX = gameObject.getPreviousPosition().getX();
				double bisecEndY = gameObject.getPreviousPosition().getY();
				double bisecMidX = (bisecBeginX + bisecEndX) / 2;
				double bisecMidY = (bisecBeginY + bisecEndY) / 2;

				// keep doing bisection until intersecting point is accurate enough
				while (new Vector2D(bisecBeginX - bisecEndX, bisecBeginY - bisecEndY).getMagnitude()> errorTolerance) {
					bisecMidX = (bisecBeginX + bisecEndX) / 2;
					bisecMidY = (bisecBeginY + bisecEndY) / 2;
					// middle point of bisection is on outline of tree
					if (new Vector2D(bisecMidX - treeXPos, bisecMidY - treeYPos).getMagnitude() == r) {
						Vector2D normal_vector = new Vector2D(-(treeXPos - bisecMidX), -(treeYPos - bisecMidY)).getUnitVector();
						Vector2D velocity = gameObject.getVelocity();
						velocity.reverseVector();
						Vector2D reflectedVelocity = velocity.reflectAroundVector(normal_vector);

						return new Vector2D[]{new Vector2D(bisecMidX, bisecMidY), reflectedVelocity};
						// middle point of bisection is inside tree
					}else if (new Vector2D(bisecMidX - treeXPos, bisecMidY - treeYPos).getMagnitude() < r) {
						bisecBeginX = bisecMidX;
						bisecBeginY = bisecMidY;
					}
					// middle point of bisection is outside tree
					else {
						bisecEndX = bisecMidX;
						bisecEndY = bisecMidY;
					}
				}
				Vector2D normal_vector = new Vector2D(-(treeXPos - bisecMidX), -(treeYPos - bisecMidY)).getUnitVector();
				Vector2D velocity = gameObject.getVelocity();
				velocity.reverseVector();
				Vector2D reflectedVelocity = velocity.reflectAroundVector(normal_vector);

				return new Vector2D[]{new Vector2D(bisecMidX, bisecMidY), reflectedVelocity};

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
//				System.out.println("currposx: "+currPosX);
//				System.out.println("prevposx: "+gameObject.getPreviousPosition().getX());
//				System.out.println(obstXPos+(obstDim/2));
//				System.out.println(obstXPos-(obstDim/2));
//				System.out.println("currposy: "+currPosY);
//				System.out.println("prevposy: "+gameObject.getPreviousPosition().getY());
//				System.out.println(obstYPos+(obstDim/2));
//				System.out.println(obstYPos-(obstDim/2));

				double bisecBeginX = currPosX;
				double bisecBeginY = currPosY;
				double bisecEndX = gameObject.getPreviousPosition().getX();
				double bisecEndY = gameObject.getPreviousPosition().getY();
				double bisecMidX = (bisecBeginX + bisecEndX) / 2;
				double bisecMidY = (bisecBeginY + bisecEndY) / 2;

				// keep doing bisection until intersecting point is accurate enough
				while (new Vector2D(bisecBeginX - bisecEndX, bisecBeginY - bisecEndY).getMagnitude() > errorTolerance) {
					bisecMidX = (bisecBeginX + bisecEndX) / 2;
					bisecMidY = (bisecBeginY + bisecEndY) / 2;

					// middle point of bisection is on outline of tree
					if ((bisecMidX == obstXPos+obstDim/2+rBall || bisecMidX == obstXPos-obstDim/2-rBall) ||
							(bisecMidY == obstYPos+obstDim/2+rBall || bisecMidY == obstYPos-obstDim/2-rBall)) {

//						double vX = gameObject.getVelocity().getX();
//						double vY = gameObject.getVelocity().getY();
//
//						if( (bisecMidX < obstXPos+obstDim/2  && bisecMidX > obstXPos-obstDim/2)) {
//							System.out.println("X ---> " + (bisecMidX) + " " + (bisecMidX)  + " " +  (obstXPos-obstDim/2) + " " + (obstXPos+obstDim/2));
//							vY = -gameObject.getVelocity().getY();
//						}else{
//							System.out.println("Y ---> " + (bisecMidY) + " " + (bisecMidY)  + " " +  (obstYPos-obstDim/2) + " " + (obstYPos+obstDim/2));
//							vX = -gameObject.getVelocity().getX();
//
//						}
//						System.out.println(1);
						Vector2D normal_vector = new Vector2D(-(obstXPos - bisecMidX) , -(obstYPos - bisecMidY)).convertToNormalVectorObstacles();
						Vector2D velocity = gameObject.getVelocity();
						velocity.reverseVector();
						Vector2D reflectedVelocity = velocity.reflectAroundVector(normal_vector);

						return new Vector2D[]{new Vector2D(bisecMidX, bisecMidY), reflectedVelocity};

						// middle point of bisection is inside tree
					}else if ((bisecMidX < obstXPos+obstDim/2+rBall && bisecMidX > obstXPos-obstDim/2-rBall) &&
							(bisecMidY < obstYPos+obstDim/2+rBall && bisecMidY > obstYPos-obstDim/2-rBall)) {
						bisecBeginX = bisecMidX;
						bisecBeginY = bisecMidY;
					}
					// middle point of bisection is outside tree
					else {
						bisecEndX = bisecMidX;
						bisecEndY = bisecMidY;
					}
				}

//				double vX = gameObject.getVelocity().getX();
//				double vY = gameObject.getVelocity().getY();
//
//				if( (bisecMidX < obstXPos+obstDim/2  && bisecMidX > obstXPos-obstDim/2)) {
//					System.out.println("X ---> " + (bisecMidX) + " " + (bisecMidX)  + " " +  (obstXPos-obstDim/2) + " " + (obstXPos+obstDim/2));
//					vY = -gameObject.getVelocity().getY();
//				}else{
//					System.out.println("X ---> " + (bisecMidX) + " " +  (obstXPos-obstDim/2) + " " + (obstXPos+obstDim/2));
//					System.out.println("Y ---> " + (bisecMidY) + " " +  (obstYPos-obstDim/2) + " " + (obstYPos+obstDim/2));
//					vX = -gameObject.getVelocity().getX();
//				}
//				System.out.println(2);
//				System.out.println("X ---> " + (bisecMidX) + " " + (bisecMidX)  + " " +  (obstXPos-obstDim/2) + " " + (obstXPos+obstDim/2));


				Vector2D normal_vector = new Vector2D(-(obstXPos - bisecMidX) , -(obstYPos - bisecMidY)).convertToNormalVectorObstacles();
				Vector2D velocity = gameObject.getVelocity();
				velocity.reverseVector();
				Vector2D reflectedVelocity = velocity.reflectAroundVector(normal_vector);

				return new Vector2D[]{new Vector2D(bisecMidX, bisecMidY), reflectedVelocity};

			}
		}
		return null;
	}

	public double getSTEP() {
		return STEP;
	}
	public double getSTOP() { return STOP; }

}
