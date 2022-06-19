package physics;


import java.util.Arrays;


public class RK2 extends Solver {

	/**
	 * @param position of the ball
	 * @param velocity of the ball
	 * @param H step
	 * @return array of new position and velocity for the ball
	 */
	public Vector2D[] calculateNext(Vector2D position , Vector2D velocity , double H) {
		double[] a = { 2/3.d };
		double[] b = { 2/3.d};
		double[] c = { 1.d , 3.d };

		double denominator = Arrays.stream(c).sum();
		Vector2D k1a = PHYSICS.calculateAcceleration(position, velocity);
		Vector2D k2V = new Vector2D(velocity.getX() + a[0] * H * k1a.getX() , velocity.getY() + b[0] * H * k1a.getY());
		Vector2D k2p = new Vector2D(position.getX() + a[0] * H * velocity.getX() , position.getY() + b[0] * H * velocity.getY() ) ;
		Vector2D k2a = PHYSICS.calculateAcceleration( k2p, k2V );

		double x = position.getX() + H * average(c , new double[]{ velocity.getX() ,k2V.getX() } , denominator) ;
		double y = position.getY() + H * average(c , new double[]{ velocity.getY() ,k2V.getY() } , denominator) ;
		double vX =velocity.getX() + H * average(c , new double[]{ k1a.getX() ,k2a.getX() } , denominator) ;
		double vY =velocity.getY() + H * average(c , new double[]{ k1a.getY() ,k2a.getY() } , denominator) ;

		Vector2D newPosition = new Vector2D(x , y);
		Vector2D newVelocity = new Vector2D(vX,vY);


		return new Vector2D[] {
				newPosition ,
				newVelocity
		};
	}


}
