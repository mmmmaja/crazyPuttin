package bot;


import objects.Ball;
import physics.Vector2D;

public class SimulatedAnnealing extends Bot {

    //TODO CHANGE THIS MF
    double initial_T = 30;

    public SimulatedAnnealing(){
        setTestNumber(1000);
        setName("SimulatedAnnealing");

    }

    @Override
    public void run() {
        Vector2D initialRandom = new Vector2D(Math.random()*10-5 , Math.random()*10-5).scaleDown(5);
        setBestVelocity(initialRandom);
        setBestResult(new TestShot(initialRandom, getTargetPosition(), Heuristics.finalPosition).getTestResult());

        double temp = initial_T;
        for (int i = 0; i < getTestNumber() ; i++) {
            Ball imaginary = universe.getBall().copyOf();
            imaginary.setVelocity(initialRandom.add( Math.random()*10-5 , Math.random()*10-5).scaleDown(5));
            universe.getSolver().nextStep(imaginary);
            Vector2D next_velocity = imaginary.getVelocity();
            double testResult = new TestShot(next_velocity, getTargetPosition(), Heuristics.finalPosition).getTestResult();

            if( testResult == 0 ){
                setBestVelocity(next_velocity);
                setBestResult(testResult);

                stop();
            }
            double fitness_value_difference = testResult - getBestResult();
            double r = Math.random();
            if(fitness_value_difference <= 0 || r < Math.exp(-fitness_value_difference/(2*temp))){
                initialRandom = next_velocity;
                setBestResult(testResult);
            }
            if (testResult<= getBestResult()){
                setBestVelocity(next_velocity);
                setBestResult(testResult);
            }
            temp = initial_T / i;
        }

        stop();
    }
}
