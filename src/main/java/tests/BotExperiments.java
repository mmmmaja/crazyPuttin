package tests;


import bot.*;
import physics.PhysicsEngine;

import java.util.ArrayList;


/**
 * class to test  the performance of the bots
 */
public class BotExperiments {

    private final  ArrayList<Bot> bots = new ArrayList<>();

    public BotExperiments() {
        for (double stepSize = 0.001; stepSize < 0.1; stepSize+= 0.1) {
            System.out.println(stepSize);


            testNonRandomBots();
        }
//        testRandom();
    }

    private void testNonRandomBots() {


            Bot hillClimbingBot = new HillClimbingBot(false);
            while (hillClimbingBot.running) {
                System.out.print("");
            }
            System.out.println(hillClimbingBot);
            double distance = hillClimbingBot.getBestResult();
            double time = hillClimbingBot.getTime(); // in sec
            System.out.println("distance: " + distance);
            System.out.println("timeElapsed: " + time);
            System.out.println("\n\n");

//        Bot ruleBasedBot = new RuleBasedBot(false);
//        while (ruleBasedBot.running) {
//            System.out.print("");
//        }
//        bots.add(ruleBasedBot);
//
//        System.out.println();

    }


    private void getResults() {
        for (Bot bot : this.bots) {
            System.out.println(bot);
            double distance = bot.getBestResult();
            double time = bot.getTime(); // in sec
            System.out.println("distance: " + distance);
            System.out.println("timeElapsed: " + time);
            System.out.println("\n\n");
        }
    }


    private void testRandom() {

        int n = 10; // run the tests n times and take the average of results

        double avgDistance = 0;
        double avgShotCounter = 0;
        double avgTime = 0;

        for (int i = 0; i < n; i++) {
            System.out.println(i);
            Bot bot = new RandomBot(1000, false);
            while (bot.running) {
                System.out.print("");
            }
            avgDistance+= bot.getBestResult();
            avgShotCounter+= bot.getShotCounter();
            avgTime+= bot.getTime(); // in sec
        }

        avgDistance /= n;
        avgShotCounter /= n;
        avgTime /= n;

        System.out.println("distance: " + avgDistance);
        System.out.println("timeElapsed: " + avgTime);
        System.out.println("shotCounter: " + avgShotCounter);


    }



}
