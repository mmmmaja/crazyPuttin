package graphics;

import javafx.application.Application;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Line;
import javafx.scene.shape.MeshView;
import javafx.stage.Stage;
import objects.Ball;
import objects.Terrain;
import physics.Vector2D;


public class Display extends Application {

    public static final int FRAME_WIDTH = 1100;
    public static final int FRAME_HEIGHT = 600;
    private SmartGroup group;


    @Override
    public void start(Stage stage) throws Exception {

        this.group = new SmartGroup();

        Scene scene = new Scene(group, FRAME_WIDTH, FRAME_HEIGHT);
        scene.setFill(Color.BLACK);

        addLight();
        addBall();
        addMesh();

        Camera camera = new PerspectiveCamera();
        scene.setCamera(camera);

        // move the objects the middle
//        group.translateXProperty().set(FRAME_WIDTH / 2.0);
//        group.translateYProperty().set(FRAME_HEIGHT / 2.0);

        group.translateXProperty().set(0);
        group.translateYProperty().set(FRAME_HEIGHT / 1.5);
        group.translateZProperty().set(-800); // move it a little closer
        group.initMouseControl(scene);

        // zoomIn, zoomOut added on scroll event
        stage.addEventHandler(ScrollEvent.SCROLL, event -> group.zoom(event.getDeltaY()));

        stage.setScene(scene);
        stage.show();
    }


    private void addBall() {

        // FIXME adjust location of the ball later
        Ball ball = new Ball(new Vector2D(10, 10), new Vector2D(0, 0));
        this.group.getChildren().add(ball.getSphere());
    }


    private void addCartesianCoordinates() {
        Line axisX = new Line(0, 0, 1000, 0);
        Line axisY = new Line(0, 0, 0, -1000);

        axisX.setStroke(Color.PALEVIOLETRED);
        axisY.setStroke(Color.FORESTGREEN);
        this.group.getChildren().add(axisX);
        this.group.getChildren().add(axisY);
    }

    private void addMesh() {
        MeshView meshView = new MeshView();
        meshView.setMesh(new Terrain().getMesh());

        // adding grass material
        PhongMaterial material = new PhongMaterial();
        String materialPath = "file:///C:\\Users\\majag\\Desktop\\green.jpg"; // TODO change path later
       // material.setDiffuseMap(new Image(materialPath));
        material.setDiffuseColor(Color.FORESTGREEN);

        meshView.setMaterial(material);
        meshView.setCullFace(CullFace.NONE);
        meshView.setDrawMode(DrawMode.FILL);

        // TODO move the mesh
        // translate location of the mesh
        this.group.getChildren().add(meshView);
    }


    /**
     * TODO
     */
    private void addLight() {
//        AmbientLight ambient = new AmbientLight();
//        ambient.setColor(Color.LAVENDER);
//        this.group.getChildren().add(ambient);
    }
}
