package physics;

import objects.GameObject;

public abstract class Solver {

	public void nextStep(GameObject gameObject){};
	public double average(double[] coefficients , double[] k , double denominator){

		double sum = 0;
		for (int i = 0; i < k.length ; i++) {
			sum+= coefficients[i] * k[i];
		}
		return sum / denominator;
	}
}

