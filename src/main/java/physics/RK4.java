package physics;

import objects.Ball;
import objects.GameObject;
import objects.TerrainGenerator;

import java.util.Arrays;


public class RK4 extends Solver {

	public final PhysicsEngine PHYSICS = new PhysicsEngine();
	public final double H = PHYSICS.getSTEP();
	@Override
	public void nextStep(GameObject gameObject) {
		GameObject imaginary = new Ball( gameObject.getPosition()) ;
		imaginary.setVelocity(gameObject.getVelocity());

		Vector2D[] next = calculateNext(gameObject.getPosition() , gameObject.getVelocity(), H );
		Vector2D nextPosition = next[0];
		Vector2D nextVelocity = next[1];


		if (TerrainGenerator.getHeightFromFile(nextPosition) >= 0 ) {
			gameObject.setPreviousPosition(gameObject.getPosition());
			gameObject.setState(nextPosition,nextVelocity);
		}
		else {
			gameObject.setState(gameObject.getPreviousPosition(),new Vector2D(0,0));
			gameObject.setWillMove(false);
		}
	}

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

		double x =  average(c , new double[]{ k1p.getX() ,k2p.getX() , k3p.getX() ,k4p.getX()} , denominator) ;
		double y =  average(c , new double[]{ k1p.getY() ,k2p.getY() , k3p.getY() ,k4p.getY()} , denominator) ;
		double vX = average(c , new double[]{ k1V.getX() ,k2V.getX() , k3V.getX() ,k4V.getX()} , denominator) ;
		double vY = average(c , new double[]{ k1V.getY() ,k2V.getY() , k3V.getY() ,k4V.getY()} , denominator) ;

		Vector2D newPosition = new Vector2D(x , y);
		Vector2D newVelocity = new Vector2D(vX,vY);



		return new Vector2D[]{ newPosition , newVelocity };
		}

	public static void main(String[] args) {
//		double t =  4 / ( 2 * 1.9619999999955358);

		double t = ( 1 / (2.d * ( 9.81 + 0.1 * 10 * 9.81  ) ) ) * 10 ;
		System.out.println(t);
		double t_calc_Euler = Math.abs(t - 0.2553422100000067);
		double t_calc_rk2 = Math.abs( t - 0.25509187450000936);
		double t_calc_heuns = Math.abs( t -0.25475866150002197);
		double t_calc_rk4 = Math.abs(   t - 0.2547587949166761 );

		System.out.println( "Euler absolute error rate -> " + t_calc_Euler);
		System.out.println( "RK2   absolute error rate -> " + t_calc_rk2 );
		System.out.println( "Heuns absolute error rate -> " + t_calc_heuns );
		System.out.println( "RK4   absolute error rate -> " + t_calc_rk4 );

	}
}
