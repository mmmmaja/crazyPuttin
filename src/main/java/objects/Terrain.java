package objects;

import graphics.Display;
import javafx.scene.shape.TriangleMesh;
import physics.Vector2D;

public class Terrain {

    private final TriangleMesh grassMesh;
    private final TriangleMesh waterMesh;
    private final TriangleMesh sandPitMesh;
    private final SandPit sandPit;

    private static final int TERRAIN_WIDTH = 900;
    private static final int TERRAIN_HEIGHT = 900;
    private static final int STEP = 2;


    public Terrain(SandPit sandPit) {
        this.grassMesh   = new TriangleMesh();
        this.waterMesh   = new TriangleMesh();
        this.sandPitMesh = new TriangleMesh();
        this.sandPit = sandPit;
        addPoints();
        addFaces();
    }


   private void addPoints() {
        for (int i = 0; i < TERRAIN_HEIGHT; i+= STEP) {
            for (int j = 0; j < TERRAIN_WIDTH; j+= STEP) {
                float height = (float) TerrainGenerator.getHeight(new Vector2D(i, j));
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

        int column = (int) (TERRAIN_WIDTH / STEP);
        int row = (int) (TERRAIN_HEIGHT / STEP);
        for (int i = 0; i < row - 2; i++) {
            for (int j = 0; j < column; j++) {

                int topLeft = column * i + j;
                int topRight = topLeft + STEP;
                int bottomLeft = topLeft + column;
                int bottomRight = bottomLeft + STEP;

                // add texture and faces to grass mesh
                if (!isSandPit(i, j) && TerrainGenerator.getHeight(new Vector2D(i, j)) >= 0) {
                    this.grassMesh.getFaces().addAll(topLeft, 0, topRight, 1, bottomLeft, 2);
                    this.grassMesh.getFaces().addAll(bottomLeft, 0, bottomRight, 1, topRight, 2);
                }

                // add texture and faces to water mesh
                if (TerrainGenerator.getHeight(new Vector2D(i, j)) < 0) {
                    this.waterMesh.getFaces().addAll(topLeft, 0, topRight, 1, bottomLeft, 2);
                    this.waterMesh.getFaces().addAll(bottomLeft, 0, bottomRight, 1, topRight, 2);

                }
                // add texture and faces to sandPit mesh
                if (isSandPit(i, j)) {
                    this.sandPitMesh.getFaces().addAll(topLeft, 0, topRight, 1, bottomLeft, 2);
                    this.sandPitMesh.getFaces().addAll(bottomLeft, 1, bottomRight, 1, topRight, 1);

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
        if (i >= this.sandPit.getSandPitX().getX() && i <= this.sandPit.getSandPitX().getY()) {
            return j >= this.sandPit.getSandPitY().getX() && j <= this.sandPit.getSandPitY().getY();
        }
        return false;
    }


}
