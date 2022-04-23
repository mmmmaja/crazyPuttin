package bot;

import Main.Universe;
import physics.Vector2D;

import java.util.ArrayList;


/**
 * iterative algorithm: at each iteration we change the velocity and asses the change by the fitness value
 * look at step: what should be the value be?
 */
public class Hill2 {

	private final Universe universe;
	private final Vector2D bestVelocity;
	private int shotCounter = 0;


	public Hill2(Universe universe) {
		this.universe = universe;
		this.bestVelocity = climb();
	}

	private Vector2D climb() {
		// TODO step value is important, should I change it??
			double step = 0.25;
			RandomBot randomBot = new RandomBot(this.universe);
			randomBot.startRandomTests(2000);
			Vector2D best_random = randomBot.getBestVelocity();
			Vector2D velocity = best_random;
			double result = new TestShot(this.universe,best_random).getTestResult(Heuristics.allPositions);


			for(double i = Math.max(-5, best_random.getX() - 1) ; i <= Math.min(5,best_random.getX() + 1)  ;i+=step) {
				double sqrt = Math.sqrt(25 - Math.pow(i, 2));
				for(double j = Math.max(-sqrt , best_random.getY() - 1); j <=  Math.min(sqrt,best_random.getX() + 1); j+= step){

					Vector2D testVelocity = new Vector2D(i, j);
					double testResult = new TestShot(this.universe,testVelocity).getTestResult(Heuristics.allPositions);
					if (testResult == 0) {
						velocity = testVelocity;
						result = testResult;
					}else if (testResult <= result) {
						velocity = testVelocity;
						result = testResult;
					}

				}
			}


			return velocity;
	}

	public Vector2D getBestVelocity() {
		System.out.println(this.bestVelocity);
		System.out.println("shot counter: " + this.shotCounter);
		return this.bestVelocity;
	}

	public int getShotCounter() {
		return this.shotCounter;
	}

	public static void main(String[] args) {
		Vector2D a = new Vector2D(5.448860629637605,4.395077753444171);
		System.out.println(new Vector2D(a.getUnitVector().getX()*5 , a.getUnitVector().getY()*5).getMagnitude());
		System.out.println(a.getUnitVector().getMagnitude());
		System.out.println(a.getMagnitude());
	}
}
