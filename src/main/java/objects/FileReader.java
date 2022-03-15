package objects;

import physics.PhysicEngine;
import physics.Vector2D;
import Main.Universe;
import java.util.Scanner;
import java.io.*;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class FileReader {
    Scanner inputReader;
    Scanner shotReader;
    File inputFile;
    File shotFile;
    String fileString = "";
    double x0;
    double y0;
    double xt;
    double yt;
    double r;
    double muk;
    double mus;
    String heightProfile;
    Vector2D sandPitX;
    Vector2D sandPitY;
    double muks;
    double muss;
    String equation;
    Expression expression;

    public FileReader() {
        try {
            shotFile = new File("C:\\Users\\majag\\IdeaProjects\\crazyPutting\\src\\main\\java\\shot.txt");
            if(shotFile.exists()){
                shotReader = new Scanner(shotFile);
            }
            inputFile = new File("C:\\Users\\majag\\IdeaProjects\\crazyPutting\\src\\main\\java\\example_inputfile.txt");
            if(inputFile.exists()){
                inputReader = new Scanner(inputFile);
                while(inputReader.hasNextLine()){
                    String[] next = inputReader.nextLine().split("=");
                    for (int i = 0; i < next.length; i++) {
                        next[i] = next[i].strip();
                    }
                    switch (next[0]){
                        case "x0":
                            x0 = Double.parseDouble(next[1]);
                            break;
                        case "y0":
                            y0 = Double.parseDouble(next[1]);
                            break;
                        case "xt":
                            xt = Double.parseDouble(next[1]);
                            break;
                        case "yt":
                            yt = Double.parseDouble(next[1]);
                            break;
                        case "r":
                            r = Double.parseDouble(next[1]);
                            break;
                        case "muk":
                            muk = Double.parseDouble(next[1]);
                            break;
                        case "mus":
                            mus = Double.parseDouble(next[1]);
                            break;
                        case "heightProfile":
                            heightProfile = next[1];
                            equation=next[1].replaceAll("Math.","");
                            expression= new ExpressionBuilder(equation)
                                    .variables("x", "y")
                                    .build();
                            break;
                        case "sandPitX":
                            String[] pitX = next[1].split("<");
                            sandPitX = new Vector2D(Double.parseDouble(pitX[0]),Double.parseDouble(pitX[2]));
                            break;
                        case "sandPitY":
                            String[] pitY = next[1].split("<");
                            sandPitY = new Vector2D(Double.parseDouble(pitY[0]),Double.parseDouble(pitY[2]));
                            break;
                        case "muks":
                            muks = Double.parseDouble(next[1]);
                            break;
                        case "muss":
                            muss = Double.parseDouble(next[1]);
                            break;
                    }

                }
            }

        }
        catch (Exception e){}
    }

    public Vector2D getInitialPosition() {
        return new Vector2D(x0,y0);
    }
    public Vector2D getTargetPosition() {
        return new Vector2D(xt,yt);
    }
    public double getTargetRadius() {
        return r;
    }
    public double getKineticFriction(){
        return muk;
    }
    public double getStaticFriction(){
        return mus;
    }
    public double getSandPitKineticFriction(){
        return muks;
    }
    public double getSandPitStaticFriction(){
        return muss;
    }
    public Vector2D getSandPitX(){
        return sandPitX;
    }
    public Vector2D getSandPitY(){
        return sandPitY;
    }
    public String getHeightProfile(){
        return heightProfile;
    }
    public Vector2D getNextShotFromFile(){
        String next = shotReader.nextLine();
        String[] shot = next.split(",");
        return new Vector2D(Double.parseDouble(shot[0]), Double.parseDouble(shot[1]));
    }

    public Expression getExpression()
    {
        return expression;
    }



    public float calculator( float x, float y)
    {
        return calculator(expression, x, y);
    }

    public float calculator(Expression expression, float x, float y)
    {
        expression
                .setVariable("x", x)
                .setVariable("y", y);

        float result = (float) expression.evaluate();

        //Assertions.assertEquals(1, result);

        return result;

    }

    public static void main(String[] args) {
        FileReader f = new FileReader();
        System.out.println(f.x0 + ", " + f.y0);
        System.out.println(f.xt + ", " + f.yt);
        System.out.println(f.r);
        System.out.println(f.muk);
        System.out.println(f.mus);
        System.out.println(f.heightProfile);
        System.out.println(f.sandPitX);
        System.out.println(f.sandPitY);
        System.out.println(f.muks);
        System.out.println(f.muss);


        System.out.println(f.equation);
        System.out.println(f.calculator(3,4));

    }

}