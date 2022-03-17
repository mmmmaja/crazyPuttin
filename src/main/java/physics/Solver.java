package physics;

import objects.Ball;
import objects.GameObject;

public abstract class Solver{
	public void nextStep(GameObject gameObject){};
	public Vector2D calculateNextVelocity(GameObject gameObject){return null;};
	public Vector2D calculateNextPosition(GameObject gameObject){return null;};


	}

