package physics;

public interface Vector {

	double getX();

	double getY();

	void setX(double x);

	void setY(double y);

	double getMagnitude();

	Vector getUnitVector();

	Vector reverseUnitVector();

	void reverseVector();

}
