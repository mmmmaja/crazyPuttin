package Main;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import objects.*;
import physics.Euler;
import physics.Solver;
import physics.Vector2D;


/**
 * Class holding all the objects used in the game and the physics engine
 */
public class Universe {

    private final FileReader fileReader;
    private Ball ball;
    private Target target;
    private Pole pole;
    private Terrain terrain;
    private Flag flag;
    private final double MAX_SPEED = 5.d;
    private MeshView[] meshViews;
    private Solver solver;
    private PhongMaterial grassMaterial ;


    public Universe(FileReader fileReader ) {
        this.fileReader = fileReader;
        createBall();
        createTerrain();
        createTarget();
        createPole();
        createFlag();
    }

    /**
     * creates the ball objects using the values from the inputFile
     */
    private void createBall() {
        Vector2D initialPosition = this.fileReader.getInitialPosition();
        this.ball = new Ball(new Vector2D(initialPosition.getX(), initialPosition.getY()) );
    }


    /**
     * creates 3 meshes: grass, water, sandPits based on the function from the inputFile
     */
    private void createTerrain() {
        this.terrain = new Terrain(fileReader);

        MeshView meshViewGrass = new MeshView();
        meshViewGrass.setMesh(this.terrain.getGrassMesh());
        MeshView meshViewSandPit = new MeshView();
        meshViewSandPit.setMesh(this.terrain.getSandPitMesh());
        MeshView meshViewWater = new MeshView();
        meshViewWater.setMesh(this.terrain.getWaterMesh());

        // adding grass material
        PhongMaterial grassMaterial = new PhongMaterial();
        grassMaterial.setDiffuseColor(Color.PURPLE);

        // adding water material
        PhongMaterial waterMaterial = new PhongMaterial();
        waterMaterial.setDiffuseColor(Color.web("#2389da"));

        // adding sandPit material
        PhongMaterial sandPitMaterial = new PhongMaterial();
        sandPitMaterial.setDiffuseColor(Color.SANDYBROWN);

        meshViewGrass.setMaterial(grassMaterial);
        meshViewGrass.setCullFace(CullFace.NONE);
        meshViewGrass.setDrawMode(DrawMode.FILL);

        meshViewSandPit.setMaterial(sandPitMaterial);
        meshViewSandPit.setCullFace(CullFace.NONE);
        meshViewSandPit.setDrawMode(DrawMode.FILL);

        meshViewWater.setMaterial(waterMaterial);
        meshViewWater.setCullFace(CullFace.NONE);
        meshViewWater.setDrawMode(DrawMode.FILL);

        this.meshViews = new MeshView[] {meshViewGrass, meshViewWater,meshViewSandPit};
    }

    /**
     * creates the target object getting the position from the InputFile
     */
    private void createTarget() {
        this.target = new Target(this.fileReader.getTargetPosition(), this.fileReader.getTargetRadius());
    }
    private void createPole() {
        this.pole = new Pole(this.fileReader.getTargetPosition());
    }
    private void createFlag() {
        this.flag = new Flag(this.fileReader.getTargetPosition());
    }



    public Solver getSolver(){
        if(solver == null )
            return new Euler();
        return this.solver;
    }

    public void setSolver(Solver solver){
        this.solver = solver;
    }

    public Ball getBall() {
        return this.ball;
    }

    public Target getTarget() {
        return this.target;
    }
    public Pole getPole(){
        return this.pole;
    }
    public Flag getFlag(){
        return this.flag;
    }

    public MeshView[] getMeshViews() {
        return this.meshViews;
    }

    public void updateBallPosition(){
        Vector2D position = ball.getPosition();
        ball.getSphere().setTranslateX(position.getX() + (ball.getPositionX() - ball.getPreviousPosition().getX()));
        ball.getSphere().setTranslateY(position.getY() + (ball.getPositionY() - ball.getPreviousPosition().getY()));
        ball.getSphere().setTranslateZ((-TerrainGenerator.getHeight(position) - 2*ball.getRADIUS()));
    }

    public FileReader getFileReader() {
        return this.fileReader;
    }
}
