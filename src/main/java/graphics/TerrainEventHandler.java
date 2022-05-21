package graphics;

import Main.Main;
import Main.Universe;
import javafx.scene.image.Image;
import javafx.scene.input.PickResult;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import objects.Obstacle;
import objects.Tree;
import physics.Vector2D;


/**
 * Handles the events when the mouse is clicked on the terrain
 *      adding obstacles
 *      adding trees
 *      is supposed to work with splines later as well (dragging the mouse)
 */
public class TerrainEventHandler {

    private final Universe universe;
    private final SmartGroup group;
    private final PhongMaterial rockMaterial;

    public TerrainEventHandler(SmartGroup group) {
        this.universe = Main.getUniverse();
        this.group = group;
        this.rockMaterial = createRockMaterial();

        mousePressed();
        mouseDragged();
        mouseReleased();
    }


    /**
     * add the obstacle or tree on the position clicked
     */
    private void mousePressed() {

        universe.getMeshViews()[0].setOnMousePressed(mouseEvent -> {

            // 3D point in the scene where the mouse was clicked
            PickResult pickResult = mouseEvent.getPickResult();


            Vector2D clickPosition = new Vector2D(
                    pickResult.getIntersectedPoint().getX(),
                    pickResult.getIntersectedPoint().getY()
            );
            // OBSTACLE events
            if (group.getObstaclesOn()) {

                // if not a target or a ball
                if (!collides(clickPosition, 0.6)) {

                    Obstacle obstacle = new Obstacle(clickPosition);
                    System.out.println(clickPosition);
                    Box box = obstacle.getBox();
                    box.setMaterial(rockMaterial);
                    this.group.getChildren().add(box);
                    universe.addObstacle(obstacle);
                }
            }

            // OBSTACLE events
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
     * create the splines
     */
    private void mouseDragged() {
        universe.getMeshViews()[0].setOnMouseDragged(mouseEvent -> {});
    }


    /**
     * does nothing for now
     */
    private void mouseReleased() {
        universe.getMeshViews()[0].setOnMouseReleased(mouseEvent -> {});

    }


    /**
     * creates the material for the obstacles
     */
    private PhongMaterial createRockMaterial() {
        Image rockImage = new Image("file:src/main/java/resources/rockTexture.jpg");
        PhongMaterial rockMaterial = new PhongMaterial();
        rockMaterial.setDiffuseMap(rockImage);
        return rockMaterial;
    }

    /**
     *
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
