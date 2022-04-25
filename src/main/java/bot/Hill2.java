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

//	private Vector2D climb() {
//		// TODO step value is important, should I change it??
//			double step = 0.01;
//			RandomBot randomBot = new RandomBot(this.universe);
//			randomBot.startRandomTests(2000);
//			Vector2D best_random = randomBot.getBestVelocity();
//			Vector2D velocity = best_random;
//			double result = new TestShot(this.universe,best_random).getTestResult(Heuristics.allPositions);
//
//			// THIS ONE IS HILL CLIMBING. IT SEARCHES A BETTER SHOT THAN THE RANDOM BEST SHOT IN [-0.5 , +0.5] RANGE.
//			// EXACT HILL CLIMBING CAN BE IMPLEMENTED BY USING DYNAMIC PROGRAMMING . USE 2D ARRAY TO STORE EACH SHOT
//			// IT DEFINITELY WONT BE EFFICIENT IN TERMS OF SPACE COMPLEXITY.
//			for(double i = Math.max(-5, best_random.getX() - 0.5) ; i <= Math.min(5,best_random.getX() + 0.5)  ;i+=step) {
//				double sqrt = Math.sqrt(25 - Math.pow(i, 2));
//				for(double j = Math.max(-sqrt , best_random.getY() - 0.5); j <=  Math.min(sqrt,best_random.getX() + 0.5); j+= step){
//
//					Vector2D testVelocity = new Vector2D(i, j);
//					double testResult = new TestShot(this.universe,testVelocity).getTestResult(Heuristics.allPositions);
//					if (testResult == 0) {
//						velocity = testVelocity;
//						result = testResult;
//					}else if (testResult <= result) {
//						velocity = testVelocity;
//						result = testResult;
//					}
//
//				}
//			}
//
//
//			return velocity;
//	}

	private Vector2D climb() {
			double step = 0.01;
			RandomBot randomBot = new RandomBot(this.universe);
			randomBot.startRandomTests(2000);
			Vector2D best_random = randomBot.getBestVelocity();
			Vector2D velocity = best_random;
			double result = new TestShot(this.universe,best_random).getTestResult(Heuristics.allPositions);

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
				System.out.println("----");
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
						System.out.print("MISS");
						velocity = testVelocity;
						result = testResult;
						play = true ;
					}
				}
			}
			return velocity;
	}

	public Vector2D getBestVelocity() {
//		System.out.println(this.bestVelocity);
//		System.out.println("shot counter: " + this.shotCounter);
		return this.bestVelocity;
	}

	public int getShotCounter() {
		return this.shotCounter;
	}


}
