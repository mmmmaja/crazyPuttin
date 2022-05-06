//package physics;
//
//import objects.Ball;
//import objects.GameObject;
//import objects.TerrainGenerator;
//
//import java.util.Arrays;
//
//public class Heuns3 extends Solver{
//	public final PhysicsEngine PHYSICS = new PhysicsEngine();
//	public final double H = PHYSICS.getSTEP();
//	@Override
//	public void nextStep(GameObject gameObject) {
//		GameObject imaginary = new Ball( gameObject.getPosition()) ;
//		imaginary.setVelocity(gameObject.getVelocity());
//		Vector2D[] next = calculateNext(gameObject.getPosition() , gameObject.getVelocity() , H );
//		Vector2D nextPosition = next[0];
//		Vector2D nextVelocity = next[1];
//
//
//		if (TerrainGenerator.getHeight(nextPosition) >= 0) {
//			if(PHYSICS.getCollisionCoordinates(gameObject) != null){
//				gameObject.setState(PHYSICS.getCollisionCoordinates(gameObject),
//						new Vector2D(-gameObject.getVelocity().getX(), -gameObject.getVelocity().getY()));
//			}
//			else {
//				gameObject.setPreviousPosition(gameObject.getPosition());
//				gameObject.setState(nextPosition,nextVelocity);
//			}
//		}
//		else {
//			gameObject.setState(gameObject.getPreviousPosition(),new Vector2D(0,0));
//			gameObject.setWillMove(false);
//		}
//	}
//
//
//	public Vector2D[] calculateNext(Vector2D position , Vector2D velocity , double H) {
//		double[] a = { 1/3.d , 2/3.d };
//		double[] b = { 1/3.d , 2/3.d };
//		double[] c = { 1.d , 0.d , 3.d};
//		double denominator = Arrays.stream(c).sum();
//
//		Vector2D k1p = position;
//		Vector2D k1V = velocity;
//		Vector2D k1a = PHYSICS.calculateAcceleration(position, velocity);
//
//		Vector2D k2V = new Vector2D(k1V.getX() + a[0] * H * k1a.getX() ,k1V.getY() + b[0] * H * k1a.getY());
//		Vector2D k2p = new Vector2D(k1p.getX() + a[0] * H * k1V.getX() ,k1p.getY() + b[0] * H * k1V.getY() ) ;
//		Vector2D k2a = PHYSICS.calculateAcceleration( k2p, k2V );
//
//		Vector2D k3V = new Vector2D(k1V.getX() + a[1] * H * k2a.getX() ,k1V.getY() + b[1] * H * k2a.getY() );
//		Vector2D k3p = new Vector2D(k1p.getX() + a[1] * H * k2V.getX() ,k1p.getY() + b[1] * H * k2V.getY() );
//
//
//
//		double x =  average(c , new double[]{ k1p.getX() ,k2p.getX() , k3p.getX() } , denominator) ;
//		double y =  average(c , new double[]{ k1p.getY() ,k2p.getY() , k3p.getY() } , denominator) ;
//		double vX = average(c , new double[]{ k1V.getX() ,k2V.getX() , k3V.getX() } , denominator) ;
//		double vY = average(c , new double[]{ k1V.getY() ,k2V.getY() , k3V.getY() } , denominator) ;
//
//		Vector2D newPosition = new Vector2D(x , y);
//		Vector2D newVelocity = new Vector2D(vX,vY);
//
//
//
//		return new Vector2D[]{ newPosition , newVelocity };
//	}
//}
