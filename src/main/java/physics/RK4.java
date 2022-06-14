package physics;

import objects.Ball;
import objects.GameObject;
import objects.TerrainGenerator;

import java.util.ArrayList;
import java.util.Arrays;


public class RK4 extends Solver {



	public Vector2D[] calculateNext(Vector2D position , Vector2D velocity , double H) {

		double[] a = { 1/2.d , 1/2.d ,1 };
		double[] b = { 1/2.d , 1/2.d ,1 };
		double[] c = { 1.d , 2.d , 2.d, 1.d};
		double denominator = Arrays.stream(c).sum();

		Vector2D k1p = position;
		Vector2D k1V = velocity;
		Vector2D k1a = PHYSICS.calculateAcceleration(position, velocity);

		Vector2D k2V = new Vector2D(k1V.getX() + a[0] * H * k1a.getX() ,k1V.getY() + b[0] * H * k1a.getY());
		Vector2D k2p = new Vector2D(k1p.getX() + a[0] * H * k1V.getX() ,k1p.getY() + b[0] * H * k1V.getY() ) ;
		Vector2D k2a = PHYSICS.calculateAcceleration( k2p, k2V );

		Vector2D k3V = new Vector2D(k1V.getX() + a[1] * H * k2a.getX() ,k1V.getY() + b[1] * H * k2a.getY());
		Vector2D k3p = new Vector2D(k1p.getX() + a[1] * H * k2V.getX() ,k1p.getY() + b[1] * H * k2V.getY() ) ;
		Vector2D k3a = PHYSICS.calculateAcceleration( k3p, k3V );

		Vector2D k4V = new Vector2D(k1V.getX() + a[2] * H * k3a.getX() ,k1V.getY() + b[2] * H * k3a.getY());
		Vector2D k4p = new Vector2D(k1p.getX() + a[2] * H * k3V.getX() ,k1p.getY() + b[2] * H * k3V.getY() ) ;
		Vector2D k4a = PHYSICS.calculateAcceleration( k4p,k4V);

		double x = position.getX() + H*average(c , new double[]{ k1V.getX() ,k2V.getX() , k3V.getX() ,k4V.getX()} , denominator) ;
		double y = position.getY() + H*average(c , new double[]{ k1V.getY() ,k2V.getY() , k3V.getY() ,k4V.getY()} , denominator) ;
		double vX =velocity.getX() + H*average(c , new double[]{ k1a.getX() ,k2a.getX() , k3a.getX() ,k4a.getX()} , denominator) ;
		double vY =velocity.getY() + H*average(c , new double[]{ k1a.getY() ,k2a.getY() , k3a.getY() ,k4a.getY()} , denominator) ;

		Vector2D newPosition = new Vector2D(x , y);
		Vector2D newVelocity = new Vector2D(vX,vY);

		return new Vector2D[]{ newPosition , newVelocity };
	}


}
