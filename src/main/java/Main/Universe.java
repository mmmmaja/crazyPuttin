package Main;

import graphics.Display;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import objects.*;
import physics.Euler;
import physics.Vector2D;


/**
 * holds all the objects used in the game and the physics engine
 */
public class Universe extends Euler {

    private final FileReader fileReader;

    private Ball ball;
    private Terrain terrain;
    private Target target;

    // object needed for the display
    private MeshView[] meshViews;

    public Universe(FileReader fileReader) {
        this.fileReader = fileReader;
        createBall();
        createTerrain();
        createTarget();
    }

    private void createBall() {
        Vector2D initialPosition = this.fileReader.getInitialPosition();
        this.ball = new Ball(new Vector2D(initialPosition.getX(), initialPosition.getY()) );
    }

    /**
     * creates 3 meshes: grass, water, sandPits
     */
    private void createTerrain() {
        this.terrain = new Terrain(fileReader.getSandPitX(), fileReader.getSandPitY());
//        this.terrain = new Terrain(new SandPit(fileReader.getSandPitX(), fileReader.getSandPitY()));

        MeshView meshViewGrass = new MeshView();
        meshViewGrass.setMesh(this.terrain.getGrassMesh());
        MeshView meshViewSandPit = new MeshView();
        meshViewSandPit.setMesh(this.terrain.getSandPitMesh());
        MeshView meshViewWater = new MeshView();
        meshViewWater.setMesh(this.terrain.getWaterMesh());

        // adding grass material
        PhongMaterial grassMaterial = new PhongMaterial();
        grassMaterial.setDiffuseColor(Color.FORESTGREEN);

        // adding water material
        PhongMaterial waterMaterial = new PhongMaterial();
        waterMaterial.setDiffuseColor(Color.ROYALBLUE);

        // adding sandPit material
        PhongMaterial sandPitMaterial = new PhongMaterial();
        sandPitMaterial.setDiffuseColor(Color.YELLOW);

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

    private void createTarget() {
        this.target = new Target(this.fileReader.getTargetPosition(), this.fileReader.getTargetRadius());
    }



    public Ball getBall() {
        return this.ball;
    }

    public Terrain getTerrain() {
        return this.terrain;
    }

    public Target getTarget() {
        return this.target;
    }

    public MeshView[] getMeshViews() {
        return this.meshViews;
    }

    public void updateBallsPosition(){
        Vector2D position = ball.getPosition();
        ball.getSphere().setTranslateX(position.getX() - Display.translateX + ( ball.getPositionX() - ball.getPreviousPosition().getX() ) );
        ball.getSphere().setTranslateY(position.getY() - Display.translateY + ( ball.getPositionY() - ball.getPreviousPosition().getY() ) );
        ball.getSphere().setTranslateZ(TerrainGenerator.getHeight(position));
    }

    public void takeShot(Vector2D velocity) {
        ball.setVelocity(velocity);
        while(ball.isMoving()){
            nextStep(ball);
            updateBallsPosition();
        }
        System.out.println(ball.getPositionX() + " " + ball.getPositionY());
    }
}
