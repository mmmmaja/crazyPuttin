package graphics;

import Main.Main;
import Main.Shot;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javafx.stage.Stage;
import Main.Universe;
import physics.Vector2D;

import java.util.Objects;


public class Display extends Application {

    public Universe universe = Main.getUniverse();

    public static final int FRAME_WIDTH = 1100;
    public static final int FRAME_HEIGHT = 600;

    private GridPane gridPane;
    public Text text = new Text("");
    public int shotCounter = 0;

    SmartGroup group ;

    @Override
    public void start(Stage stage) {
        // to smartGroup all the objects are added (rotation built-in)
        group = new SmartGroup();
        AmbientLight ambient = new AmbientLight();
        PointLight point = new PointLight   ();
        ambient.setColor(Color.rgb(191, 191, 191,0.6));
        point.setColor(Color.rgb(255, 255, 255,0.1));

        point.setLayoutX(1000);
        point.setLayoutY(1000);

        point.setTranslateZ(-1100);
        // add water, sandPit and grass meshes to the display
        MeshView[] meshViews = universe.getMeshViews();
        Image grass_im = new Image("file:src/main/java/grass.jpg" ,5,5,false,false);
        Image water_im = new Image("file:src/main/java/water.jpg" ,5,5,false,false);
        Image sand_im = new Image(  "file:src/main/java/sand.jpg" ,5,5,false,false);
        Image flag_im = new Image(  "file:src/main/java/flag.jpg" ,5,5,false,false);

        PhongMaterial grass = new PhongMaterial();
        PhongMaterial water = new PhongMaterial();
        PhongMaterial sand = new PhongMaterial();
        PhongMaterial flag = new PhongMaterial();

        grass.setDiffuseMap(grass_im);
        water.setDiffuseMap(water_im);
        sand.setDiffuseMap(sand_im);
        flag.setDiffuseMap(flag_im);
        universe.getFlag().getBox().setMaterial(flag);
        meshViews[0].setMaterial(grass);
        meshViews[1].setMaterial(water);
        meshViews[2].setMaterial(sand);
        for (MeshView meshView : meshViews) {
            group.getChildren().add(meshView);
        }
        group.getChildren().addAll(point,ambient);
        group.getChildren().add(universe.getBall().getSphere());
        group.getChildren().add(universe.getTarget().getCylinder());
        group.getChildren().add(universe.getPole().getCylinder());
        group.getChildren().add(universe.getFlag().getBox());
        // this subScene holds terrain display (left part of the frame)
        SubScene subScene = new SubScene(group, FRAME_WIDTH - 200, FRAME_HEIGHT, true, null);
        subScene.setFill(Color.web("#3d3d3d"));

        Pane displayPane = new Pane();
        displayPane.setPrefSize(FRAME_WIDTH, FRAME_HEIGHT);

        // gridPane contains buttons and information about the state of the game (right part of the frame)
        this.gridPane = new GridPane();
        addPanel();
        displayPane.getChildren().add(this.gridPane);
        this.gridPane.setTranslateX(FRAME_WIDTH - 200);

        Camera camera = new PerspectiveCamera();
        camera.setNearClip(0.0001);
        subScene.setCamera(camera);

        Pane pane3D = new Pane();
        pane3D.setPrefSize(FRAME_WIDTH - 200, FRAME_HEIGHT);
        pane3D.getChildren().add(subScene);
        displayPane.getChildren().add(pane3D);
        Scene scene = new Scene(displayPane);

        // move the all the objects the middle of the frame
        group.translateXProperty().set((FRAME_WIDTH + 190 + 60) / 3.0); // plus right
        group.translateYProperty().set((FRAME_HEIGHT + 250 + 55) / 3.0);  // plus down
        group.zoom(camera.getTranslateY() + 900); // zoomIn
        group.initMouseControl(scene);
        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> gridPane.requestFocus());


        // zoomIn, zoomOut added on scroll event
        scene.addEventHandler(ScrollEvent.SCROLL, event -> camera.setTranslateZ(camera.getTranslateZ() + event.getDeltaY()/5));

