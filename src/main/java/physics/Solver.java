package physics;

import objects.Ball;
import objects.GameObject;

public interface Solver{
	public void nextStep(GameObject gameObject);
	public Vector2D calculateNextVelocity(GameObject gameObject);
	public Vector2D calculateNextPosition(GameObject gameObject);




	}

