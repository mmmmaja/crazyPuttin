package physics;

import java.util.ArrayList;

public class AdamsBashforth3 extends Solver{
    private ArrayList<Vector2D> positions = new ArrayList<>();
    private ArrayList<Vector2D> velocities = new ArrayList<>();
    private ArrayList<Vector2D> accelerations = new ArrayList<>();
    private Solver bootstrap = new RK4();
    public AdamsBashforth3(){
        positions = new ArrayList<>();
        velocities = new ArrayList<>();
        accelerations = new ArrayList<>();
    }
    public Vector2D[] calculateNext(Vector2D position , Vector2D velocity , double H) {
//        if(velocities==null || accelerations==null )return new Vector2D[]{position,new Vector2D(0,0)};

        double[] c = {23, -16, 5};
        double denominator = 12.d;


        if (velocities.size() <= 3) {
            Vector2D[] next = bootstrap.calculateNext(position,velocity,H);
            positions.add(next[0]);
            velocities.add(next[1]);
            accelerations.add(PHYSICS.calculateAcceleration(next[0], next[1]));
            return next;
        } else {
//            System.out.println(velocities.size());
            int n = velocities.size() - 1;
            double prediction_x = positions.get(n).getX()  + H * average(c,new double[]{   velocities.get(n).getX(),    velocities.get(n - 1).getX()   ,   velocities.get(n - 2).getX()},denominator);
            double prediction_y = positions.get(n).getY()  + H * average(c,new double[]{   velocities.get(n).getY(),    velocities.get(n - 1).getY()   ,   velocities.get(n - 2).getY()},denominator);
            double prediction_vX= velocities.get(n).getX() + H * average(c,new double[]{accelerations.get(n).getX(), accelerations.get(n - 1).getX(),   accelerations.get(n - 2).getX()},denominator);
            double prediction_vY= velocities.get(n).getY() + H * average(c,new double[]{accelerations.get(n).getY(), accelerations.get(n - 1).getY(),   accelerations.get(n - 2).getY()},denominator);

            Vector2D predictPos = new Vector2D(prediction_x, prediction_y);
            Vector2D predictVel = new Vector2D(prediction_vX, prediction_vY);
            Vector2D predictAcc = PHYSICS.calculateAcceleration(predictPos,predictVel);

            velocities.add(predictVel);
            positions.add(predictPos);
            accelerations.add(predictAcc);

            velocities.remove(0);
            positions.remove(0);
            accelerations.remove(0);

            return new Vector2D[]{predictPos, predictVel};
        }
    }
}
