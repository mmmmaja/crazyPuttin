package physics;


public class Vector3D implements Vector {

	private double x;
	private double y;
	private double z;

	public Vector3D(double x, double y , double z ) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getZ(){
		return this.z;
	}

	public void setZ(double z){
		this.z = z;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public void setX(double x) {
		this.x = x ;
	}

	@Override
	public void setY(double y) {
		this.y = y ;
	}

	@Override
	public double getMagnitude() {
		return Math.sqrt( Math.pow(x,2)  + Math.pow(y,2) + Math.pow(z,2));
	}

	@Override
	public Vector getUnitVector() {
		return new Vector3D( x / getMagnitude()  , y / getMagnitude() , z / getMagnitude() );
	}

	@Override
	public Vector reverseUnitVector() {
		return new Vector3D( -1 * x / getMagnitude()  , -1 * y / getMagnitude() , -1 * z / getMagnitude() );

	}

	@Override
	public void reverseVector() {
		setX(-x);
		setY(-y);
		setZ(-z);
	}

}
