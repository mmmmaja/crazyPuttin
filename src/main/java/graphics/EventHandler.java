package graphics;

import Main.Main;
import Main.Universe;
import bot.mazes.RecursiveMaze;
import javafx.scene.Camera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.PickResult;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import objects.Obstacle;
import objects.Tree;
import physics.Vector2D;

import java.util.ArrayList;


/**
 * Handles the events when the mouse is clicked on the terrain or the key is pressed
 *      adding obstacles
 *      adding trees
 *      adding mazes
 *      clearing all obstacles
 */
public class EventHandler {

    private final Universe universe = Main.getUniverse();
    private final SmartGroup group;
    private final PhongMaterial rockMaterial;

    public EventHandler(SmartGroup group) {
        this.group = group;
        this.rockMaterial = createRockMaterial();

        mousePressed();
    }

    /**
     * @param scene current scene with all the objects added
     * @param camera to be translated on the mouse press
     */
    public void handleCamera(Scene scene, Camera camera) {

        // add movement of the camera when keys are pressed
        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            switch (e.getCode()) {
                case W -> camera.setTranslateY(camera.getTranslateY() + 1);
                case S -> camera.setTranslateY(camera.getTranslateY() - 1);
                case D -> camera.setTranslateX(camera.getTranslateX() + 1);
                case A -> camera.setTranslateX(camera.getTranslateX() - 1);

                // add a maze
                case M -> {
                    RecursiveMaze recursiveMaze = new RecursiveMaze();
                    ArrayList<Obstacle> obstacles = recursiveMaze.getObstacles();
                    for (Obstacle obstacle : obstacles) {
                        universe.addObstacle(obstacle);
                        Box box = obstacle.getBox();
                        box.setMaterial(rockMaterial);
                        this.group.getChildren().add(box);
                    }
                }

                // remove maze and visualizers for A*
                case C -> {
                    for (Obstacle obstacle : universe.getObstacles()) {
                        this.group.getChildren().remove(obstacle.getBox());
                    }
                    for (Sphere sphere : universe.getPathVisualizations()) {
                        this.group.getChildren().remove(sphere);
                    }
                    universe.deleteObstacles();
                    universe.deletePathVisualizations();
                }

                // set the level of the maze when pressing numbers on the keyboard
                case DIGIT1 -> RecursiveMaze.step = 2;
                case DIGIT2 -> RecursiveMaze.step = 3;
                case DIGIT3 -> RecursiveMaze.step = 4;
                case DIGIT4 -> RecursiveMaze.step = 5;
                case DIGIT5 -> RecursiveMaze.step = 6;
                case DIGIT6 -> RecursiveMaze.step = 7;
                case DIGIT7,DIGIT8, DIGIT9 -> RecursiveMaze.step = 8;
            }
        });
    }


    /**
     * add the obstacle or tree on the position clicked
     */
    private void mousePressed() {

        universe.getMeshViews()[0].setOnMousePressed(mouseEvent -> {

            // 3D point in the scene where the mouse was clicked
            PickResult pickResult = mouseEvent.getPickResult();


            Vector2D clickPosition = new Vector2D(
                    Math.ceil(pickResult.getIntersectedPoint().getX())-0.5,
                    Math.ceil(pickResult.getIntersectedPoint().getY())-0.5
            );
            // add the OBSTACLE
            if (group.getObstaclesOn()) {

                // if not a target or a ball
                if (!collides(clickPosition, 0.5)) {

                    Obstacle obstacle = new Obstacle(clickPosition);
                    System.out.println(clickPosition);
                    Box box = obstacle.getBox();
                    box.setMaterial(rockMaterial);

                    this.group.getChildren().add(box);
                    universe.addObstacle(obstacle);
                }
            }

            // add a TREE
            else if (group.getTreesOn()) {
                // if not a target or a ball
                if (!collides(clickPosition, 0.1)) {

                    Tree tree = new Tree(3.7, 0.1, clickPosition);
                    this.group.getChildren().add(tree.getSphere());

                    Cylinder cylinder = tree.getCylinder();
                    Image cylinderImage = new Image("file:src/main/java/resources/Bark Dark_3D_p.png", 0.3, 0.01, false, false);
                    PhongMaterial cylinderMaterial = new PhongMaterial();
                    cylinderMaterial.setDiffuseMap(cylinderImage);
                    cylinder.setMaterial(cylinderMaterial);

                    this.group.getChildren().add(cylinder);
                    universe.addTree(tree);
                }
            }
        });
    }


    /**
     * @return the material for the obstacles
     */
    private PhongMaterial createRockMaterial() {
        Image rockImage = new Image("file:src/main/java/resources/rockTexture.jpg");
        PhongMaterial rockMaterial = new PhongMaterial();
        rockMaterial.setDiffuseMap(rockImage);
        return rockMaterial;
    }

    /**
     * @param clickPosition position on the frame where the mouse was clicked
     * @return true if the obstacle would collide with the target or the ball
     */
    private boolean collides(Vector2D clickPosition, double radius) {

        if (this.universe.getTarget().getEuclideanDistance(clickPosition) < radius) {
            return true;
        }
        return this.universe.getBall().getPosition().getEuclideanDistance(clickPosition) < radius;
    }


}
