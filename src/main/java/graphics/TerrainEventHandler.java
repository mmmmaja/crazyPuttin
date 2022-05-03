package graphics;

import Main.Universe;
import javafx.scene.image.Image;
import javafx.scene.input.PickResult;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import objects.Obstacle;
import objects.Spline;
import physics.Vector2D;


/**
 * Handles the events when the mouse is clicked on the terrain
 *      adding obstacles
 *      is supposed to work with splines later as well (dragging the mouse)
 */
public class TerrainEventHandler {

    private final Universe universe;
    private final SmartGroup group;
    private final PhongMaterial rockMaterial;

    private Spline spline;
    private Vector2D clickPosition;


    public TerrainEventHandler(Universe universe, SmartGroup group) {
        this.universe = universe;
        this.group = group;
        this.rockMaterial = createRockMaterial();
        this.spline = null;

        mousePressed();
        mouseDragged();
        mouseReleased();
    }


    /**
     * add the obstacle on the position clicked
     */
    private void mousePressed() {

        universe.getMeshViews()[0].setOnMousePressed(mouseEvent -> {

            // 3D point in the scene where the mouse was clicked
            PickResult pickResult = mouseEvent.getPickResult();
            this.clickPosition = new Vector2D(mouseEvent.getX(), mouseEvent.getY());

            Vector2D clickPosition = new Vector2D(
                    pickResult.getIntersectedPoint().getX(),
                    pickResult.getIntersectedPoint().getY()
            );

            // OBSTACLE events
            if (group.getObstaclesOn()) {

                // if not a target or a ball
                if (!collides(clickPosition)) {

                    Obstacle obstacle = new Obstacle(clickPosition);
                    Box box = obstacle.getBox();
                    box.setMaterial(rockMaterial);
                    this.group.getChildren().add(box);
                    universe.addObstacle(obstacle);
                }
            }

            // SPLINE events
            else if (group.getSplineOn()) {
                this.spline = new Spline(clickPosition);
                universe.addSplines(this.spline);
            }
        });
    }


    /**
     * create the splines
     */
    private void mouseDragged() {
        double draggingFactor = 0.5;

        universe.getMeshViews()[0].setOnMouseDragged(mouseEvent -> {

            // start dragging the terrain
            if (this.group.getSplineOn()) {

                Vector2D newClickPosition = new Vector2D(mouseEvent.getX(), mouseEvent.getY());

                double deltaHeight = this.group.getSceneAngle() * (this.clickPosition.getY() - newClickPosition.getY());
                this.spline.addHeight(deltaHeight * draggingFactor);
                this.clickPosition = newClickPosition;

                Sphere sphere = new Sphere();
                sphere.setTranslateX(this.spline.getPosition().getX());
                sphere.setTranslateY(this.spline.getPosition().getY());
                sphere.setTranslateZ(this.spline.getHeight());
                sphere.setRadius(0.4);
                this.group.getChildren().add(sphere);
            }
        });
    }

    /**
     * does nothing for now
     */
    private void mouseReleased() {
        universe.getMeshViews()[0].setOnMouseReleased(mouseEvent -> {
            //System.out.println("spline "+this.universe.getSplines().size()+": "+ this.spline.getHeight());
        });

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
