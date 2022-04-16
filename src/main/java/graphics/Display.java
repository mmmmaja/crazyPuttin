package graphics;

import Main.Main;
import Main.Shot;
import bot.HillClimbingBot;
import bot.RandomBot;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javafx.stage.Stage;
import Main.Universe;
import objects.Tree;
import physics.*;

import java.util.Objects;


/**
 * TODO add button for the bot
 * TODO remove the canvas and enable shooting the ball on the display
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

    private SmartGroup group;


    @Override
    public void start(Stage stage) {

        // all the objects are added to smartGroup (rotation built-in)
        group = new SmartGroup();

        // add lighting to the scene
        addLight();

        // add three types of meshes from the display that represent the terrain
        addTerrainMeshes();

        // TODO add the tree to the display
        addTrees();

        // add the flag to the display (pole and the material)
        addFlag();

        // add rest of the objects to the SmartGroup
        this.group.getChildren().add(universe.getBall().getSphere());
        this.group.getChildren().add(universe.getTarget().getCylinder());

        // add clear canvas for arrow visualization
        createCanvas();

        // displayPane hold all other panes of the frame
        Pane displayPane = new Pane();
        displayPane.setPrefSize(FRAME_WIDTH, FRAME_HEIGHT);

        // adds the subScene that holds terrain display (left part of the frame)
        SubScene subScene = new SubScene(group, FRAME_WIDTH - 200, FRAME_HEIGHT, true, null);
        subScene.setFill(Color.web("#3d3d3d"));

        // gridPane contains buttons and information about the state of the game (right part of the frame)
        this.gridPane = new GridPane();
        this.gridPane.setTranslateX(FRAME_WIDTH - 200);
        displayPane.getChildren().add(this.gridPane);

        // adds panel at the right-hand side with the buttons to the frame
        addPanel();

        // add the camera to the scene
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
        this.group.translateXProperty().set((FRAME_WIDTH + 250) / 3.0);
        this.group.translateYProperty().set((FRAME_HEIGHT + 305) / 3.0);
        this.group.zoom(camera.getTranslateY() + 900); // zoomIn
        this.group.initMouseControl(scene);
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
     * create Canvas for arrow visualisation
     */
    private void createCanvas() {
        this.canvas = new Canvas(150,120);
        // enable drawing on the canvas
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        clearCanvas(this.canvas, graphicsContext);

        // whenever the canvas is pressed draw an arrow to the pointer's direction
        canvas.setOnMousePressed(e -> mouseClicked(e, canvas, graphicsContext));
    }


    /**
     * add the flag to the display
     * (the pole: cylinder object and the material: the box)
     */
    private void addFlag() {
        Image flagImage = new Image("file:src/main/java/flag.jpg" ,5,5,false,false);
        PhongMaterial flagMaterial = new PhongMaterial();
        flagMaterial.setDiffuseMap(flagImage);

        universe.getFlag().getBox().setMaterial(flagMaterial);
        this.group.getChildren().add(universe.getPole().getCylinder());
        this.group.getChildren().add(universe.getFlag().getBox());
    }


    /**
     * TODO
     */
    private void addTrees() {
        for (Tree tree : this.universe.getTrees()) {

            // add the root of the tree (the cylinder object)
            Cylinder cylinder = tree.getCylinder();
            Image cylinderImage = new Image("file:src/main/java/Bark Dark_3D_p.png", 0.3, 0.01, false, false);
            PhongMaterial cylinderMaterial = new PhongMaterial();
            cylinderMaterial.setDiffuseMap(cylinderImage);
            cylinder.setMaterial(cylinderMaterial);

            group.getChildren().add(cylinder);
            group.getChildren().add(tree.getSphere());
        }
    }


    /**
     * add water, sandPit and grass meshes to the display
     */
    private void addTerrainMeshes() {

        Image grassImage = new Image("file:src/main/java/grass.jpg" ,5,5,false,false);
        Image waterImage = new Image("file:src/main/java/water.jpg" ,5,5,false,false);
        Image sandImage = new Image("file:src/main/java/sand.jpg" ,5,5,false,false);

        PhongMaterial grassMaterial = new PhongMaterial();
        PhongMaterial waterMaterial = new PhongMaterial();
        PhongMaterial sandMaterial = new PhongMaterial();

        grassMaterial.setDiffuseMap(grassImage);
        waterMaterial.setDiffuseMap(waterImage);
        sandMaterial.setDiffuseMap(sandImage);

        MeshView[] terrainMeshViews = universe.getMeshViews();
        terrainMeshViews[0].setMaterial(grassMaterial);
        terrainMeshViews[1].setMaterial(waterMaterial);
        terrainMeshViews[2].setMaterial(sandMaterial);

        for (MeshView meshView : terrainMeshViews) {
            this.group.getChildren().add(meshView);
        }
    }


    /**
     * add the lighting to the scene: ambient light and point light
     */
    private void addLight() {
        AmbientLight ambientLight = new AmbientLight();
        PointLight pointLight = new PointLight();
        ambientLight.setColor(Color.rgb(191, 191, 191,0.6));
        pointLight.setColor(Color.rgb(255, 255, 255,0.1));
        pointLight.setLayoutX(1000);
        pointLight.setLayoutY(1000);
        pointLight.setTranslateZ(-1100);
        group.getChildren().addAll(pointLight, ambientLight);
    }


    /**
     * updates x and y position of the ball
     * update counter of the shoots and point counter
     * @param x position x of the ball
     * @param y position y of the ball
     */
    public static void updatePanel(double x, double y) {
        textPosition.setText("Number of shots: " + shotCounter +
                "\nNumber of points: " + pointCounter +
                "\n\nX-position:  " + String.format("%.5f", x)  +
                "\nY-position:  " + String.format("%.5f", y));
    }


    /**
     * adds the panel with the buttons to the frame
     */
    public void addPanel() {
        this.gridPane.setPrefSize(200, FRAME_HEIGHT);
        this.gridPane.setStyle("-fx-background-color: darkgrey");
        this.gridPane.setTranslateX(FRAME_WIDTH-200);
        this.gridPane.setHgap(8);
        this.gridPane.setVgap(8);
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

        // each time resetButton is clicked ball is set back to the initial position
        resetButton.setOnMouseClicked(mouseEvent -> {
            if (!universe.getBall().isMoving()) {
                universe.resetBall();
                universe.updateBallPosition();
                updatePanel(universe.getBall().getPosition().getX(),universe.getBall().getPosition().getY());
            }
        });

        String[] list = {"RK4" , "RK2" , "Euler","Heuns3"};
        ComboBox<String> comboBox = new ComboBox(FXCollections.observableArrayList(list));
        comboBox.setValue("RK4");
        gridPane.add(new HBox(30, comboBox), 0, position + 10);
        button.setOnMouseClicked(mouseEvent -> {
            if(comboBox.getValue().equals("RK4")) universe.setSolver( new RK4());
            if(comboBox.getValue().equals("RK2")) universe.setSolver( new RK2());
            if(comboBox.getValue().equals("Euler")) universe.setSolver( new Euler());
            if(comboBox.getValue().equals("Heuns3")) universe.setSolver( new Heuns3());
            shootBall(xVel, yVel);
        });

        addBotButtons(position);
    }


    /**
     * adds buttons that trigger the specific bot
     * @param position where to place the button
     */
    private void addBotButtons(int position) {
        Button randomBotButton = new Button("random Bot");
        this.gridPane.add(new HBox(30, randomBotButton), 0, position + 7);

        randomBotButton.setOnMouseClicked(mouseEvent -> {
            Vector2D velocity = new RandomBot(this.universe).getBestVelocity();
            new Shot(universe, velocity);
            shotCounter++;
        });

        Button hillClimbingBotButton = new Button("hill climbing Bot");
        this.gridPane.add(new HBox(30, hillClimbingBotButton), 0, position + 8);

        hillClimbingBotButton.setOnMouseClicked(mouseEvent -> {
            Vector2D velocity = new HillClimbingBot(this.universe).getBestVelocity();
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