package graphics;

import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;


public class Display extends Application {

    public static final int FRAME_WIDTH = 1100;
    public static final int FRAME_HEIGHT = 600;


    @Override
    public void start(Stage stage) throws Exception {
        SmartGroup group = new SmartGroup();

        Scene scene = new Scene(group, FRAME_WIDTH, FRAME_HEIGHT);
        scene.setFill(Color.BLACK);

        Camera camera = new PerspectiveCamera();
        scene.setCamera(camera);

        // move the objects the middle
        // FIXME might not need it yet
        group.translateXProperty().set(FRAME_WIDTH / 2.0);
        group.translateYProperty().set(FRAME_HEIGHT / 2.0);
        group.translateZProperty().set(-800);

        group.initMouseControl(scene);

        // zoomIn, zoomOut added on scroll event
        stage.addEventHandler(ScrollEvent.SCROLL, event -> group.zoom(event.getDeltaY()));

        stage.setTitle("display");
        stage.setScene(scene);
        stage.show();
    }

}
