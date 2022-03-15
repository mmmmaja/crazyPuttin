package graphics;

import Main.Main;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.MeshView;
import javafx.stage.Stage;
import Main.Universe;
import objects.TerrainGenerator;
import physics.Vector2D;


/**
 * fixme why hole disappears?
 */
public class Display extends Application {
    public static Universe universe = Main.getUniverse();

    public static final int FRAME_WIDTH = 1100;
    public static final int FRAME_HEIGHT = 600;

    // translate all the objects to the middle of the frame (used just for the display)
    public static final int translateX = FRAME_WIDTH / 3;
    public static final int translateY =  FRAME_HEIGHT / 3;

    // to which all the objects are added
    private SmartGroup group;


    @Override
    public void start(Stage stage) {



        this.group = new SmartGroup();
        Scene scene = new Scene(group, FRAME_WIDTH, FRAME_HEIGHT);
        scene.setFill(Color.BLACK);

        // add 3 different components for the mesh: grass, sandPits and water
        MeshView[] meshViews = universe.getMeshViews();
        for (MeshView meshView : meshViews) {
            this.group.getChildren().add(meshView);
        }
        this.group.getChildren().add(universe.getBall().getSphere());
        this.group.getChildren().add(universe.getTarget().getCircle());
        //addAxis();

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
                universe.takeShot(new Vector2D(0,20));
            }
        });


        stage.setScene(scene);
        stage.show();
    }



}
