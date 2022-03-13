package physics;

public class Vector3D extends Vector2D{

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
		return this.z ;
	}

	public void setZ(double z){
		this.z = z ;
	}

	@Override
	public double getMagnitude() {
		return Math.sqrt( Math.pow(x,2)  + Math.pow(y,2) + Math.pow(z,2));
	}
}
