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
public class Display extends Application implements EventHandler {
    public static Universe universe = Main.getUniverse();

    public static final int FRAME_WIDTH = 1100;
    public static final int FRAME_HEIGHT = 600;

    // translate all the objects to the middle of the frame (used just for the display)
    public static final int translateX = FRAME_WIDTH / 3;
    public static final int translateY =  FRAME_HEIGHT / 3;

    // to which all the objects are added (rotation built-in)
    private SmartGroup root;
    private SmartGroup group;


    @Override
    public void start(Stage stage) {

        this.root = new SmartGroup();
        this.group = new SmartGroup();
        root.getChildren().addAll(group);
        Scene scene = new Scene(root, FRAME_WIDTH, FRAME_HEIGHT);
        scene.setFill(Color.BLACK);

        // add 3 different components for the mesh: grass, sandPits and water
        MeshView[] meshViews = universe.getMeshViews();
        for (MeshView meshView : meshViews) {
            this.group.getChildren().add(meshView);
        }
        this.group.getChildren().add(universe.getBall().getSphere());
        this.group.getChildren().add(universe.getTarget().getCircle());

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
                takeShot(new Vector2D(5,5), stage);

            }
        });

        addPanel();
        stage.setScene(scene);
        stage.show();
    }


    private void addPanel() {
        GridPane gridpane = new GridPane();
        gridpane.setPrefSize(200, FRAME_HEIGHT);
        gridpane.setStyle("-fx-background-color: darkgrey");
        gridpane.setTranslateX(FRAME_WIDTH-200);
        gridpane.setHgap(10);
        gridpane.setVgap(10);
        gridpane.setAlignment(Pos.CENTER);

        Font font = new Font("Verdana", 14);
        Font bigFont = new Font("Verdana", 20);

        Text title = new Text("Golf Game \n- by group 6");
        title.setFill(Color.WHITE);
        title.setFont(bigFont);
        gridpane.add(new HBox(30, title), 0, 0);

        Text gap1 = new Text("");
        gridpane.add(new HBox(30, gap1), 0, 1);

        Text text = new Text(" X-position:  " + universe.getBall().getPositionX() +
                " \n Y-position:  " + universe.getBall().getPositionY() +
                " \n Number of shots: \n");
        text.setFill(Color.WHITE);
        text.setFont(font);
        gridpane.add(new HBox(30, text), 0, 2);

        Text gap2 = new Text("");
        gridpane.add(new HBox(30, gap2), 0, 3);

        Text readFromFile = new Text("Read file for\ninitial velocity");
        readFromFile.setFill(Color.WHITE);
        readFromFile.setFont(font);
        gridpane.add(new HBox(30, readFromFile), 0, 4);

        Button readFile = new Button("Read file");
        gridpane.add(new HBox(30, readFile), 0, 5);
        readFile.setOnMouseClicked(mouseEvent -> {
            universe.getFileReader().getNextShotFromFile();
        });

        Text gap3 = new Text("");
        gridpane.add(new HBox(30, gap3), 0, 6);

        Text readFromTextField = new Text("Type in\ninitial velocity");
        readFromTextField.setFill(Color.WHITE);
        readFromTextField.setFont(font);
        gridpane.add(new HBox(30, readFromTextField), 0, 7);

        Text x = new Text("Velocity in x-direction:");
        x.setFill(Color.WHITE);
        x.setFont(font);
        gridpane.add(new HBox(30, x), 0, 8);

        TextField xvel = new TextField();
        gridpane.add(new HBox(30, xvel), 0, 9);

        Text y = new Text("Velocity in y-direction:");
        y.setFill(Color.WHITE);
        y.setFont(font);
        gridpane.add(new HBox(30, y), 0, 10);

        TextField yvel = new TextField();
        gridpane.add(new HBox(30, yvel), 0, 11);

        Button button = new Button("Hit the ball");
        gridpane.add(new HBox(30, button), 0, 12);

        button.setOnMouseClicked(mouseEvent -> {
            // TODO
        });
        this.root.getChildren().add(gridpane);
    }


    public void takeShot(Vector2D velocity, Stage stage) {
        universe.getBall().setVelocity(velocity);
        int counter = 0;
        while (universe.getBall().isMoving()) {

            if (universe.getBall().isMoving()) {
                universe.nextStep(universe.getBall());
                universe.updateBallsPosition();
                System.out.println(universe.getBall().getPositionX() + " " + universe.getBall().getPositionY());
            }


        }
//        stage.getScene().getWindow().setWidth(stage.getScene().getWidth() + Math.pow(-1, counter) + 13.5411111111);
    }

    @Override
    public void handle(Event event) {

    }
}
