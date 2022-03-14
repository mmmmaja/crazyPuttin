package objects;
import physics.Vector2D;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.*;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;


public class FileReader {

    float x0 ;            // x-coordinate of the initial position
    float y0 ;            // y-coordinate of the initial position
    Vector2D position0;
    float xt ;            // x-coordinate of the target position
    float yt ;            // y-coordinate of the target position
    Vector2D positionTarget;
    float r ;           // radius of the target
    float muk;          // kinetic friction coefficient of grass
    float mus ;         // static friction coefficient of grass
    float heightProfile ;
    float sandPitXmin ;        // x start of a sandpit
    float sandPitXmax ;        // x end of a sandpit
    float sandPitYmin ;        // y start of a sand pit
    float sandPitYmax ;        // y end of a sandpit
    Vector2D sandPitStart;
    Vector2D sandPitEnd;
    float muks ;        // kinetic friction coefficient of sand
    float muss ;        // static friction coefficient of sand
    File file;


    public FileReader(File file) {
        this.file=file;
        try {
            Scanner scanner = new Scanner(file);

            this.x0=Float.parseFloat(checkRegex(scanner.nextLine(),"\\s[+-]?([0-9]*[.])?[0-9]\\s"));
            this.y0=Float.parseFloat(checkRegex(scanner.nextLine(),"\\s[+-]?([0-9]*[.])?[0-9]\\s"));
            position0=new Vector2D(x0,y0);
            this.xt=Float.parseFloat(checkRegex(scanner.nextLine(),"\\s[+-]?([0-9]*[.])?[0-9]\\s"));
            this.yt=Float.parseFloat(checkRegex(scanner.nextLine(),"\\s[+-]?([0-9]*[.])?[0-9]\\s"));
            this.positionTarget=new Vector2D(x0,y0);
            this.r=Float.parseFloat(checkRegex(scanner.nextLine(),"\\s[+-]?([0-9]*[.])?[0-9]\\s"));
            this.muk=Float.parseFloat(checkRegex(scanner.nextLine(),"\\s[+-]?([0-9]*[.])?[0-9]\\s"));
            this.mus=Float.parseFloat(checkRegex(scanner.nextLine(),"\\s[+-]?([0-9]*[.])?[0-9]\\s"));
            this.heightProfile=calculator(checkRegex(scanner.nextLine(),"(?<= = )(.*)"));
            String string = scanner.nextLine();
            this.sandPitXmin=Float.parseFloat(checkRegex(string,"(?<= = )[+-]?([0-9]*[.])?[0-9]"));
            this.sandPitXmax=Float.parseFloat(checkRegex(string,"(?<=<x<)[+-]?([0-9]*[.])?[0-9]"));
            string = scanner.nextLine();
            this.sandPitYmin=Float.parseFloat(checkRegex(string,"(?<= = )[+-]?([0-9]*[.])?[0-9]"));
            this.sandPitYmax=Float.parseFloat(checkRegex(string,"(?<=<y<)[+-]?([0-9]*[.])?[0-9]"));
            this.sandPitStart=new Vector2D(sandPitXmin,sandPitYmin);
            this.sandPitEnd=new Vector2D(sandPitXmax,sandPitYmax);
            this.muks=Float.parseFloat(checkRegex(scanner.nextLine(),"\\s[+-]?([0-9]*[.])?[0-9]\\s"));
            this.muss=Float.parseFloat(checkRegex(scanner.nextLine(),"\\s[+-]?([0-9]*[.])?[0-9]\\s"));

        }

        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    public Vector2D getInitialPosition()
    {
        return this.position0;
    }

    public Vector2D getTargetPosition()
    {
        return this.positionTarget;
    }

    public double getTargetRadius()
    {
        return this.r;
    }

    public Vector2D getSandPitStart()
    {
        return this.sandPitStart;
    }

    public Vector2D getSandPitEnd()
    {
        return this.sandPitEnd;
    }



    public static String checkRegex (String string, String regex)
    {
        Pattern pattern = Pattern.compile(string);
        Matcher matcher = pattern.matcher(regex);
        boolean isMatch = matcher.find();

        return matcher.group();
    }

    public float calculator(String equation) {
        Expression expression = new ExpressionBuilder(equation)
                .variables("x", "y")
                .build()
                .setVariable("x", this.xt)
                .setVariable("y", this.yt);

        float result = (float) expression.evaluate();

        //Assertions.assertEquals(1, result);

        return result;

    }



}
