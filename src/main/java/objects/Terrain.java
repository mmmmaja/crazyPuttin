package objects;

import graphics.Display;
import javafx.scene.shape.TriangleMesh;
import physics.Vector2D;

public class Terrain {

    private final TriangleMesh mesh;

    private static final int TERRAIN_WIDTH = 900;
    private static final int TERRAIN_HEIGHT = 900;
    private static final double STEP = 2;


    public Terrain() {
        this.mesh = new TriangleMesh();

        addPoints();
        addFaces();
    }


   private void addPoints() {

        for (float i = 0; i < TERRAIN_HEIGHT; i+= STEP) {
            for (float j = 0; j < TERRAIN_WIDTH; j+= STEP) {
                this.mesh.getPoints().addAll(i - Display.translateX, j - Display.translateY, (float) TerrainGenerator.getHeight(new Vector2D(i, j)));
            }
        }
   }


    private void addFaces() {

        int column = (int) (TERRAIN_WIDTH / STEP);
        int row = (int) (TERRAIN_HEIGHT / STEP);
        for (int i = 0; i < row - 2; i++) {
            for (int j = 0; j < column; j++) {

                // fixme not sure here
                int topLeft = column * i + j;
                int topRight = topLeft + 1;
                int bottomLeft = topLeft + column;
                int bottomRight = bottomLeft + 1;

                // fixme
                this.mesh.getTexCoords().addAll(topLeft, topLeft, topRight, topRight, bottomLeft, bottomLeft);
                this.mesh.getTexCoords().addAll(bottomLeft, bottomLeft, bottomRight, bottomRight, topRight, topRight);

                this.mesh.getFaces().addAll(topLeft, topLeft, topRight, topRight, bottomLeft, bottomLeft);
                this.mesh.getFaces().addAll(bottomLeft, bottomLeft, bottomRight, bottomRight, topRight, topRight);
            }
        }
    }


   public TriangleMesh getMesh() {
        return this.mesh;
   }


}
