package graphics;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.transform.Rotate;



public class SmartGroup extends Group {

    private static final double zoomFactor = 1.2;

    // starting point for x and y
    private double anchorX, anchorY;

    // current X and Y angle
    private double anchorAngleX, anchorAngleY;

    // update these after dragging the mouse
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);


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

        this.getTransforms().addAll(
                rotateX = new Rotate(0, Rotate.X_AXIS),
                rotateY = new Rotate(0, Rotate.Y_AXIS)
        );
        rotateX.angleProperty().bind(angleX);
        rotateY.angleProperty().bind(angleY);

        scene.setOnMousePressed(mouseEvent -> {
            this.anchorX = mouseEvent.getSceneX();
            this.anchorY = mouseEvent.getSceneY();
            this.anchorAngleX = angleX.get();
            this.anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(mouseEvent -> {
            this.angleX.set(this.anchorAngleX - (this.anchorY - mouseEvent.getSceneY()));
            this.angleY.set(this.anchorAngleY + this.anchorX - mouseEvent.getSceneX());
        });
    }
}
