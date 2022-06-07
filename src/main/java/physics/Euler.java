package physics;

import java.util.Arrays;


public class Euler extends Solver {

	public Vector2D[] calculateNext(Vector2D position , Vector2D velocity , double H) {
		double[] a = { 1 };
		double[] b = { 1 };
		double[] c = { 0.d , 1.d };
		double denominator = Arrays.stream(c).sum();

		Vector2D k1a = PHYSICS.calculateAcceleration(position, velocity);
		Vector2D k2V = new Vector2D(velocity.getX() + a[0] * H * k1a.getX() , velocity.getY() + b[0] * H * k1a.getY()); // change in speed ( a * Δh  = ΔV)
		Vector2D k2p = new Vector2D(position.getX() + a[0] * H * velocity.getX() , position.getY() + b[0] * H * velocity.getY() ) ; // change in speed ( V * Δh  = Δx)

		double x =  average(c , new double[]{ position.getX() ,k2p.getX() } , denominator) ;
		double y =  average(c , new double[]{ position.getY() ,k2p.getY() } , denominator) ;
		double vX = average(c , new double[]{ velocity.getX() ,k2V.getX() } , denominator) ;
		double vY = average(c , new double[]{ velocity.getY() ,k2V.getY() } , denominator) ;

		Vector2D newPosition = new Vector2D(x , y);
		Vector2D newVelocity = new Vector2D(vX,vY);

		return new Vector2D[]{ newPosition , newVelocity };
	}

}
