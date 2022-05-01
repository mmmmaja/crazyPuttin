package graphics;

import Main.Universe;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.PickResult;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import objects.Obstacle;
import objects.Spline;
import physics.Vector2D;

public class TerrainEventHandler {

    private final Universe universe;
    private final SmartGroup group;
    private final PhongMaterial rockMaterial;

    private PickResult pickResult;
    private Spline spline;

    public TerrainEventHandler(Universe universe, SmartGroup group) {
        this.universe = universe;
        this.group = group;
        this.rockMaterial = createRockMaterial();
        this.spline = null;

        mousePressed();
        mouseDragged();
        mouseReleased();
    }

    private void mousePressed() {

        universe.getMeshViews()[0].setOnMousePressed(mouseEvent -> {

            // 3D point in the scene where the mouse was clicked
            this.pickResult = mouseEvent.getPickResult();
            //System.out.println("pressed: "+pickResult.getIntersectedPoint());

            Vector2D clickPosition = new Vector2D(
                    this.pickResult.getIntersectedPoint().getX(),
                    this.pickResult.getIntersectedPoint().getY()
            );

            // OBSTACLE events
            if (group.getObstaclesOn()) {

                Obstacle obstacle = new Obstacle(clickPosition);
                Box box = obstacle.getBox();
                box.setMaterial(rockMaterial);
                this.group.getChildren().add(box);
                universe.addObstacle(obstacle);
            }

            // SPLINE events
            else if (group.getSplineOn()) {
                this.spline = new Spline(clickPosition);
                universe.addSplines(this.spline);
            }
        });
    }


    private void mouseDragged() {
        double draggingFactor = 1;

        universe.getMeshViews()[0].setOnMouseDragged(mouseEvent -> {

            // start dragging the terrain
            if (this.group.getSplineOn()) {
                PickResult newPickResult = mouseEvent.getPickResult();

                double deltaZ = this.pickResult.getIntersectedPoint().getZ() - newPickResult.getIntersectedPoint().getZ();

//                System.out.println("old: "+this.pickResult.getIntersectedPoint().getZ());
//                System.out.println("new: "+newPickResult.getIntersectedPoint().getZ());
//                System.out.println(deltaZ+"\n");
                this.spline.addHeight(deltaZ * draggingFactor);
                this.pickResult = newPickResult;
            }
        });
    }

    private void mouseReleased() {

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
}
