package graphics;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.transform.Rotate;

/**
 * class used for holding all the objects added to the display
 * provides rotation and zooming-in and zooming-out when mouse is moved
 */
public class SmartGroup extends Group {

    private static final double zoomFactor = 1.2;

    // starting point for x and y
    private double anchorX, anchorY;

    // current X and Y angle
    private double anchorAngleX, anchorAngleY;

    // update these after dragging the mouse
    private final DoubleProperty angleX = new SimpleDoubleProperty(-50);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private final DoubleProperty angleZ = new SimpleDoubleProperty(0);


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
                rotateX = new Rotate(0, Rotate.X_AXIS),
                rotateY = new Rotate(0, Rotate.Y_AXIS),
                rotateZ = new Rotate(0, Rotate.Z_AXIS)
        );
        rotateX.angleProperty().bind(angleX);
        rotateY.angleProperty().bind(angleY);
        rotateZ.angleProperty().bind(angleZ);

        scene.setOnMousePressed(mouseEvent -> {
            this.anchorX = mouseEvent.getSceneX();
            this.anchorY = mouseEvent.getSceneY();

            this.anchorAngleX = angleX.get();
            this.anchorAngleY = angleZ.get();
        });

        scene.setOnMouseDragged(mouseEvent -> {
            // lock the X rotation, so we can see under the surface
            if (this.anchorAngleX - (this.anchorY - mouseEvent.getSceneY()) > -68.0) {
                if (this.anchorAngleX - (this.anchorY - mouseEvent.getSceneY()) < 60) {
                    this.angleX.set(this.anchorAngleX - (this.anchorY - mouseEvent.getSceneY()));
                }
            }
            this.angleZ.set(this.anchorAngleY + this.anchorX - mouseEvent.getSceneX());

        });
    }
}
