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
import objects.FileReader;
import objects.Terrain;
import objects.Universe;
import physics.Vector2D;


public class Display extends Application {

    public static final int FRAME_WIDTH = 1100;
    public static final int FRAME_HEIGHT = 600;

    private SmartGroup group;


    @Override
    public void start(Stage stage) throws Exception {
        Universe universe = Main.getUniverse();

        this.group = new SmartGroup();

        Scene scene = new Scene(group, FRAME_WIDTH, FRAME_HEIGHT);
        scene.setFill(Color.BLACK);

        this.group.getChildren().add(universe.getMeshView());
        this.group.getChildren().add(universe.getBall().getSphere());


        Camera camera = new PerspectiveCamera();
        scene.setCamera(camera);

        // move the objects the middle
        group.translateXProperty().set(FRAME_WIDTH / 2.0);
        group.translateYProperty().set(FRAME_HEIGHT / 2.0);
        group.translateZProperty().set(-300); // move it a little closer
        group.initMouseControl(scene);

        // zoomIn, zoomOut added on scroll event
        stage.addEventHandler(ScrollEvent.SCROLL, event -> group.zoom(event.getDeltaY()));

        stage.setScene(scene);
        stage.show();
    }

}
