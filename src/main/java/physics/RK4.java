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

	public static void main(String[] args) {
		PhysicsEngine physicsEngine = new PhysicsEngine();


		Solver[] solvers = {new RK4() , new Heuns3() , new RK2() , new Euler()};

		ArrayList<Double> errors = new ArrayList<>();

		int[] iteration = { 2  , 4   , 5  , 10 , 20  , 50  , 100  , 200   , 500  , 1000 , 2000   };
		double[] steps =  { 0.5, 0.25, 0.2, 0.1, 0.05, 0.02, 0.01 , 0.005 , 0.002, 0.001, 0.0005 };

		for(int s = 0 ; s < solvers.length ; s++) {
			Solver solver = solvers[s];
			for (int i = 0; i < steps.length ; i++) {
				Vector2D vel = new Vector2D(2, 0);
				Vector2D pos = new Vector2D(0, 0);
				physicsEngine.setSTEP(steps[i]);
				Vector2D[] v4 = solver.calculateNext(pos, vel, steps[i]);
				for (int j = 1; j < iteration[i]; j++) {
					pos = v4[0];
					vel = v4[1];
					v4 = solver.calculateNext(pos, vel, steps[i]);
				}
				errors.add(0, Math.abs(v4[0].getX()));
			}
			System.out.println(solver.getClass().getName().substring(8) +"="+errors+";");
			errors = new ArrayList<>();
		}

	}
}
