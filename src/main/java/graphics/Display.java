package graphics;

import Main.Main;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import Main.Universe;
import objects.Ball;
import objects.TerrainGenerator;
import physics.Vector2D;


public class Display extends Application {

    public static final int FRAME_WIDTH = 1100;
    public static final int FRAME_HEIGHT = 600;

    // translate all the objects to the middle of the frame (used just for the display)
    public static final int translateX = FRAME_WIDTH / 3;
    public static final int translateY =  FRAME_HEIGHT / 3;

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

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.W) {
                Vector2D position = universe.getBall().getPosition();
                universe.getBall().getSphere().setTranslateX(position.getX() - Display.translateX+1);
                universe.getBall().translateObject(1, 0);
                universe.getBall().getSphere().setTranslateZ(TerrainGenerator.getHeight(position));

            }
        });


        stage.setScene(scene);
        stage.show();
    }



}
