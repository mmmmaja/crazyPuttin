package Main;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import objects.Ball;
import objects.FileReader;
import objects.Target;
import objects.Terrain;
import physics.Euler;
import physics.PhysicEngine;
import physics.Vector2D;


public class Universe extends Euler {

    private final FileReader fileReader;

    private Ball ball;
    private Terrain terrain;
    private Target target;

    // object needed just for the display
    private MeshView meshView;

    public Universe(FileReader fileReader) {
        this.fileReader = fileReader;
        createBall();
        createTerrain();
        createTarget();
    }

    private void createBall() {
        Vector2D initialPosition = this.fileReader.getInitialPosition();
        this.ball = new Ball(new Vector2D(initialPosition.getX(), initialPosition.getY()));
    }

    /**
     * TODO add sandpits!
     */
    private void createTerrain() {
        this.meshView = new MeshView();
        this.terrain = new Terrain();
        this.meshView.setMesh(this.terrain.getMesh());

        // adding grass material
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.FORESTGREEN);

        this.meshView.setMaterial(material);
        this.meshView.setCullFace(CullFace.NONE);
        this.meshView.setDrawMode(DrawMode.FILL);
    }


    private void createTarget() {
        this.target = new Target(this.fileReader.getTargetPosition());
        this.target.setDimension(new Vector2D(this.fileReader.getTargetRadius(), this.fileReader.getTargetRadius()));
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

    public MeshView getMeshView() {
        return this.meshView;
    }
}
