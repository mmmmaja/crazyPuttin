package physics;

import objects.Ball;

import java.util.ArrayList;

public class ErrorExperiments {

    static PhysicsEngine physicsEngine = new PhysicsEngine();

    static int[] iterationsRunTime =
            {
                    4,10,101, 201, 301, 401, 501, 601,
                    701, 801, 901, 1001, 1101, 1201,
                    1301, 1401, 1501, 1601, 1701, 1801,
                    1901, 2001
            };

    static double[] stepsRunTime = {
            0.25,0.1,  0.009900990099010, 0.004975124378109, 0.003322259136213,
            0.002493765586035, 0.001996007984032, 0.001663893510815,
            0.001426533523538, 0.001248439450687, 0.001109877913430,
            0.000999000999001, 0.000908265213442, 0.000832639467111,
            0.000768639508071, 0.000713775874375, 0.000666222518321,
            0.000624609618988, 0.000587889476778, 0.000555247084953,
            0.000526038926881, 0.000499750124938
    };

    static int[] iterationsError = {
            4    , 5   , 10  , 20   , 25   , 50  , 100  ,
            200   , 250   , 500   , 1000  , 2000
    };

    static double[] stepsErrors =  {
            0.25 , 0.2 , 0.1 , 0.05 , 0.04 , 0.02, 0.01 ,
            0.005 , 0.004 , 0.002 , 0.001 , 0.0005
    };

    public static void main(String[] args) {
        Solver[] solvers = {new RK4(), new Heuns3(), new AdamsMoulton3(),new AdamsBashforth3(), new RK2(), new Euler()};

        Solver solver ;
        for (Solver value : solvers) {
            solver = value;
            System.out.println(solver.getClass().getName().substring(8) + " = " + errorExp(solver) + ";");
        }
        System.out.println("\n");
        for (Solver value : solvers) {
            solver = value;
            ArrayList<ArrayList<Double>> experiments = runTimeExp(value);
            System.out.println(solver.getClass().getName().substring(8) + "_ex=" + average(experiments) + ";");
        }
    }

    public static ArrayList<Double> errorExp(Solver solver){
        ArrayList<Double> errors = new ArrayList<>();
        for (int i = 0; i < stepsErrors.length; i++) {
            if(solver.getClass().getName().contains("AdamsMoulton3")){
                solver=new AdamsMoulton3();
            }
            if(solver.getClass().getName().contains("AdamsBashforth3")){
                solver=new AdamsBashforth3();
            }
            Ball ball = new Ball(new Vector2D(0,0));
            ball.setVelocity(new Vector2D(2, 0));
            for (int j = 0; j < iterationsError[i]; j++) {
                ball.setState(solver.calculateNext(ball.getPosition(),ball.getVelocity(),stepsErrors[i]));
            }
            errors.add(0, ball.getPosition().getX());
            }
        return errors;
    }

    public static ArrayList<ArrayList<Double>> runTimeExp(Solver solver){
        ArrayList<Double> execution = new ArrayList<>();
        ArrayList<ArrayList<Double>> executions = new ArrayList<>();
        for (int a = 0; a < 100; a++) {
            for (int i = 0; i < stepsRunTime.length; i++) {
                double start = System.nanoTime();
                Vector2D vel = new Vector2D(2, 0);
                Vector2D pos = new Vector2D(0, 0);
                physicsEngine.setSTEP(stepsRunTime[i]);
                Vector2D[] v4 = solver.calculateNext(pos, vel, stepsRunTime[i]);
                for (int j = 1; j < iterationsRunTime[i]; j++) {
                    pos = v4[0];
                    vel = v4[1];
                    v4 = solver.calculateNext(pos, vel, stepsRunTime[i]);
                }
                double finish = System.nanoTime();
                execution.add(0, (finish - start) / Math.pow(10, 6));
            }
            executions.add(execution);
            execution = new ArrayList<>();
        }
        return executions;
    }

    public static ArrayList<Double> average(ArrayList<ArrayList<Double>> experiments){
        ArrayList<Double> average = new ArrayList<>();

        double sum ;
        for (int i = 0; i < experiments.get(0).size(); i++) {
            sum=0;
            for (ArrayList<Double> experiment : experiments) {
                sum += experiment.get(i);
            }
            average.add(i,sum/experiments.get(0).size());
        }
        return average;
    }

}
