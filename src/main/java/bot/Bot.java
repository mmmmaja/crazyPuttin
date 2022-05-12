package bot;


import physics.Vector2D;

import java.util.ArrayList;

public interface Bot {

    int getTotalShotCounter();

    ArrayList<Vector2D> getVelocities();

    String toString();

}
