package physics;

import objects.Ball;
import objects.GameObject;

import java.util.ArrayList;
import java.util.Arrays;

public class AdamsMoulton3 extends Solver{
    private ArrayList<Vector2D> positions = new ArrayList<>();
    private ArrayList<Vector2D> velocities = new ArrayList<>();
    private ArrayList<Vector2D> accelerations = new ArrayList<>();
    private Solver bootstrap = new RK4();
    public AdamsMoulton3(){
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

    public static void main(String[] args) {

        Solver solver1 = new RK4();
        Solver solver2 = new AdamsBashforth3();
        AdamsMoulton3 solver3 = new AdamsMoulton3();
        Solver solver4 = new Euler();

        Ball ball1 = new Ball(new Vector2D(0, 0));
        Ball ball2 = new Ball(new Vector2D(0, 0));
        Ball ball3 = new Ball(new Vector2D(0, 0));
        Ball ball4 = new Ball(new Vector2D(0, 0));
        ball1.setVelocity(new Vector2D(2,0));
        ball2.setVelocity(new Vector2D(2,0));
        ball3.setVelocity(new Vector2D(2,0));
        ball4.setVelocity(new Vector2D(2,0));
        for (int i = 0; i < 1000; i++) {
            ball1.setState(solver1.calculateNext(ball1.getPosition(),ball1.getVelocity(),0.001));
            ball2.setState(solver2.calculateNext(ball2.getPosition(),ball2.getVelocity(),0.001));
            ball3.setState(solver3.calculateNext(ball3.getPosition(),ball3.getVelocity(),0.001));
            ball4.setState(solver4.calculateNext(ball4.getPosition(),ball4.getVelocity(),0.001));


        }
        System.out.println(solver3.getPositions());
        System.out.println(ball1.getPosition().getX());
        System.out.println(ball2.getPosition().getX());
        System.out.println(ball3.getPosition().getX());
        System.out.println(ball4.getPosition().getX());

    }
}
