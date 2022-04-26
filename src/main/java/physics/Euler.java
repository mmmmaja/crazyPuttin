package physics;

import objects.GameObject;
import objects.TerrainGenerator;

import java.util.Arrays;
import java.util.Collections;


public class Euler extends Solver {

	public final PhysicsEngine PHYSICS = new PhysicsEngine();
	public final double H = PHYSICS.getSTEP();
	@Override
	public void nextStep(GameObject gameObject) {

		Vector2D[] next = calculateNextStep(gameObject.getPosition() , gameObject.getVelocity() , H );
		Vector2D nextPosition = next[0];
		Vector2D nextVelocity = next[1];

		if (TerrainGenerator.getHeight(nextPosition) >= 0) {
			if(PHYSICS.getCollisionCoordinates(gameObject) != null){
				gameObject.setState(PHYSICS.getCollisionCoordinates(gameObject),
						new Vector2D(-gameObject.getVelocity().getX(), -gameObject.getVelocity().getY()));
			}
			else {
				gameObject.setPreviousPosition(gameObject.getPosition());
				gameObject.setState(nextPosition,nextVelocity);
			}
		}
		else {
			gameObject.setState(gameObject.getPreviousPosition(),new Vector2D(0,0));
			gameObject.setWillMove(false);
		}
	}

	public Vector2D[] calculateNextStep(Vector2D position , Vector2D velocity , double H) {
		double[] a = { 1 };
		double[] b = { 1 };
		double[] c = { 0.d , 1.d };
		double denominator = Arrays.stream(c).sum();

		Vector2D k1p = position;
		Vector2D k1V = velocity;
		Vector2D k1a = PHYSICS.calculateAcceleration(position, velocity);

		Vector2D k2V = new Vector2D(k1V.getX() + a[0] * H * k1a.getX() ,k1V.getY() + b[0] * H * k1a.getY()); // change in speed ( a * Δh  = ΔV)
		Vector2D k2p = new Vector2D(k1p.getX() + a[0] * H * k1V.getX() ,k1p.getY() + b[0] * H * k1V.getY() ) ; // change in speed ( V * Δh  = Δx)

		double x =  average(c , new double[]{ k1p.getX() ,k2p.getX() } , denominator) ;
		double y =  average(c , new double[]{ k1p.getY() ,k2p.getY() } , denominator) ;
		double vX = average(c , new double[]{ k1V.getX() ,k2V.getX() } , denominator) ;
		double vY = average(c , new double[]{ k1V.getY() ,k2V.getY() } , denominator) ;

		Vector2D newPosition = new Vector2D(x , y);
		Vector2D newVelocity = new Vector2D(vX,vY);

//		if (newVelocity.equals(velocity))
//			newVelocity = new Vector2D(0,0);

		return new Vector2D[]{ newPosition , newVelocity };
	}

}
