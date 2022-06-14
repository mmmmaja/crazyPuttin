package physics;

import java.util.ArrayList;
import java.util.Arrays;

public class AdamsBashforth3 extends Solver{
    private ArrayList<Vector2D> positions = new ArrayList<>();
    private ArrayList<Vector2D> velocities = new ArrayList<>();
    private ArrayList<Vector2D> accelerations = new ArrayList<>();
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
            Vector2D[] next = bootstrap(position,velocity,H);
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
    public Vector2D[] bootstrap(Vector2D position , Vector2D velocity , double H) {

        double[] a = { 1/2.d , 1/2.d ,1 };
        double[] b = { 1/2.d , 1/2.d ,1 };
        double[] c = { 1.d , 2.d , 2.d, 1.d};
        double denominator = Arrays.stream(c).sum();

        Vector2D k1p = position;
        Vector2D k1V = velocity;
        Vector2D k1a = PHYSICS.calculateAcceleration(position, velocity);

        Vector2D k2V = new Vector2D(k1V.getX() + a[0] * H * k1a.getX() ,k1V.getY() + b[0] * H * k1a.getY());
        Vector2D k2p = new Vector2D(k1p.getX() + a[0] * H * k1V.getX() ,k1p.getY() + b[0] * H * k1V.getY() ) ;
        Vector2D k2a = PHYSICS.calculateAcceleration( k2p, k2V );

        Vector2D k3V = new Vector2D(k1V.getX() + a[1] * H * k2a.getX() ,k1V.getY() + b[1] * H * k2a.getY());
        Vector2D k3p = new Vector2D(k1p.getX() + a[1] * H * k2V.getX() ,k1p.getY() + b[1] * H * k2V.getY() ) ;
        Vector2D k3a = PHYSICS.calculateAcceleration( k3p, k3V );

        Vector2D k4V = new Vector2D(k1V.getX() + a[2] * H * k3a.getX() ,k1V.getY() + b[2] * H * k3a.getY());
        Vector2D k4p = new Vector2D(k1p.getX() + a[2] * H * k3V.getX() ,k1p.getY() + b[2] * H * k3V.getY() ) ;
        Vector2D k4a = PHYSICS.calculateAcceleration( k4p,k4V);

        double x = position.getX() + H*average(c , new double[]{ k1V.getX() ,k2V.getX() , k3V.getX() ,k4V.getX()} , denominator) ;
        double y = position.getY() + H*average(c , new double[]{ k1V.getY() ,k2V.getY() , k3V.getY() ,k4V.getY()} , denominator) ;
        double vX =velocity.getX() + H*average(c , new double[]{ k1a.getX() ,k2a.getX() , k3a.getX() ,k4a.getX()} , denominator) ;
        double vY =velocity.getY() + H*average(c , new double[]{ k1a.getY() ,k2a.getY() , k3a.getY() ,k4a.getY()} , denominator) ;

        Vector2D newPosition = new Vector2D(x , y);
        Vector2D newVelocity = new Vector2D(vX,vY);

        return new Vector2D[]{ newPosition , newVelocity };
    }

}
