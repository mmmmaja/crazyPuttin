package physics;

import objects.Ball;
import objects.GameObject;

import java.util.ArrayList;
import java.util.Arrays;

public class AdamsMoulton3 extends Solver{

    public AdamsMoulton3(){

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
            double prediction_x = positions.get(n).getX()  + H * average(c,new double[]{velocities.get(n).getX()   , velocities.get(n - 1).getX()   ,   velocities.get(n - 2).getX()},denominator);
            double prediction_y = positions.get(n).getY()  + H * average(c,new double[]{velocities.get(n).getY()   , velocities.get(n - 1).getY()   ,   velocities.get(n - 2).getY()},denominator);
            double prediction_vX= velocities.get(n).getX() + H * average(c,new double[]{accelerations.get(n).getX(), accelerations.get(n - 1).getX(),accelerations.get(n - 2).getX()},denominator);
            double prediction_vY= velocities.get(n).getY() + H * average(c,new double[]{accelerations.get(n).getY(), accelerations.get(n - 1).getY(),accelerations.get(n - 2).getY()},denominator);

            Vector2D predictPos = new Vector2D(prediction_x, prediction_y);
            Vector2D predictVel = new Vector2D(prediction_vX, prediction_vY);
            Vector2D predictAcc = PHYSICS.calculateAcceleration(predictPos,predictVel);
            double x = positions.get(n).getX()  + H * average( new double[]{5,8,-1} ,new double[]{predictVel.getX(), velocities.get(n).getX(), velocities.get(n - 1).getX()} ,12);
            double y = positions.get(n).getY()  + H * average( new double[]{5,8,-1} ,new double[]{predictVel.getY(), velocities.get(n).getY(), velocities.get(n - 1).getY()} ,12);
            double vX= velocities.get(n).getX() + H * average( new double[]{5,8,-1} ,new double[]{predictAcc.getX(), accelerations.get(n).getX(), accelerations.get(n - 1).getX()} ,12);
            double vY= velocities.get(n).getY() + H * average( new double[]{5,8,-1} ,new double[]{predictAcc.getY(), accelerations.get(n).getY(), accelerations.get(n - 1).getY()} ,12);
            Vector2D correctedPos = new Vector2D(x,y);
            Vector2D correctedVel = new Vector2D(vX,vY);
            Vector2D correctedAcc = PHYSICS.calculateAcceleration(correctedPos,correctedVel);
            velocities.add(correctedVel);
            positions.add(correctedPos);
            accelerations.add(correctedAcc);

            velocities.remove(0);
            positions.remove(0);
            accelerations.remove(0);

            return new Vector2D[]{correctedPos, correctedVel};
        }
    }

    public ArrayList<Vector2D> getPositions() {
        return positions;
    }

    public ArrayList<Vector2D> getVelocities() {
        return velocities;
    }

    public ArrayList<Vector2D> getAccelerations() {
        return accelerations;
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


//    public static void main(String[] args) {
//
//        Solver solver1 = new RK4();
//        Solver solver2 = new AdamsBashforth3();
//        AdamsMoulton3 solver3 = new AdamsMoulton3();
//        Solver solver4 = new Euler();
//
//        Ball ball1 = new Ball(new Vector2D(0, 0));
//        Ball ball2 = new Ball(new Vector2D(0, 0));
//        Ball ball3 = new Ball(new Vector2D(0, 0));
//        Ball ball4 = new Ball(new Vector2D(0, 0));
//        ball1.setVelocity(new Vector2D(2,0));
//        ball2.setVelocity(new Vector2D(2,0));
//        ball3.setVelocity(new Vector2D(2,0));
//        ball4.setVelocity(new Vector2D(2,0));
//        for (int i = 0; i < 1000; i++) {
//            ball1.setState(solver1.calculateNext(ball1.getPosition(),ball1.getVelocity(),0.001));
//            ball2.setState(solver2.calculateNext(ball2.getPosition(),ball2.getVelocity(),0.001));
//            ball3.setState(solver3.calculateNext(ball3.getPosition(),ball3.getVelocity(),0.001));
//            ball4.setState(solver4.calculateNext(ball4.getPosition(),ball4.getVelocity(),0.001));
//
//
//        }
//        System.out.println(solver3.getPositions());
//        System.out.println(ball1.getPosition().getX());
//        System.out.println(ball2.getPosition().getX());
//        System.out.println(ball3.getPosition().getX());
//        System.out.println(ball4.getPosition().getX());
//
//    }
}
