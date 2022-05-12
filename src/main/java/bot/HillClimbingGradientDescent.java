package bot;

import Main.Universe;
import physics.Vector2D;

import java.util.ArrayList;


/**
 * iterative algorithm: at each iteration we change the velocity and asses the change by the fitness value
 * look at step: what should be the value be?
 */
public class HillClimbingGradientDescent implements Bot {

    private final Universe universe;
    private final Vector2D targetPosition;
    private final Heuristics heuristics = Heuristics.allPositions;

    private final Vector2D bestVelocity;
    private double bestResult;
    private int shotCounter = 0;
    private final double LR = 0.9;
    private double learningRate = 0.9;

    public HillClimbingGradientDescent(Universe universe) {
        this.universe = universe;
        this.targetPosition = universe.getTarget().getPosition();
        this.bestVelocity = climb();
    }


    public HillClimbingGradientDescent(Universe universe, Vector2D targetPosition) {
        this.universe = universe;
        this.targetPosition = targetPosition;
        this.bestVelocity = climb();
    }

    private Vector2D climb() {
        int index = 0 ;
        int iteration = 10 ;
        int power = -5;
        int testNumber = 10;
        double stepSize = 0.000001;

        Vector2D velocity = new RandomBot(this.universe,testNumber).getVelocities().get(0);
        this.bestResult = new TestShot(this.universe, velocity, this.targetPosition).getTestResult(this.heuristics);
        boolean play = true;
        ArrayList<Vector2D> testVelocities = new ArrayList<>();
        testVelocities.add(0,velocity);

        while (play && testVelocities.size()<200 ) {
            play = false;
            this.shotCounter++;
            double derivativeX = derivativeX(velocity, stepSize)*learningRate;
            double derivativeY = derivativeY(velocity, stepSize)*learningRate;

            Vector2D testVelocity = new Vector2D(velocity.getX() - derivativeX, velocity.getY() - derivativeY);
            testVelocities.add(0,testVelocity);
            double testResult = new TestShot(this.universe, testVelocity, this.targetPosition).getTestResult(Heuristics.allPositions);
            System.out.println(bestResult + " --------> " + learningRate + " " + derivativeX  +" " + derivativeY);

            if (testResult == 0) {
                velocity = testVelocities.get(0);
                this.bestResult = testResult;
                break;
            }else if(testResult < this.bestResult) {

                velocity = testVelocities.get(0);
                this.bestResult = testResult;
                play = true;
            }


//            System.out.println(bestResult);
//            System.out.println("BEST " + bestResult);
            if( bestResult != 0 && testResult > bestResult && learningRate > Math.pow(10, power)){
                testVelocities.remove(0);
                velocity = testVelocities.get(0);
                learningRate *= 0.1;
                play = true ;
            }
            if(learningRate < Math.pow(10, power) && index < iteration ){
                System.out.println("RESET==================================================\n============================================");
                play = true;
                velocity = new RandomBot(this.universe,testNumber).getVelocities().get(0);

                this.bestResult = new TestShot(this.universe, velocity, this.targetPosition).getTestResult(this.heuristics);
                testVelocities = new ArrayList<>();
                testVelocities.add(0,velocity);
                learningRate = LR;
                index++;
            }

        }
        return velocity;
    }

    @Override
    public int getShotCounter() {
        return this.shotCounter;
    }

    @Override
    public ArrayList<Vector2D> getVelocities() {
        ArrayList<Vector2D> velocities = new ArrayList<>();
        velocities.add(this.bestVelocity);
        return velocities;
    }

    @Override
    public String toString() {
        return "Hill Climbing Gradient Descent Bot: "+
                "\nBest velocity: " + this.bestVelocity +
                "\nresult: " + this.bestResult +
                "\nshotCounter: " + this.shotCounter +
                "\nheuristics: " + this.heuristics+ "\n";
    }
    public double derivativeX( Vector2D velocity , double step){
        Vector2D velocityPlus = velocity.add(step , 0 );
        Vector2D velocityMinus = velocity.add(-1 * step , 0 );
//        System.out.println(velocityPlus + " -- " + velocityMinus + " -- > " + velocityPlus.add( velocityMinus.multiply(-1) ));
        double testShotPlus = new TestShot(this.universe, velocityPlus, this.targetPosition).getTestResult(this.heuristics);
        double testShotMinus = new TestShot(this.universe, velocityMinus, this.targetPosition).getTestResult(this.heuristics);
        return (testShotPlus-testShotMinus) / ( 2 * step) ;
    }
    public double derivativeY( Vector2D velocity , double step){
        Vector2D velocityPlus = velocity.add(0 , step );
        Vector2D velocityMinus = velocity.add( 0 , -1.d * step );
        double testShotPlus = new TestShot(this.universe, velocityPlus, this.targetPosition).getTestResult(this.heuristics);
        double testShotMinus = new TestShot(this.universe, velocityMinus, this.targetPosition).getTestResult(this.heuristics);
        return (testShotPlus-testShotMinus) / ( 2 * step) ;
    }

}
