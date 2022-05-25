package physics;

import objects.Ball;
import objects.GameObject;
import objects.TerrainGenerator;

import java.util.Arrays;


public class RK2 extends Solver {

	public Vector2D[] calculateNext(Vector2D position , Vector2D velocity , double H) {
		double[] a = { 2/3.d };
		double[] b = { 2/3.d};
		double[] c = { 1.d , 3.d };
		double denominator = Arrays.stream(c).sum();
		Vector2D k1p = position;
		Vector2D k1V = velocity;
		Vector2D k1a = PHYSICS.calculateAcceleration(position, velocity);

		Vector2D k2V = new Vector2D(k1V.getX() + a[0] * H * k1a.getX() ,k1V.getY() + b[0] * H * k1a.getY());
		Vector2D k2p = new Vector2D(k1p.getX() + a[0] * H * k1V.getX() ,k1p.getY() + b[0] * H * k1V.getY() ) ;
		Vector2D k2a = PHYSICS.calculateAcceleration( k2p, k2V );




		double x = position.getX() + H*average(c , new double[]{ k1V.getX() ,k2V.getX() } , denominator) ;
		double y = position.getY() + H*average(c , new double[]{ k1V.getY() ,k2V.getY() } , denominator) ;
		double vX =velocity.getX() + H*average(c , new double[]{ k1a.getX() ,k2a.getX() } , denominator) ;
		double vY =velocity.getY() + H*average(c , new double[]{ k1a.getY() ,k2a.getY() } , denominator) ;



		Vector2D newPosition = new Vector2D(x , y);
		Vector2D newVelocity = new Vector2D(vX,vY);

		double hL_x = newPosition.getX()/position.getX() - 1 ;
		double hL_y = newPosition.getX()/position.getX() - 1 ;

		if (hL_x <= -2 || hL_y <= -2)
			System.out.println("UNSTABLE !!!!!!!!!!!!!!!");

		return new Vector2D[]{ newPosition , newVelocity };
	}


}
