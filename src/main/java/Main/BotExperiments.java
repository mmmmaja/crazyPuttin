package Main;

import bot.Bot;
import bot.HillClimbingBot;
import bot.ImprovedRandomBot;
import bot.RandomBot;
import physics.Vector2D;

public class BotExperiments {

    private final Bot[] bots;

    public BotExperiments() {
        Universe universe = Main.getUniverse();
        this.bots = new Bot[] {
                new RandomBot(1000),
                new ImprovedRandomBot(1000),
                new HillClimbingBot()
        };
        run();
    }

    private void run() {
//        for (Bot bot : this.bots) {
//            System.out.println(bot);
//            Vector2D velocity = bot.getVelocities().get(0);
//            System.out.println("velocity: " + velocity + "\n");
//        }
    }

}
