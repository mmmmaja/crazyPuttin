package graphics;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.transform.Rotate;
import physics.Vector2D;

/**
 * Class used for holding all the objects added to the display
 * provides rotation and zooming-in and zooming-out when mouse is moved
 */
public class SmartGroup extends Group {

    private static final double zoomFactor = 1.2;

    // indicates whether scene can be rotated: rotation is disabled when arrow is drowned on canvas
    private boolean arrowOn = false;

    // indicates whether we can draw the obstacles on the screen
    private boolean obstaclesOn = false;

    // indicates whether we can add the trees on the screen
    private boolean treesOn = false;

    // indicates whether we can draw the obstacles on the screen
    private boolean splineOn = false;

    // starting point for x and y
    private double anchorX, anchorY;

    // current X and Y angle
    private double anchorAngleX, anchorAngleY;

    // update these after dragging the mouse
    private final DoubleProperty angleX = new SimpleDoubleProperty(-68);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private final DoubleProperty angleZ = new SimpleDoubleProperty(-20);


    /**
     * zoomIn and zoomOut on mouse scroll
     */
    public void zoom(double deltaY) {
        deltaY *= -zoomFactor;
        this.translateZProperty().set(this.getTranslateZ() + deltaY);
    }


    /**
     * add rotation when dragging the mouse
     */
    public void initMouseControl(Scene scene) {
        Rotate rotateX;
        Rotate rotateY;
        Rotate rotateZ;

        this.getTransforms().addAll(
                rotateX = new Rotate(0,0,0,0, Rotate.X_AXIS),
                rotateY = new Rotate(0,0,0,0, Rotate.Y_AXIS),
                rotateZ = new Rotate(0,0,0,0, Rotate.Z_AXIS)
        );

        rotateX.angleProperty().bind(angleX);
        rotateY.angleProperty().bind(angleY);
        rotateZ.angleProperty().bind(angleZ);

        scene.setOnMousePressed(mouseEvent -> {
            // rotate the scene
            if (!this.arrowOn && !this.obstaclesOn) {
                this.anchorX = mouseEvent.getSceneX();
                this.anchorY = mouseEvent.getSceneY();

                this.anchorAngleX = angleX.get();
                this.anchorAngleY = angleZ.get();
            }

        });

        scene.setOnMouseDragged(mouseEvent -> {
            if (!this.arrowOn && !this.obstaclesOn && !this.splineOn) {
                // lock the X rotation, so we can see under the surface
                if (this.anchorAngleX - (this.anchorY - mouseEvent.getSceneY()) > -68.0) {
                    if (this.anchorAngleX - (this.anchorY - mouseEvent.getSceneY()) < 5) {
                        this.angleX.set(this.anchorAngleX - (this.anchorY - mouseEvent.getSceneY()));
                    }
                }
                this.angleZ.set(this.anchorAngleY + this.anchorX - mouseEvent.getSceneX());
            }
        });
    }

    public void setObstaclesOn(boolean obstaclesOn) {
        this.obstaclesOn = obstaclesOn;
    }

    public boolean getObstaclesOn() {
        return this.obstaclesOn;
    }

    public void setTreesOn(boolean treesOn) {
        this.treesOn = treesOn;
    }

    public boolean getTreesOn() {
        return this.treesOn;
    }

}
