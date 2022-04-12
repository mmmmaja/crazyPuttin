package graphics;

import Main.Main;
import Main.Shot;
import bot.RandomBot;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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


/**
 * TODO add button for the bot
 */
public class Display extends Application {

    public Universe universe = Main.getUniverse();

    public static final int FRAME_WIDTH = 1100;
    public static final int FRAME_HEIGHT = 600;

    private GridPane gridPane;
    private Canvas canvas;

    public static Text textPosition;
    public static int shotCounter = 0;
    public static int pointCounter = 0;

    SmartGroup group ;

    @Override
    public void start(Stage stage) {

        // to smartGroup all the objects are added (rotation built-in)
        group = new SmartGroup();

        // add the lighting to the scene: ambient light and point light
        AmbientLight ambient = new AmbientLight();
        PointLight point = new PointLight();
        ambient.setColor(Color.rgb(191, 191, 191,0.6));
        point.setColor(Color.rgb(255, 255, 255,0.1));
        point.setLayoutX(1000);
        point.setLayoutY(1000);
        point.setTranslateZ(-1100);

        // add water, sandPit and grass meshes to the display
        MeshView[] meshViews = universe.getMeshViews();

        // load the pictures that will be applied as a textures for the objects
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

        // add all the objects to the SmartGroup
        for (MeshView meshView : meshViews) {
            group.getChildren().add(meshView);
        }
        group.getChildren().addAll(point,ambient);
        group.getChildren().add(universe.getBall().getSphere());
        group.getChildren().add(universe.getTarget().getCylinder());
        group.getChildren().add(universe.getPole().getCylinder());
        group.getChildren().add(universe.getFlag().getBox());

        // create Canvas for arrow visualisation
        this.canvas = new Canvas(150,120);
        // enable drawing on the canvas
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        clearCanvas(this.canvas, graphicsContext);

        // whenever the canvas is pressed draw an arrow to the pointer's direction
        canvas.setOnMousePressed(e -> mouseClicked(e, canvas, graphicsContext));

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
        // enable zooming in really close without the disappearance of the objects
        camera.setNearClip(0.0001);
        subScene.setCamera(camera);

        Pane pane3D = new Pane();
        pane3D.setPrefSize(FRAME_WIDTH - 200, FRAME_HEIGHT);
        pane3D.getChildren().add(subScene);
        displayPane.getChildren().add(pane3D);
        Scene scene = new Scene(displayPane);

        // move the all the objects the middle of the frame
        group.translateXProperty().set((FRAME_WIDTH + 250) / 3.0);
        group.translateYProperty().set((FRAME_HEIGHT + 305) / 3.0);
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
     * updates x and y position of the ball
     * update counter of the shoots and point counter
     * @param x position x of the ball
     * @param y position y of the ball
     */
    public static void updatePanel(double x, double y) {
        textPosition.setText("Number of shots: " + shotCounter +
                "\n\nNumber of points: " + pointCounter +
                "\n\n\nX-position:  " + String.format("%.2f", x)  +
                "\nY-position:  " + String.format("%.2f", y));
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

        Text title = new Text("Crazy putting");
        title.setFill(Color.WHITE);
        title.setFont(bigFont);
        gridPane.add(new HBox(30, title), 0, 1);

        textPosition = new Text("Number of shots: " + shotCounter +
                "\nNumber of points: " + pointCounter +
                "\n\nX-position:  " + String.format("%.2f", universe.getBall().getPosition().getX())  +
                "\nY-position:  " + String.format("%.2f", universe.getBall().getPosition().getY()));
        textPosition.setFill(Color.WHITE);
        textPosition.setFont(font);

        gridPane.add(new HBox(30, textPosition), 0, 2);

        this.gridPane.add(this.canvas, 0, 3);

        Text readFromTextField = new Text("Type in\ninitial velocity:");
        readFromTextField.setFill(Color.WHITE);
        readFromTextField.setFont(font);
        int position = 4;

        gridPane.add(new HBox(30, readFromTextField), 0, position);

        Text x = new Text("Velocity in x-direction:");
        x.setFill(Color.WHITE);
        x.setFont(font);
        gridPane.add(new HBox(30, x), 0, position + 1);

        TextField xVel = new TextField();
        gridPane.add(new HBox(30, xVel), 0, position + 2);

        Text y = new Text("Velocity in y-direction:");
        y.setFill(Color.WHITE);
        y.setFont(font);
        gridPane.add(new HBox(30, y), 0, position + 3);

        TextField yVel = new TextField();
        gridPane.add(new HBox(30, yVel), 0, position + 4);

        Button button = new Button("Hit the ball");
        gridPane.add(new HBox(30, button), 0, position + 5);
        Button resetButton = new Button("Reset ball");
        gridPane.add(new HBox(30, resetButton), 0, position + 6);
        Button randomBotButton = new Button("random Bot");
        gridPane.add(new HBox(30, randomBotButton), 0, position + 7);

        // each time resetButton is clicked ball is set back to the initial position
        resetButton.setOnMouseClicked(mouseEvent -> {
            if (!universe.getBall().isMoving()) {
                universe.resetBall();
                universe.updateBallPosition();
                updatePanel(universe.getBall().getPosition().getX(),universe.getBall().getPosition().getY());
            }
        });

        button.setOnMouseClicked(mouseEvent -> {
            shootBall(xVel, yVel);
        });

        randomBotButton.setOnMouseClicked(mouseEvent -> {
            Vector2D velocity = new RandomBot(this.universe).getBestVelocity();
            new Shot(universe, velocity);
            shotCounter++;
        });
    }


    /**
     * triggered when the button is pressed
     * initializes the movement of the ball from the Shot class
     * @param xVel textField with initial x velocity
     * @param yVel textField with initial y velocity
     */
    private void shootBall(TextField xVel, TextField yVel) {
        Vector2D velocity;
        if (!universe.getBall().isMoving()) {

            // if the textFields were filled, then read the initial velocity from them
            if (!Objects.equals(xVel.getText(), "") && !Objects.equals(yVel.getText(), "")) {
                float xV = Float.parseFloat(xVel.getText());
                float yV = Float.parseFloat(yVel.getText());
                velocity = new Vector2D(xV, yV);
            }

            // read the velocity from the file shot.txt
            else {
                velocity = universe.getFileReader().getNextShotFromFile();
            }

            // no more shots left in the file shot.txt
            if (velocity != null) {
                new Shot(universe, velocity);
                shotCounter++;
            }
        }
    }

    /**
     * triggered when the arrow was drawn on canvas
     * @param velocity initial velocity of the ball
     */
    private void shootBall(Vector2D velocity) {
        if (!universe.getBall().isMoving()) {
            new Shot(universe, velocity);
            shotCounter++;
        }
    }


    /**
     * clear canvas each time the arrow changes the position so that there's no multiple arrows on the canvas
     * @param canvas box where arrow is drowned
     * @param g graphic component enabling drawing on the canvas
     */
    public void clearCanvas(Canvas canvas, GraphicsContext g) {

        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g.setStroke(Color.WHITE);
        g.setLineWidth(5.0);
        g.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g.setLineWidth(2.0);
    }


    /**
     * draws the arrow when the canvas was pressed
     * @param startX starting x position
     * @param startY starting y position
     * @param endX ending x position
     * @param endY ending y position
     * @param g graphic component enabling drawing on the canvas
     */
    public void drawArrow(double startX, double startY, double endX, double endY, GraphicsContext g) {

        //drawing arrow body
        g.strokeLine(startX, startY, endX, endY);

        //drawing arrow head
        g.strokeLine(endX, endY, endX-5, endY+5);
        g.strokeLine(endX, endY, endX-5, endY-5);
    }


    /**
     * drawing an arrow if the current position of the ball has been clicked
     * @param mouseEvent mouse clicked on the canvas
     * @param canvas plane where the arrow will be drawn
     * @param g graphics
     */
    public void mouseClicked(MouseEvent mouseEvent, Canvas canvas, GraphicsContext g) {

        this.group.setArrowOn(true);
        double clickedX = mouseEvent.getX();
        double clickedY = mouseEvent.getY();

        canvas.setOnMouseDragged(MouseDragged -> {
            clearCanvas(canvas, g);
            drawArrow(clickedX, clickedY, MouseDragged.getX(), MouseDragged.getY(), canvas.getGraphicsContext2D());
        });

        canvas.setOnMouseReleased(MouseReleased -> {
            this.group.setArrowOn(false);
            double vX = MouseReleased.getX()-clickedX;
            double vY = MouseReleased.getY()-clickedY;

            Vector2D velocity = new Vector2D(vX,vY);
            clearCanvas(canvas,g);
            shootBall(velocity);
        });

    }
}