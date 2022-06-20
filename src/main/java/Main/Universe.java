package Main;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import objects.*;
import physics.*;
import java.util.ArrayList;


/**
 * Class holding all the objects used in the game and the physics engine
 */
public class Universe {

    private final FileReader fileReader;
    private Solver solver;

    private Ball ball;
    private Target target;
    private Pole pole;
    private Flag flag;

    private MeshView[] meshViews;
    private final ArrayList<Tree> trees = new ArrayList<>();
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private ArrayList<Sphere> pathVisualizations = new ArrayList<>();


    public Universe(FileReader fileReader) {
        this.fileReader = fileReader;
        createBall();
        createTerrain();
        createTarget();
        createPole();
        createFlag();
        createObstacles();

    }

    private void createObstacles() {
        addObstacle(new Obstacle(new Vector2D(7.5, -9.5)));
        addObstacle(new Obstacle(new Vector2D(7.5, -8.5)));
        addObstacle(new Obstacle(new Vector2D(7.5, -7.5)));
        addObstacle(new Obstacle(new Vector2D(7.5, -6.5)));
        addObstacle(new Obstacle(new Vector2D(7.5, -5.5)));
        addObstacle(new Obstacle(new Vector2D(7.5, -4.5)));
        addObstacle(new Obstacle(new Vector2D(7.5, -3.5)));
        addObstacle(new Obstacle(new Vector2D(7.5, -2.5)));
        addObstacle(new Obstacle(new Vector2D(7.5, -1.5)));
        addObstacle(new Obstacle(new Vector2D(7.5, -0.5)));
        addObstacle(new Obstacle(new Vector2D(7.5, 0.5)));
        addObstacle(new Obstacle(new Vector2D(7.5, 1.5)));
        addObstacle(new Obstacle(new Vector2D(7.5, 2.5)));
        addObstacle(new Obstacle(new Vector2D(7.5, 3.5)));
        addObstacle(new Obstacle(new Vector2D(7.5, 4.5)));
        addObstacle(new Obstacle(new Vector2D(7.5, 5.5)));
        addObstacle(new Obstacle(new Vector2D(7.5, 6.5)));
        addObstacle(new Obstacle(new Vector2D(7.5, 7.5)));
        addObstacle(new Obstacle(new Vector2D(7.5, 8.5)));
        addObstacle(new Obstacle(new Vector2D(6.5, 8.5)));
        addObstacle(new Obstacle(new Vector2D(5.5, 8.5)));
        addObstacle(new Obstacle(new Vector2D(4.5, 8.5)));
        addObstacle(new Obstacle(new Vector2D(3.5, 8.5)));
        addObstacle(new Obstacle(new Vector2D(2.5, 8.5)));
        addObstacle(new Obstacle(new Vector2D(1.5, 8.5)));
        addObstacle(new Obstacle(new Vector2D(6.5, -9.5)));
        addObstacle(new Obstacle(new Vector2D(5.5, -9.5)));
        addObstacle(new Obstacle(new Vector2D(4.5, -9.5)));
        addObstacle(new Obstacle(new Vector2D(3.5, -9.5)));
        addObstacle(new Obstacle(new Vector2D(2.5, -9.5)));
        addObstacle(new Obstacle(new Vector2D(1.5, -9.5)));
        addObstacle(new Obstacle(new Vector2D(0.5, -9.5)));
        addObstacle(new Obstacle(new Vector2D(-0.5, -9.5)));
        addObstacle(new Obstacle(new Vector2D(-1.5, -9.5)));
        addObstacle(new Obstacle(new Vector2D(-2.5, -9.5)));
        addObstacle(new Obstacle(new Vector2D(-3.5, -9.5)));
        addObstacle(new Obstacle(new Vector2D(-4.5, -9.5)));
        addObstacle(new Obstacle(new Vector2D(-5.5, -9.5)));
        addObstacle(new Obstacle(new Vector2D(-6.5, -9.5)));
        addObstacle(new Obstacle(new Vector2D(-7.5, -9.5)));
        addObstacle(new Obstacle(new Vector2D(-8.5, -9.5)));
        addObstacle(new Obstacle(new Vector2D(-9.5, -9.5)));
        addObstacle(new Obstacle(new Vector2D(-9.5, -8.5)));
        addObstacle(new Obstacle(new Vector2D(-9.5, -7.5)));
        addObstacle(new Obstacle(new Vector2D(-9.5, -6.5)));
        addObstacle(new Obstacle(new Vector2D(-9.5, -5.5)));
        addObstacle(new Obstacle(new Vector2D(-9.5, -4.5)));
        addObstacle(new Obstacle(new Vector2D(-9.5, -3.5)));
        addObstacle(new Obstacle(new Vector2D(-9.5, -2.5)));
        addObstacle(new Obstacle(new Vector2D(-9.5, -1.5)));
        addObstacle(new Obstacle(new Vector2D(-9.5, -0.5)));
        addObstacle(new Obstacle(new Vector2D(-9.5, 0.5)));
        addObstacle(new Obstacle(new Vector2D(-9.5, 1.5)));
        addObstacle(new Obstacle(new Vector2D(-9.5, 2.5)));
        addObstacle(new Obstacle(new Vector2D(-9.5, 3.5)));
        addObstacle(new Obstacle(new Vector2D(-9.5, 4.5)));
        addObstacle(new Obstacle(new Vector2D(-9.5, 5.5)));
        addObstacle(new Obstacle(new Vector2D(0.5, 8.5)));
        addObstacle(new Obstacle(new Vector2D(-0.5, 8.5)));
        addObstacle(new Obstacle(new Vector2D(-1.5, 8.5)));
        addObstacle(new Obstacle(new Vector2D(-2.5, 8.5)));
        addObstacle(new Obstacle(new Vector2D(-9.5, 6.5)));
        addObstacle(new Obstacle(new Vector2D(-9.5, 7.5)));
        addObstacle(new Obstacle(new Vector2D(-9.5, 8.5)));
        addObstacle(new Obstacle(new Vector2D(-8.5, 8.5)));
        addObstacle(new Obstacle(new Vector2D(-7.5, 8.5)));
        addObstacle(new Obstacle(new Vector2D(-6.5, 8.5)));
        addObstacle(new Obstacle(new Vector2D(-5.5, 8.5)));
        addObstacle(new Obstacle(new Vector2D(-4.5, 8.5)));
        addObstacle(new Obstacle(new Vector2D(-3.5, 8.5)));
        addObstacle(new Obstacle(new Vector2D(6.5, 2.5)));
        addObstacle(new Obstacle(new Vector2D(5.5, 2.5)));
        addObstacle(new Obstacle(new Vector2D(4.5, 2.5)));
        addObstacle(new Obstacle(new Vector2D(3.5, 2.5)));
        addObstacle(new Obstacle(new Vector2D(2.5, 2.5)));
        addObstacle(new Obstacle(new Vector2D(1.5, 2.5)));
        addObstacle(new Obstacle(new Vector2D(0.5, 2.5)));
        addObstacle(new Obstacle(new Vector2D(-0.5, 2.5)));
        addObstacle(new Obstacle(new Vector2D(-0.5, 3.5)));
        addObstacle(new Obstacle(new Vector2D(-0.5, 4.5)));
        addObstacle(new Obstacle(new Vector2D(-8.5, -3.5)));
        addObstacle(new Obstacle(new Vector2D(-7.5, -3.5)));
        addObstacle(new Obstacle(new Vector2D(-6.5, -3.5)));
        addObstacle(new Obstacle(new Vector2D(-5.5, -3.5)));
        addObstacle(new Obstacle(new Vector2D(-4.5, -3.5)));
        addObstacle(new Obstacle(new Vector2D(-3.5, -3.5)));
        addObstacle(new Obstacle(new Vector2D(-2.5, -3.5)));
        addObstacle(new Obstacle(new Vector2D(-1.5, -3.5)));
        addObstacle(new Obstacle(new Vector2D(-0.5, -3.5)));
        addObstacle(new Obstacle(new Vector2D(0.5, -3.5)));
        addObstacle(new Obstacle(new Vector2D(0.5, -2.5)));
        addObstacle(new Obstacle(new Vector2D(0.5, -1.5)));
        addObstacle(new Obstacle(new Vector2D(1.5, -1.5)));
        addObstacle(new Obstacle(new Vector2D(2.5, -1.5)));
        addObstacle(new Obstacle(new Vector2D(-4.5, 4.5)));
        addObstacle(new Obstacle(new Vector2D(-3.5, 4.5)));
        addObstacle(new Obstacle(new Vector2D(-2.5, 4.5)));
        addObstacle(new Obstacle(new Vector2D(-1.5, 4.5)));
        addObstacle(new Obstacle(new Vector2D(0.5, -6.5)));
        addObstacle(new Obstacle(new Vector2D(0.5, -5.5)));
        addObstacle(new Obstacle(new Vector2D(0.5, -4.5)));
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
        Terrain terrain = new Terrain(fileReader);

        MeshView meshViewGrass = new MeshView();
        meshViewGrass.setMesh(terrain.getGrassMesh());
        MeshView meshViewSandPit = new MeshView();
        meshViewSandPit.setMesh(terrain.getSandPitMesh());
        MeshView meshViewWater = new MeshView();
        meshViewWater.setMesh(terrain.getWaterMesh());

        // adding grass material
        PhongMaterial grassMaterial = new PhongMaterial();
        grassMaterial.setDiffuseColor(Color.PURPLE);

        // adding grass material
        PhongMaterial grass2Material = new PhongMaterial();
        grass2Material.setDiffuseColor(Color.PURPLE);

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

        this.meshViews = new MeshView[] {meshViewGrass, meshViewWater , meshViewSandPit};
    }