        // add movement of the camera when keys are pressed
        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            switch (e.getCode()) {
                case W -> camera.setTranslateY(camera.getTranslateY() + 1);
                case S -> camera.setTranslateY(camera.getTranslateY() - 1);
                case D -> camera.setTranslateX(camera.getTranslateX() + 1);
                case A -> camera.setTranslateX(camera.getTranslateX() - 1);
            }
        });

        // exit the application when the window is closed by the user
        stage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

        stage.setScene(scene);
        stage.show();
    }



    /**
     * updates x and y position of the ball and counter of the shoots each time the ball is shot
     */
    public void updatePanel() {
        this.text.setText(" X-position:  " + String.format("%.2f" , universe.getBall().getPositionX() )  +
                " \n Y-position:  " + String.format("%.2f" , universe.getBall().getPositionY()) +
                " \n Number of shots: "+ this.shotCounter+"\n\n");
    }

    /**
     * adds the panel with the buttons to the frame
     */
    public void addPanel() {
        this.gridPane.setPrefSize(200, FRAME_HEIGHT);
        this.gridPane.setStyle("-fx-background-color: darkgrey");
        this.gridPane.setTranslateX(FRAME_WIDTH-200);
        this.gridPane.setHgap(10);
        this.gridPane.setVgap(10);
        this.gridPane.setAlignment(Pos.CENTER);

        Font font = new Font("Verdana", 14);
        Font bigFont = new Font("Verdana", 20);

        Text title = new Text("Golf Game \n- group 6");
        title.setFill(Color.WHITE);
        title.setFont(bigFont);
        gridPane.add(new HBox(30, title), 0, 0);

        Text gap1 = new Text("");
        gridPane.add(new HBox(30, gap1), 0, 1);

        this.text= new Text(" X-position:  " + String.format("%.2f" , universe.getBall().getPositionX() )  +
                " \n Y-position:  " + String.format("%.2f" , universe.getBall().getPositionY()) +
                " \n Number of shots: "+ this.shotCounter+"\n\n");
        this.text.setFill(Color.WHITE);
        this.text.setFont(font);
        gridPane.add(new HBox(30, text), 0, 2);

        Text gap2 = new Text("");
        gridPane.add(new HBox(30, gap2), 0, 3);

        Text gap3 = new Text("");
        gridPane.add(new HBox(30, gap3), 0, 6);

        Text readFromTextField = new Text("Type in\ninitial velocity:");
        readFromTextField.setFill(Color.WHITE);
        readFromTextField.setFont(font);
        gridPane.add(new HBox(30, readFromTextField), 0, 6);

        Text x = new Text("Velocity in x-direction:");
        x.setFill(Color.WHITE);
        x.setFont(font);
        gridPane.add(new HBox(30, x), 0, 7);

        TextField xVel = new TextField();
        gridPane.add(new HBox(30, xVel), 0, 8);

        Text y = new Text("Velocity in y-direction:");
        y.setFill(Color.WHITE);
        y.setFont(font);
        gridPane.add(new HBox(30, y), 0, 10);

        TextField yVel = new TextField();
        gridPane.add(new HBox(30, yVel), 0, 11);

        Button button = new Button("Hit the ball");
        gridPane.add(new HBox(30, button), 0, 13);

        Button resetButton = new Button("Reset ball");
        gridPane.add(new HBox(30, resetButton), 0, 15);

        resetButton.setOnMouseClicked(mouseEvent -> {
            if (!universe.getBall().isMoving() )
                universe.resetBall();
            universe.updateBallPosition();
        });

        button.setOnMouseClicked(mouseEvent -> {
            Vector2D velocity;

            // if the textFields were filled read the initial velocity from them
            if (!Objects.equals(xVel.getText(), "") && !Objects.equals(yVel.getText(), ""))
            {
                int xV = Integer.parseInt(xVel.getText());
                int yV = Integer.parseInt(yVel.getText());
                velocity = new Vector2D(xV, yV);
            }
            // read the velocity from the file shot.txt
            else {
                velocity = universe.getFileReader().getNextShotFromFile();
            }
            // no more shots left in the file shot.txt
            if (velocity != null) {
                new Shot(universe, velocity);
                this.shotCounter++;
            }
                updatePanel();
        });
    }


}