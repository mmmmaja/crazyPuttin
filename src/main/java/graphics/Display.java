package graphics;

import Main.Main;
import Main.Shot;
import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.shape.MeshView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import Main.Universe;
import javafx.stage.WindowEvent;
import physics.Vector2D;

import java.util.Objects;


public class Display extends Application {

    public static Universe universe = Main.getUniverse();

    public static final int FRAME_WIDTH = 1100;
    public static final int FRAME_HEIGHT = 600;

    // translate all the objects to the middle of the frame
    public static final int translateX = FRAME_WIDTH / 3;
    public static final int translateY =  FRAME_HEIGHT / 3;

    // to smartGroup all the objects are added (rotation built-in)
    private SmartGroup group;

    private GridPane gridPane;
    private Text text;
    private int shotCounter = 0;


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

        Pane displayPane = new Pane();
        displayPane.setPrefSize(FRAME_WIDTH, FRAME_HEIGHT);

        this.gridPane = new GridPane();
        addPanel();
        displayPane.getChildren().add(this.gridPane);
        this.gridPane.setTranslateX(FRAME_WIDTH - 200);

        Camera camera = new PerspectiveCamera();
        subScene.setCamera(camera);

        Pane pane3D = new Pane();
        pane3D.setPrefSize(FRAME_WIDTH - 200, FRAME_HEIGHT);
        pane3D.getChildren().add(subScene);
        displayPane.getChildren().add(pane3D);

        Scene scene = new Scene(displayPane);

        // move the objects the middle
        group.translateXProperty().set((FRAME_WIDTH - 200)/ 2.0);
        group.translateYProperty().set(FRAME_HEIGHT / 2.0);
        group.translateZProperty().set(-200); // move it a little closer
        group.initMouseControl(scene);

        // zoomIn, zoomOut added on scroll event
        stage.addEventHandler(ScrollEvent.SCROLL, event -> group.zoom(event.getDeltaY()));

        // exit the application when the window is closed by the user
        stage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

        stage.setScene(scene);
        stage.show();
    }

    /**
     * update x and y position of the ball and counter of the shoots
     */
    private void updatePanel() {
        this.text.setText(" X-position:  " + Math.round(universe.getBall().getPositionX())  +
                " \n Y-position:  " + Math.round(universe.getBall().getPositionY()) +
                " \n Number of shots: "+ this.shotCounter+"\n");
    }

    /**
     * adds the panel with the buttons at the right-hand side
     */
    private void addPanel() {
        gridPane.setPrefSize(200, FRAME_HEIGHT);
        gridPane.setStyle("-fx-background-color: darkgrey");
        gridPane.setTranslateX(FRAME_WIDTH-200);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);

        Font font = new Font("Verdana", 14);
        Font bigFont = new Font("Verdana", 20);

        Text title = new Text("Golf Game \n- group 6");
        title.setFill(Color.WHITE);
        title.setFont(bigFont);
        gridPane.add(new HBox(30, title), 0, 0);

        Text gap1 = new Text("");
        gridPane.add(new HBox(30, gap1), 0, 1);

        this.text = new Text(" X-position:  " + Math.round(universe.getBall().getPositionX())  +
                " \n Y-position:  " + Math.round(universe.getBall().getPositionY()) +
                " \n Number of shots: "+ this.shotCounter+"\n");
        this.text.setFill(Color.WHITE);
        this.text.setFont(font);
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
            Vector2D velocity;

            if (!Objects.equals(xvel.getText(), "") && !Objects.equals(yvel.getText(), ""))
            {
                int xV = Integer.parseInt(xvel.getText());
                int yV = Integer.parseInt(yvel.getText());
                velocity = new Vector2D(xV, yV);
            }
            else {
                velocity = universe.getFileReader().getNextShotFromFile();
            }
            // TODO what if we run out of shots???
            if (velocity != null) {
                new Shot(universe, velocity);
                this.shotCounter++;
                updatePanel();
            }
        });
    }


}
