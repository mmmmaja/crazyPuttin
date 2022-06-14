package physics;

import Main.Main;
import objects.GameObject;
import objects.Obstacle;
import objects.TerrainGenerator;
import objects.Tree;

import java.util.ArrayList;

public class PhysicsEngine {

	private static final double g = 9.81;
	public static double STEP = 0.004;
	private static final double STOP = STEP;

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

	public boolean isProbableWaterCollision(GameObject gameObject) {
		double prevSlopeX = TerrainGenerator.getSlopeX(gameObject.getPreviousPosition());
		double prevSlopeY = TerrainGenerator.getSlopeY(gameObject.getPreviousPosition());
		double slopeX = TerrainGenerator.getSlopeX(gameObject.getPosition());
		double slopeY = TerrainGenerator.getSlopeY(gameObject.getPosition());

		return Math.signum(prevSlopeX) != Math.signum(slopeX) || Math.signum(prevSlopeY) != Math.signum(slopeY);
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
			double obstDim = obstacle.getDimension();

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

//				Vector2D reflectedVelocity = gameObject.getVelocity().multiply(-1);
				Vector2D normal_vector = new Vector2D(-(obstXPos - bisecMidX) , -(obstYPos - bisecMidY)).convertToNormalVectorObstacles().getUnitVector();
//				if(normal_vector.getMagnitude() == 1 ) {
					Vector2D velocity = gameObject.getVelocity();
					velocity.reverseVector();
					Vector2D reflectedVelocity = velocity.reflectAroundVector(normal_vector);
//				}

				return new Vector2D[]{new Vector2D(bisecMidX, bisecMidY), reflectedVelocity};

			}
		}
		return null;
	}
public Vector2D[] getCollisionCoordinates(Vector2D position ,Vector2D previousPosition, Vector2D velocity){
		double currPosX = position.getX();
		double currPosY = position.getY();
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
				double bisecEndX = previousPosition.getX();
				double bisecEndY = previousPosition.getY();
				double bisecMidX = (bisecBeginX + bisecEndX) / 2;
				double bisecMidY = (bisecBeginY + bisecEndY) / 2;

				// keep doing bisection until intersecting point is accurate enough
				while (new Vector2D(bisecBeginX - bisecEndX, bisecBeginY - bisecEndY).getMagnitude()> errorTolerance) {
					bisecMidX = (bisecBeginX + bisecEndX) / 2;
					bisecMidY = (bisecBeginY + bisecEndY) / 2;
					// middle point of bisection is on outline of tree
					if (new Vector2D(bisecMidX - treeXPos, bisecMidY - treeYPos).getMagnitude() == r) {
						Vector2D normal_vector = new Vector2D(-(treeXPos - bisecMidX), -(treeYPos - bisecMidY)).getUnitVector();
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
			double obstDim = obstacle.getDimension();

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
				double bisecEndX = previousPosition.getX();
				double bisecEndY = previousPosition.getY();
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

//				Vector2D reflectedVelocity = gameObject.getVelocity().multiply(-1);
				Vector2D normal_vector = new Vector2D(-(obstXPos - bisecMidX) , -(obstYPos - bisecMidY)).convertToNormalVectorObstacles().getUnitVector();
//				if(normal_vector.getMagnitude() == 1 )
					velocity.reverseVector();
					Vector2D reflectedVelocity = velocity.reflectAroundVector(normal_vector);
//				}

				return new Vector2D[]{new Vector2D(bisecMidX, bisecMidY), reflectedVelocity};

			}
		}
		return null;
	}

	public double getSTEP() {
		return STEP;
	}
	public double getSTOP() { return STOP; }


	public Vector2D getWaterCollision(GameObject gameObject) {
		double currPosX = gameObject.getPosition().getX();
		double currPosY = gameObject.getPosition().getY();
		double bisecBeginX = currPosX;
		double bisecBeginY = currPosY;
		double bisecEndX = gameObject.getPreviousPosition().getX();
		double bisecEndY = gameObject.getPreviousPosition().getY();
		double bisecMidX = (bisecBeginX + bisecEndX) / 2;
		double bisecMidY = (bisecBeginY + bisecEndY) / 2;

		// keep doing bisection until intersecting point is accurate enough
		while (new Vector2D(bisecBeginX - bisecEndX, bisecBeginY - bisecEndY)
				.getMagnitude() > errorTolerance) {
			bisecMidX = (bisecBeginX + bisecEndX) / 2;
			bisecMidY = (bisecBeginY + bisecEndY) / 2;
			// height at middle point is less than  (0- error tolerance)
			if (TerrainGenerator.getHeight(new Vector2D(bisecMidX, bisecMidY)) < -1.0 * errorTolerance) {
				bisecEndX = bisecMidX;
				bisecEndY = bisecMidY;
			}
			// height at middle point is more than
			else if (TerrainGenerator.getHeight(new Vector2D(bisecMidX, bisecMidY)) > errorTolerance) {
				bisecBeginX = bisecMidX;
				bisecBeginY = bisecMidY;
			}
			// height at middle point of bisection is within the error tolerance
			// from zero
			else {
				return new Vector2D(bisecMidX, bisecMidY);
			}
		}

		return null;

	}

    public void setSTEP(double step) {
		STEP = step;
    }
}
