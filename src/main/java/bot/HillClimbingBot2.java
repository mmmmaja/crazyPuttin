package bot;

import Main.Universe;
import physics.Vector2D;


/**
 * iterative algorithm: at each iteration we change the velocity and asses the change by the fitness value
 * look at step: what should be the value be?
 */
public class HillClimbingBot2 {

	private final Universe universe;
	private final Vector2D bestVelocity;
	private int shotCounter = 0;


	public HillClimbingBot2(Universe universe) {
		this.universe = universe;
		this.bestVelocity = climb();
	}

	private Vector2D climb() {
			double step = 0.01;
			RandomBot randomBot = new RandomBot(this.universe);
			randomBot.startRandomTests(1000);
			Vector2D bestRandomShot = randomBot.getBestVelocity();
			Vector2D velocity = bestRandomShot;
			double result = new TestShot(this.universe, bestRandomShot).getTestResult(Heuristics.allPositions);

			double[][] stepArray = {
					{step, 0},
					{-step, 0},
					{0, step},
					{0, -step},
			};
			boolean play = true;

			while (result != 0 && play) {
				play = false;
				this.shotCounter++;

				for (double[] stepCase : stepArray) {
					Vector2D testVelocity = new Vector2D(velocity.getX() + stepCase[0], velocity.getY() + stepCase[1]);
					TestShot testShot = new TestShot(this.universe , testVelocity);
					double testResult = testShot.getTestResult(Heuristics.allPositions);

					// target was reached: break all
					if (testResult == 0) {
						System.out.print("HIT");
						velocity = testVelocity;
						result = testResult;
						play = false;
						break;
					}
					// climb the hill
					else if (testResult < result) {
						velocity = testVelocity;
						result = testResult;
						play = true ;
					}
				}
			}
			return velocity;
	}

	public Vector2D getBestVelocity() {
		System.out.println("shot counter: " + this.shotCounter);
		return this.bestVelocity;
	}

	public int getShotCounter() {
		return this.shotCounter;
	}


}
