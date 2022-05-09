package graphics;

import Main.Universe;
import javafx.scene.image.Image;
import javafx.scene.input.PickResult;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import objects.Obstacle;
import physics.Vector2D;


/**
 * TODO add obstacles on mouse dragged
 * Handles the events when the mouse is clicked on the terrain
 *      adding obstacles
 *      is supposed to work with splines later as well (dragging the mouse)
 */
public class TerrainEventHandler {

    private final Universe universe;
    private final SmartGroup group;
    private final PhongMaterial rockMaterial;

    public TerrainEventHandler(Universe universe, SmartGroup group) {
        this.universe = universe;
        this.group = group;
        this.rockMaterial = createRockMaterial();

        mousePressed();
        mouseDragged();
        mouseReleased();
    }


    /**
     * add the obstacle on the position clicked
     */
    private void mousePressed() {

        universe.getMeshViews()[0].setOnMousePressed(mouseEvent -> {});
    }


    /**
     * create the splines
     */
    private void mouseDragged() {
        universe.getMeshViews()[0].setOnMouseDragged(mouseEvent -> {

            // 3D point in the scene where the mouse was clicked
            PickResult pickResult = mouseEvent.getPickResult();


            Vector2D clickPosition = new Vector2D(
                    pickResult.getIntersectedPoint().getX(),
                    pickResult.getIntersectedPoint().getY()
            );
            // OBSTACLE events
            if (group.getObstaclesOn()) {

                // if not a target or a ball
                if (!collides(clickPosition) && !(ifClickedInCentre(clickPosition))) {

                    Obstacle obstacle = new Obstacle(clickPosition);
                    Box box = obstacle.getBox();
                    box.setMaterial(rockMaterial);
                    this.group.getChildren().add(box);
                    universe.addObstacle(obstacle);
                }
            }
        });
    }

    /**
     * does nothing for now
     */
    private void mouseReleased() {
        universe.getMeshViews()[0].setOnMouseReleased(mouseEvent -> {});

    }

    private boolean ifClickedInCentre(Vector2D clickPosition) {
        double epsilon = 0.7;
        return clickPosition.getX() < epsilon && clickPosition.getY() < epsilon;
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
    private boolean collides(Vector2D clickPosition) {

        if (this.universe.getTarget().getEuclideanDistance(clickPosition) < 0.6) {
            return true;
        }
        return this.universe.getBall().getPosition().getEuclideanDistance(clickPosition) < 0.6;
    }


}
