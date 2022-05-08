package objects;

import javafx.scene.shape.TriangleMesh;
import physics.Vector2D;


/**
 * class responsible for creating the Triangular meshes that will be representation of the terrain in the display
 */
public class Terrain extends TerrainGenerator {

    private final TriangleMesh grassMesh;
    private final TriangleMesh waterMesh;
    private final TriangleMesh sandPitMesh;
    private final Vector2D sandPitX;
    private final Vector2D sandPitY;

    public static final int TERRAIN_WIDTH = 50;
    public static final int TERRAIN_HEIGHT = 50;


    // the size of each polygon in the mesh
    public static final double STEP = 1;

    // to be used to dynamically alter the mesh when dragging the terrain
    private float[] points;


    public Terrain(FileReader fileReader) {
        this.sandPitX = fileReader.getSandPitX();
        this.sandPitY = fileReader.getSandPitY();
        this.grassMesh   = new TriangleMesh();
        this.waterMesh   = new TriangleMesh();
        this.sandPitMesh = new TriangleMesh();

        addPoints();
        addFaces();
    }


    /**
     * Adds all the points from which the polygons of the three meshes will be created
     */
    private void addPoints() {
        this.points = new float[(int) ((1 / STEP) * 2 * TERRAIN_HEIGHT * (1 / STEP) * 2 * TERRAIN_WIDTH) * 3];

        int counter = -1;
        for (double i = -TERRAIN_HEIGHT; i < TERRAIN_HEIGHT; i+= STEP) {
            for (double j = -TERRAIN_WIDTH; j < TERRAIN_WIDTH; j+= STEP) {
                points[counter+=1] = (float) i;
                points[counter+=1] = (float) j;
                points[counter+=1] = - (float) getHeight(new Vector2D(i, j));
            }
        }
    }


    /**
     * creates the faces (triangles) of the meshes and maps the texture into the exact points
     */
    private void addFaces() {

        this.sandPitMesh.getPoints().addAll(points);
        this.waterMesh.getPoints().addAll(points);
        this.grassMesh.getPoints().addAll(points);

        // add the indexes of the indicated textures
        this.sandPitMesh.getTexCoords().addAll(0, 0, 0, 1, 1, 0, 1, 1);
        this.waterMesh.getTexCoords().addAll(0, 0, 0, 1, 1, 0, 1, 1);
        this.grassMesh.getTexCoords().addAll(0, 0, 0, 1, 1, 0, 1, 1);

        for (int i = 0; i < TERRAIN_HEIGHT * 2 / STEP - 2 ; i++) {
            for (int j = 0; j < TERRAIN_WIDTH * 2 / STEP - 1 ; j++) {

                // indexes of the points in the Array of points
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
                else if (isSandPit((i * STEP - (TERRAIN_HEIGHT)) , (j * STEP - (TERRAIN_HEIGHT)) ) ) {
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


    /**
     * @param i x index of the checked point
     * @param j y index of the checked point
     * @return true if point at the given index is a sandPit
     */
    public boolean isSandPit(double i, double j) {
        if (i >= this.sandPitX.getX() && i <= this.sandPitX.getY()) {
            return j >= this.sandPitY.getX() && j <= this.sandPitY.getY();
        }
        return false;
    }

}
