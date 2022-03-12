package graphics;

import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import objects.Ball;
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

        addBall();
        addCartesianCoordinates();

        Camera camera = new PerspectiveCamera();
        scene.setCamera(camera);

        // move the objects the middle
        // FIXME might not need it yet
        group.translateXProperty().set(FRAME_WIDTH / 2.0);
        group.translateYProperty().set(FRAME_HEIGHT / 2.0);
        group.translateZProperty().set(-600); // make it a little closer

        group.initMouseControl(scene);

        // zoomIn, zoomOut added on scroll event
        stage.addEventHandler(ScrollEvent.SCROLL, event -> group.zoom(event.getDeltaY()));

        stage.setTitle("display");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * TODO change access later
     */
    private void addBall() {
        // fixme location of the ball!!!
        Ball ball = new Ball(new Vector2D(10, 10), new Vector2D(0, 0));
        this.group.getChildren().add(ball.getSphere());
    }

    private void addCartesianCoordinates() {
        Line axisX = new Line(0, 0, 1000, 0);
        Line axisY = new Line(0, 0, 0, 1000);

        axisX.setStroke(Color.PALEVIOLETRED);
        axisY.setStroke(Color.FORESTGREEN);
        this.group.getChildren().add(axisX);
        this.group.getChildren().add(axisY);

    }

    private void addMesh() {

    }

}
