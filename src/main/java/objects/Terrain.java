package objects;

import Main.Universe;
import graphics.Display;
import javafx.scene.shape.TriangleMesh;
import physics.Vector2D;

public class Terrain extends TerrainGenerator {
    private final TriangleMesh grassMesh;
    private final TriangleMesh waterMesh;
    private final TriangleMesh sandPitMesh;
    private final Vector2D sandPitX;
    private final Vector2D sandPitY;

    private static final int TERRAIN_WIDTH = 50;
    private static final int TERRAIN_HEIGHT = 50;
    private static final double STEP = 0.25;


    public Terrain(FileReader fileReader) {
        this.sandPitX = fileReader.getSandPitX();
        this.sandPitY = fileReader.getSandPitY();
        this.grassMesh   = new TriangleMesh();
        this.waterMesh   = new TriangleMesh();
        this.sandPitMesh = new TriangleMesh();
        addPoints();
        addFaces();
    }


    private void addPoints() {
        for (double i = -TERRAIN_HEIGHT; i < TERRAIN_HEIGHT; i+= STEP) {
            for (double j = -TERRAIN_WIDTH; j < TERRAIN_WIDTH; j+= STEP) {
                float height = (float) -getHeight(new Vector2D(i, j));
                this.sandPitMesh.getPoints().addAll((float) i, (float) j, height);
                this.waterMesh.getPoints().addAll((float) i, (float) j, height);
                this.grassMesh.getPoints().addAll((float) i, (float) j, height);
            }
        }
    }


    private void addFaces() {
        this.sandPitMesh.getTexCoords().addAll(0, 0, 0, 1, 1, 0, 1, 1);
        this.waterMesh.getTexCoords().addAll(0, 0, 0, 1, 1, 0, 1, 1);
        this.grassMesh.getTexCoords().addAll(0, 0, 0, 1, 1, 0, 1, 1);

        for (int i = 0; i < TERRAIN_HEIGHT * 2 / STEP - 2 ; i++) {
            for (int j = 0; j < TERRAIN_WIDTH * 2 / STEP - 1 ; j++) {

                int topLeft = (int) ((TERRAIN_WIDTH * 2  / STEP) * i + j);
                int topRight = topLeft + 1;
                int bottomLeft = (int) (topLeft + (TERRAIN_WIDTH * 2 / STEP));
                int bottomRight = bottomLeft + 1;

                // add texture and faces to water mesh
                if (TerrainGenerator.getHeight(new Vector2D( i * STEP - 50, j * STEP -50 )) < 0) {
                    this.waterMesh.getFaces().addAll(topLeft, 0, topRight, 1, bottomLeft, 2);
                    this.waterMesh.getFaces().addAll(bottomLeft, 0, topRight, 1, bottomRight, 2);

                }
                // add texture and faces to sandPit mesh
                else if (isSandPit(i * STEP - 50  , j * STEP - 50) ) {
                    this.sandPitMesh.getFaces().addAll(topLeft, 0, topRight, 1, bottomLeft, 2);
                    this.sandPitMesh.getFaces().addAll(bottomLeft, 0, topRight, 1, bottomRight, 2);
                }
                // add texture and faces to grass mesh
                else {
                    this.grassMesh.getFaces().addAll(topLeft, 0, topRight, 1, bottomLeft, 2);
                    this.grassMesh.getFaces().addAll(bottomLeft, 0, topRight, 1, bottomRight, 2);
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


    public boolean isSandPit(double i, double j) {
        if (i >= this.sandPitX.getX() && i <= this.sandPitX.getY()) {
            return j >= this.sandPitY.getX() && j <= this.sandPitY.getY();
        }
        return false;
    }

}