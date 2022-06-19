package physics;


import java.util.Arrays;


public class RK4 extends Solver {


	/**
	 * @param position of the ball
	 * @param velocity of the ball
	 * @param H step
	 * @return array of new position and velocity for the ball
	 */
	public Vector2D[] calculateNext(Vector2D position , Vector2D velocity , double H) {

		double[] a = { 1/2.d , 1/2.d ,1 };
		double[] b = { 1/2.d , 1/2.d ,1 };
		double[] c = { 1.d , 2.d , 2.d, 1.d};
		double denominator = Arrays.stream(c).sum();

		Vector2D k1a = PHYSICS.calculateAcceleration(position, velocity);
		Vector2D k2V = new Vector2D(velocity.getX() + a[0] * H * k1a.getX() , velocity.getY() + b[0] * H * k1a.getY());
		Vector2D k2p = new Vector2D(position.getX() + a[0] * H * velocity.getX() , position.getY() + b[0] * H * velocity.getY() ) ;
		Vector2D k2a = PHYSICS.calculateAcceleration( k2p, k2V );

		Vector2D k3V = new Vector2D(velocity.getX() + a[1] * H * k2a.getX() , velocity.getY() + b[1] * H * k2a.getY());
		Vector2D k3p = new Vector2D(position.getX() + a[1] * H * k2V.getX() , position.getY() + b[1] * H * k2V.getY() ) ;
		Vector2D k3a = PHYSICS.calculateAcceleration( k3p, k3V );

		Vector2D k4V = new Vector2D(velocity.getX() + a[2] * H * k3a.getX() , velocity.getY() + b[2] * H * k3a.getY());
		Vector2D k4p = new Vector2D(position.getX() + a[2] * H * k3V.getX() , position.getY() + b[2] * H * k3V.getY() ) ;
		Vector2D k4a = PHYSICS.calculateAcceleration( k4p,k4V);

		double x = position.getX() + H*average(c , new double[]{ velocity.getX() ,k2V.getX() , k3V.getX() ,k4V.getX()} , denominator) ;
		double y = position.getY() + H*average(c , new double[]{ velocity.getY() ,k2V.getY() , k3V.getY() ,k4V.getY()} , denominator) ;
		double vX =velocity.getX() + H*average(c , new double[]{ k1a.getX() ,k2a.getX() , k3a.getX() ,k4a.getX()} , denominator) ;
		double vY =velocity.getY() + H*average(c , new double[]{ k1a.getY() ,k2a.getY() , k3a.getY() ,k4a.getY()} , denominator) ;

		Vector2D newPosition = new Vector2D(x , y);
		Vector2D newVelocity = new Vector2D(vX,vY);

		return new Vector2D[]{ newPosition , newVelocity };
	}


}
