package objects;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import physics.Vector2D;

public class Universe {

    private final FileReader fileReader;

    private Ball ball;
    private Terrain terrain;
    private MeshView meshView;

    public Universe(FileReader fileReader) {
        this.fileReader = fileReader;
        createBall();
        createTerrain();

    }

    private void createBall() {
        Vector2D initialPosition = this.fileReader.getInitialPosition();
        this.ball = new Ball(new Vector2D(initialPosition.getX(), initialPosition.getY()));
    }

    private void createTerrain() {
        this.meshView = new MeshView();
        this.meshView.setMesh(new Terrain().getMesh());

        // adding grass material
        PhongMaterial material = new PhongMaterial();
        String materialPath = "file:///C:\\Users\\majag\\Desktop\\green.jpg"; // TODO change path later
        //material.setDiffuseMap(new Image(materialPath));
        material.setDiffuseColor(Color.FORESTGREEN);

        this.meshView.setMaterial(material);
        this.meshView.setCullFace(CullFace.NONE);
        this.meshView.setDrawMode(DrawMode.FILL);
    }


    public Ball getBall() {
        return this.ball;
    }

    public Terrain getTerrain() {
        return this.terrain;
    }

    public MeshView getMeshView() {
        return this.meshView;
    }
}
