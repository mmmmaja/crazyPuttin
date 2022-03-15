package objects;

import graphics.Display;
import javafx.scene.shape.TriangleMesh;
import physics.Vector2D;

public class Terrain extends TerrainGenerator {

    private final TriangleMesh grassMesh;
    private final TriangleMesh waterMesh;
    private final TriangleMesh sandPitMesh;
    private final Vector2D sandPitX;
    private final Vector2D sandPitY;

    private static final int TERRAIN_WIDTH = 700;
    private static final int TERRAIN_HEIGHT = 700;
    private static final int STEP = 2;


    public Terrain(FileReader fileReader) {
        super(fileReader);
        this.sandPitX = fileReader.getSandPitX();
        this.sandPitY = fileReader.getSandPitY();
        this.grassMesh   = new TriangleMesh();
        this.waterMesh   = new TriangleMesh();
        this.sandPitMesh = new TriangleMesh();
        addPoints();
        addFaces();
    }


   private void addPoints() {
        for (int i = 0; i < TERRAIN_HEIGHT; i+= STEP) {
            for (int j = 0; j < TERRAIN_WIDTH; j+= STEP) {
                float height = (float) getHeight(new Vector2D(i, j));
                this.sandPitMesh.getPoints().addAll(i - Display.translateX, j - Display.translateY, height);
                this.waterMesh.getPoints().addAll(i - Display.translateX, j - Display.translateY, height);
                this.grassMesh.getPoints().addAll(i - Display.translateX, j - Display.translateY, height);
            }
        }
   }


    private void addFaces() {
        this.sandPitMesh.getTexCoords().addAll(0, 0, 0, 1, 1, 1, 1, 0);
        this.waterMesh.getTexCoords().addAll(0, 0, 0, 1, 1, 1, 1, 0);
        this.grassMesh.getTexCoords().addAll(0, 0, 0, 1, 1, 1, 1, 0);

        for (int i = 0; i < TERRAIN_HEIGHT / STEP - 2; i++) {
            for (int j = 0; j < TERRAIN_WIDTH / STEP; j++) {

                int topLeft = (TERRAIN_WIDTH / STEP) * i + j;
                int topRight = topLeft + 1;
                int bottomLeft = topLeft + (TERRAIN_WIDTH / STEP);
                int bottomRight = bottomLeft + 1;


                // add texture and faces to water mesh
                if (TerrainGenerator.getHeight(new Vector2D(i * STEP, j * STEP)) > 0) {
                    this.waterMesh.getFaces().addAll(topLeft, 1, topRight, 1, bottomLeft, 1);

                }
                // add texture and faces to sandPit mesh
                if (isSandPit(i * STEP, j * STEP)) {
                    this.sandPitMesh.getFaces().addAll(topLeft, 1, topRight, 1, bottomLeft, 1);
                    this.sandPitMesh.getFaces().addAll(bottomLeft, 1, topRight, 1, bottomRight, 1);
                }
                else {
                    this.grassMesh.getFaces().addAll(topLeft, 1, topRight, 1, bottomLeft, 1);
                }
            }
        }
    }



    public TriangleMesh getGrassMesh() {
        return this.grassMesh;
   }

    public TriangleMesh getWaterMesh() {
        return this.waterMesh;
    }

    public TriangleMesh getSandPitMesh() {
        return this.sandPitMesh;
    }


    private boolean isSandPit(int i, int j) {
        if (i >= this.sandPitX.getX() && i <= this.sandPitX.getY()) {
            return j >= this.sandPitY.getX() && j <= this.sandPitY.getY();
        }
        return false;
    }

}
