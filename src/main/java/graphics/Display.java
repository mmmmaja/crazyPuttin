package graphics;

import Main.Main;
import Main.Shot;
import bot.HillClimbingBot;
import bot.RandomBot;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javafx.stage.Stage;
import Main.Universe;
import objects.Obstacle;
import objects.SplineDragger;
import objects.Tree;
import physics.*;

import java.util.ArrayList;
import java.util.Objects;


/**
 * TODO remove the canvas and enable shooting the ball on the display
 * TODO how to translate the click of the mouse to the actual location of the point at the 3D terrain??? (Splines, obstacles)
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

        addTrees();

        // add the flag to the display (pole and the material)
        addFlag();

        addObstacles();

        // add rest of the objects to the SmartGroup
        this.group.getChildren().add(universe.getBall().getSphere());
        this.group.getChildren().add(universe.getTarget().getCylinder());


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

        // adds panel at the right-hand side with the buttons to the frame
        addPanel();

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
        this.gridPane.add(this.canvas, 0, 3);
    }


    /**
     * add the flag to the display
     * (the pole: cylinder object and the material: the box)
     */
    private void addFlag() {
        Image flagImage = new Image("file:src/main/java/resources/flag.jpg" ,5,5,false,false);
        PhongMaterial flagMaterial = new PhongMaterial();
        flagMaterial.setDiffuseMap(flagImage);

        universe.getFlag().getBox().setMaterial(flagMaterial);
        this.group.getChildren().add(universe.getPole().getCylinder());
        this.group.getChildren().add(universe.getFlag().getBox());
    }


    /**
     * add the trees to the display: the root (cylinder) and the leafs (sphere)
     */
    private void addTrees() {
        for (Tree tree : this.universe.getTrees()) {

            Cylinder cylinder = tree.getCylinder();
            Image cylinderImage = new Image("file:src/main/java/resources/Bark Dark_3D_p.png", 0.3, 0.01, false, false);
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

        Image grassImage = new Image("file:src/main/java/resources/grass.jpg" ,5,5,false,false);
        Image waterImage = new Image("file:src/main/java/resources/water.jpg" ,5,5,false,false);
        Image sandImage = new Image("file:src/main/java/resources/sand.jpg" ,5,5,false,false);

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
     * adds the panel with the buttons to the frame
     */
    public void addPanel() {

        // create the gridPane
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
        gridPane.add(new HBox(30, title), 0, 0);

        textPosition = new Text("Number of shots: " + shotCounter +
                "\nNumber of points: " + pointCounter +
                "\n\nX-position:  " + String.format("%.2f", universe.getBall().getPosition().getX())  +
                "\nY-position:  " + String.format("%.2f", universe.getBall().getPosition().getY()));
        textPosition.setFill(Color.WHITE);
        textPosition.setFont(font);

        this.gridPane.add(new HBox(30, textPosition), 0, 2);

        // add clear canvas for arrow visualization
        // createCanvas();

        int position = 3;

        Text xVelocity = new Text("Velocity in x-direction:");
        xVelocity.setFill(Color.WHITE);
        xVelocity.setFont(font);
        gridPane.add(new HBox(30, xVelocity), 0, position);

        Text yVelocity = new Text("Velocity in y-direction:");
        yVelocity.setFill(Color.WHITE);
        yVelocity.setFont(font);
        this.gridPane.add(new HBox(30, yVelocity), 0, position+=2);

        position = addBallShootingOptions(position);
        position = addBotButtons(position);
        addSplines(position);
    }

    /**
     * TODO
     */
    private void addSplines(int position) {
        CheckBox checkBox = new CheckBox("add splines");
        checkBox.setSelected(false);
        this.gridPane.add(checkBox, 0, position + 1);
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    for (SplineDragger splineDragger : universe.getSplineDraggers()) {
                        group.getChildren().add(splineDragger.getModel());

                        splineDragger.getModel().setOnMousePressed(mouseEvent -> {
                            splineDragger.setColor(Color.PALEGOLDENROD);

                        });
                        splineDragger.getModel().setOnMouseReleased(mouseEvent -> {
                            splineDragger.resetColor();
                        });
                    }
                }
                else {
                    for (SplineDragger splineDragger : universe.getSplineDraggers()) {
                        group.getChildren().remove(splineDragger.getModel());
                    }
                }
                System.out.println(oldValue);
                System.out.println(newValue);
            }
        });
    }

    /**
     * updates x and y position of the ball on the panel
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
     * add the buttons to shoot and reset the ball
     * creates the comboBoxes for different solver
     * and textiles to display the velocity of the ball
     */
    private int addBallShootingOptions(int position) {

        // add the textFields to display the velocity of the ball
        TextField xVel = new TextField();
        gridPane.add(new HBox(30, xVel), 0, position - 1);
        TextField yVel = new TextField();
        gridPane.add(new HBox(30, yVel), 0, position+=2);
        position++;

        Button hitTheBall = new Button("Hit the ball");
        gridPane.add(new HBox(30, hitTheBall), 0, position++);
        Button resetButton = new Button("Reset ball");
        gridPane.add(new HBox(30, resetButton), 0, position++);

        // each time resetButton is clicked ball is set back to the initial position
        resetButton.setOnMouseClicked(mouseEvent -> {
            if (!universe.getBall().isMoving()) {
                universe.resetBall();
                universe.updateBallPosition();
                updatePanel(universe.getBall().getPosition().getX(),universe.getBall().getPosition().getY());
            }
        });

        // add the box with the solver options
        String[] solverList = {"RK4" , "RK2" , "Euler","Heuns3"};
        ComboBox<String> solverComboBox = new ComboBox(FXCollections.observableArrayList(solverList));
        solverComboBox.setValue("RK4");
        gridPane.add(new HBox(30, solverComboBox), 0, position++);

        hitTheBall.setOnMouseClicked(mouseEvent -> {
            if(solverComboBox.getValue().equals("RK4")) universe.setSolver( new RK4());
            if(solverComboBox.getValue().equals("RK2")) universe.setSolver( new RK2());
            if(solverComboBox.getValue().equals("Euler")) universe.setSolver( new Euler());
            if(solverComboBox.getValue().equals("Heuns3")) universe.setSolver( new Heuns3());
            shootBall(xVel, yVel);
        });
        return position;
    }


    /**
     * adds buttons that trigger the specific bot based on the comboBox
     */
    private int addBotButtons(int position) {

        Button botButton = new Button("bot");
        gridPane.add(new HBox(30, botButton), 0, position++);

        String[] botList = {"randomBot" , "hillClimbingBot"};
        ComboBox<String> botComboBox = new ComboBox(FXCollections.observableArrayList(botList));
        botComboBox.setValue("randomBot");
        gridPane.add(new HBox(30, botComboBox), 0, position++);

        botButton.setOnMouseClicked(mouseEvent -> {
            Vector2D velocity = new Vector2D(0, 0);

            if (botComboBox.getValue().equals("randomBot")) {
                velocity = new RandomBot(this.universe).getBestVelocity();
            }
            if (botComboBox.getValue().equals("hillClimbingBot")) {
                velocity = new HillClimbingBot(this.universe).getBestVelocity();
            }
            new Shot(universe, velocity);
            shotCounter++;
        });
        return position;
    }


    private double anchorX;
    private double anchorY;

    private void addObstacles() {

        ArrayList<Obstacle> obstacles = this.universe.getObstacles();
        for (Obstacle obstacle : obstacles) {

            // apply the texture for the obstacle
            Image rockImage = new Image("file:src/main/java/resources/rockTexture.jpg");
            PhongMaterial phongMaterial = new PhongMaterial();
            phongMaterial.setDiffuseMap(rockImage);
            Box box = obstacle.getBox();
            box.setMaterial(phongMaterial);

            this.group.getChildren().add(box);

            // when the obstacle is clicked enable dragging it and disable scene rotation
            obstacle.getBox().setOnMousePressed(mouseEvent -> {
                this.group.setObstaclesOn(true);
                this.anchorX = mouseEvent.getSceneX();
                this.anchorY = mouseEvent.getSceneY();

            });

            // drag the obstacle: TODO fix the movement based on the rotation of the scene
            obstacle.getBox().setOnMouseDragged(mouseEvent -> {

                Vector2D mouseClick = new Vector2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());

                double deltaX = -(this.anchorX - mouseClick.getX());
                double deltaY = -(this.anchorY - mouseClick.getY());
                this.anchorX += deltaX;
                this.anchorY += deltaY;
                obstacle.move(deltaX, deltaY); // works but very poorly
            });

            obstacle.getBox().setOnMouseReleased(mouseEvent -> {
                this.group.setObstaclesOn(false);
                System.out.println(this.group.getSceneRotation());
            });
        }

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