    /**
     * creates the target object getting the position from the InputFile
     */
    private void createTarget() {
        this.target = new Target(this.fileReader.getTargetPosition(), this.fileReader.getTargetRadius());
    }

    /**
     * create the pole for the flag to be added to the display
     */
    private void createPole() {
        this.pole = new Pole(this.fileReader.getTargetPosition());
    }

    /**
     * create a flag to be added to the display
     */
    private void createFlag() {
        this.flag = new Flag(this.fileReader.getTargetPosition());
    }

    /**
     *
     * @return Euler solver is this.Solver is null otherwise solver that was assign to this universe
     */
    public Solver getSolver(){
        if (solver == null) {
            solver = new RK4();
            return solver;
        }
        return this.solver;
    }

    /**
     * @param solver Euler solver is this.Solver is null otherwise solver that was assign to this universe
     */
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


    /**
     * compute the next position for the ball and compute the location of the sphere
     */
    public void updateBallPosition(){
        Vector2D position = ball.getPosition();
        ball.getSphere().setTranslateX(position.getX() + (ball.getPosition().getX() - ball.getPreviousPosition().getX()));
        ball.getSphere().setTranslateY(position.getY() + (ball.getPosition().getY() - ball.getPreviousPosition().getY()));
        ball.getSphere().setTranslateZ((-TerrainGenerator.getHeight(position) - 2 * ball.getRADIUS()));

    }

    /**
     * set the ball back to the position read from inputFile
     */
    public void resetBall(){
        ball.setPosition(fileReader.getInitialPosition());
        ball.setPreviousPosition(fileReader.getInitialPosition());
        ball.setVelocity(new Vector2D(0,0));
    }

    public FileReader getFileReader() {
        return this.fileReader;
    }

    public ArrayList<Tree> getTrees() {
        return this.trees;
    }

    public ArrayList<Obstacle> getObstacles() {
        return this.obstacles;
    }

    public void addObstacle(Obstacle obstacle) {
        this.obstacles.add(obstacle);
    }

    public void deleteObstacles() {
        this.obstacles = new ArrayList<>();
    }

    public void addTree(Tree tree) {
        this.trees.add(tree);
    }

    public ArrayList<Sphere> getPathVisualizations() {
        return pathVisualizations;
    }

    public void addPathVisualization(Sphere sphere) {
        this.pathVisualizations.add(sphere);
    }

    public void deletePathVisualizations() {
        this.pathVisualizations = new ArrayList<>();
    }
}
