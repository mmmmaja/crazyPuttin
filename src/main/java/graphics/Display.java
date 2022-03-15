package graphics;

import Main.Main;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.MeshView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import Main.Universe;
import objects.TerrainGenerator;
import physics.Vector2D;

import java.util.EventListener;


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

    // to which all the objects are added (rotation built-in)
    private SmartGroup root;
    private SmartGroup group;

    private Pane displayPane;
    private GridPane gridPane;



    @Override
    public void start(Stage stage) {

        this.group = new SmartGroup();
        MeshView[] meshViews = universe.getMeshViews();
        for (MeshView meshView : meshViews) {
            this.group.getChildren().add(meshView);
        }
        this.group.getChildren().add(universe.getBall().getSphere());
        this.group.getChildren().add(universe.getTarget().getCircle());

        SubScene subScene = new SubScene(this.group, FRAME_WIDTH - 200, FRAME_HEIGHT, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.BLACK);

        Camera camera = new PerspectiveCamera();

        this.displayPane = new Pane();
        displayPane.setPrefSize(FRAME_WIDTH, FRAME_HEIGHT);

        this.gridPane = new GridPane();
        addPanel();
        this.displayPane.getChildren().add(this.gridPane);
        gridPane.setTranslateX(FRAME_WIDTH - 200);
        subScene.setCamera(camera);

        Pane pane3D = new Pane();
        pane3D.setPrefSize(FRAME_WIDTH - 200, FRAME_HEIGHT);
        pane3D.getChildren().add(subScene);
        this.displayPane.getChildren().add(pane3D);

        Scene scene = new Scene(this.displayPane);

        // move the objects the middle
        group.translateXProperty().set(FRAME_WIDTH / 2.0);
        group.translateYProperty().set(FRAME_HEIGHT / 2.0);
        group.translateZProperty().set(-300); // move it a little closer
        group.initMouseControl(scene);

        // zoomIn, zoomOut added on scroll event
        stage.addEventHandler(ScrollEvent.SCROLL, event -> group.zoom(event.getDeltaY()));

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.W) {
                takeShot(new Vector2D(5,5), stage, scene);

            }
        });


        stage.setScene(scene);
        stage.show();
    }


    private void addPanel() {
        gridPane.setPrefSize(200, FRAME_HEIGHT);
        gridPane.setStyle("-fx-background-color: darkgrey");
        gridPane.setTranslateX(FRAME_WIDTH-200);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);

        Font font = new Font("Verdana", 14);
        Font bigFont = new Font("Verdana", 20);

        Text title = new Text("Golf Game \n- by group 6");
        title.setFill(Color.WHITE);
        title.setFont(bigFont);
        gridPane.add(new HBox(30, title), 0, 0);

        Text gap1 = new Text("");
        gridPane.add(new HBox(30, gap1), 0, 1);

        Text text = new Text(" X-position:  " + Math.round(universe.getBall().getPositionX())  +
                " \n Y-position:  " + Math.round(universe.getBall().getPositionY()) +
                " \n Number of shots: \n");
        text.setFill(Color.WHITE);
        text.setFont(font);
        gridPane.add(new HBox(30, text), 0, 2);

        Text gap2 = new Text("");
        gridPane.add(new HBox(30, gap2), 0, 3);

        Text readFromFile = new Text("Read file for\ninitial velocity");
        readFromFile.setFill(Color.WHITE);
        readFromFile.setFont(font);
        gridPane.add(new HBox(30, readFromFile), 0, 4);

        Button readFile = new Button("Read file");
        gridPane.add(new HBox(30, readFile), 0, 5);
        readFile.setOnMouseClicked(mouseEvent -> {
            universe.getFileReader().getNextShotFromFile();
        });

        Text gap3 = new Text("");
        gridPane.add(new HBox(30, gap3), 0, 6);

        Text readFromTextField = new Text("Type in\ninitial velocity");
        readFromTextField.setFill(Color.WHITE);
        readFromTextField.setFont(font);
        gridPane.add(new HBox(30, readFromTextField), 0, 7);

        Text x = new Text("Velocity in x-direction:");
        x.setFill(Color.WHITE);
        x.setFont(font);
        gridPane.add(new HBox(30, x), 0, 8);

        TextField xvel = new TextField();
        gridPane.add(new HBox(30, xvel), 0, 9);

        Text y = new Text("Velocity in y-direction:");
        y.setFill(Color.WHITE);
        y.setFont(font);
        gridPane.add(new HBox(30, y), 0, 10);

        TextField yvel = new TextField();
        gridPane.add(new HBox(30, yvel), 0, 11);

        Button button = new Button("Hit the ball");
        gridPane.add(new HBox(30, button), 0, 12);

        button.setOnMouseClicked(mouseEvent -> {
            // TODO
        });
    }


    public void takeShot(Vector2D velocity, Stage stage, Scene scene) {
        universe.getBall().setVelocity(velocity);
        int counter = 0;
        while (universe.getBall().isMoving()) {

            if (universe.getBall().isMoving()) {
                universe.nextStep(universe.getBall());
                universe.updateBallsPosition();
                System.out.println(universe.getBall().getPositionX() + " " + universe.getBall().getPositionY());
            }


        }
    }

}
