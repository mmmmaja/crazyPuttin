package physics;

public interface Vector {
	public double getX();


	public double getY();

	public void setX(double x);

	public void setY(double y);

	public double getMagnitude();

	public Vector getUnitVector();

	public Vector reverseUnitVector();

	public Vector reverseVector();


}
