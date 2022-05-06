package objects;

import javafx.collections.ObservableFloatArray;
import javafx.scene.shape.TriangleMesh;
import physics.Vector2D;
import physics.Vector3D;


/**
 * class responsible for creating the Triangular meshes that will be representation of the terrain in the display
 */
public class Terrain extends TerrainGenerator {

    private static TriangleMesh grassMesh;
    private final TriangleMesh waterMesh;
    private final TriangleMesh sandPitMesh;
    private final Vector2D sandPitX;
    private final Vector2D sandPitY;

    public static final int TERRAIN_WIDTH = 50;
    public static final int TERRAIN_HEIGHT = 50;

    public static Vector3D[][] points;
    public static double STEP = 0.05;

    private static final double meshStep = STEP * 100;

    public Terrain(FileReader fileReader) {
        this.sandPitX = fileReader.getSandPitX();
        this.sandPitY = fileReader.getSandPitY();

        grassMesh   = new TriangleMesh();
        this.waterMesh   = new TriangleMesh();
        this.sandPitMesh = new TriangleMesh();

        points = generatePoints();
        createMesh();
    }

    public static void alterMesh() {
        ObservableFloatArray pointArray = grassMesh.getPoints();
        int counter = -1;
        for (int i = 0; i < points.length; i += meshStep) {
            for (int j = 0; j < points[0].length; j += meshStep) {
                pointArray.set(counter += 1, (float) points[i][j].getX());
                pointArray.set(counter += 1, (float) points[i][j].getY());
                pointArray.set(counter += 1, (float) points[i][j].getZ());
            }
        }
    }


    /**
     * @return array of the points in the terrain array that will be dynamically changed when the splines are added
     */
    private Vector3D[][] generatePoints() {
        Vector3D[][] points = new Vector3D[(int) (TERRAIN_HEIGHT * 2 / STEP)][(int) (TERRAIN_WIDTH * 2 / STEP)];

        int x = 0;
        for (double i = -TERRAIN_HEIGHT; i <= TERRAIN_HEIGHT - STEP; i+= STEP) {
            int y = 0;
            for (double j = -TERRAIN_WIDTH; j <= TERRAIN_WIDTH - STEP; j+= STEP) {
                points[x][y] = new Vector3D(i, j, TerrainGenerator.getHeightFromFile(i, j));
                y++;
            }
            x++;
        }
        return points;
    }


    /**
     * creates the faces (triangles) of the meshes and maps the texture into the exact points
     */
    private void createMesh() {
        float[] meshPoints = new float[(int) (points.length * points[0].length * 3 / (meshStep * meshStep))];
        int counter = -1;
        for (int i = 0; i < points.length; i += meshStep) {
            for (int j = 0; j < points[0].length; j += meshStep) {
                meshPoints[counter += 1] = (float) points[i][j].getX();
                meshPoints[counter += 1] = (float) points[i][j].getY();
                meshPoints[counter += 1] = -(float) points[i][j].getZ();
            }
        }

        this.sandPitMesh.getPoints().addAll(meshPoints);
        this.waterMesh.getPoints().addAll(meshPoints);
        grassMesh.getPoints().addAll(meshPoints);

        // add the indexes of the indicated textures
        this.sandPitMesh.getTexCoords().addAll(0, 0, 0, 1, 1, 0, 1, 1);
        this.waterMesh.getTexCoords().addAll(0, 0, 0, 1, 1, 0, 1, 1);
        grassMesh.getTexCoords().addAll(0, 0, 0, 1, 1, 0, 1, 1);

        for (int i = 0; i < TERRAIN_HEIGHT * 2 / (STEP * meshStep) - 2; i++) {
            for (int j = 0; j < TERRAIN_WIDTH * 2 / (STEP * meshStep) - 1; j++) {

                // indexes of the points in the Array of points
                int topLeft = (int) (TERRAIN_WIDTH * 2 / (STEP * meshStep) * i + j);
                int topRight = topLeft + 1;
                int bottomLeft = (int) (topLeft + (TERRAIN_WIDTH * 2 / (STEP * meshStep)));
                int bottomRight = bottomLeft + 1;

                // add texture and faces to water mesh
                if (TerrainGenerator.getHeightFromFile(i * (STEP * meshStep) - TERRAIN_WIDTH, j * (STEP * meshStep) - TERRAIN_HEIGHT) < 0) {
                    this.waterMesh.getFaces().addAll(topLeft, 0, topRight, 1, bottomLeft, 2);
                    this.waterMesh.getFaces().addAll(bottomLeft, 0, topRight, 1, bottomRight, 2);
                }
                // add texture and faces to sandPit mesh
                else if (isSandPit(i * (STEP * meshStep) - TERRAIN_WIDTH, j * (STEP * meshStep) - TERRAIN_HEIGHT)) {
                    this.sandPitMesh.getFaces().addAll(topLeft, 0, topRight, 1, bottomLeft, 2);
                    this.sandPitMesh.getFaces().addAll(bottomLeft, 0, topRight, 1, bottomRight, 2);
                }
                // add texture and faces to grass mesh
                else {
                    grassMesh.getFaces().addAll(topLeft, 0, topRight, 1, bottomLeft, 2);
                    grassMesh.getFaces().addAll(bottomLeft, 0, topRight, 1, bottomRight, 2);
                }
            }
        }
    }


    public TriangleMesh getGrassMesh() {
        return grassMesh;
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


    /**
     * @param position of the point to be checked
     * @return height of the terrain at the given position based on the function passed in the InputFile
     * TODO interpolate this shit
     */
    public static double getHeight(Vector2D position) {
        int column = (int) ((position.getY() + Terrain.TERRAIN_WIDTH) / Terrain.STEP);
        int row = (int) ((position.getX() + Terrain.TERRAIN_HEIGHT)/ Terrain.STEP);
        return points[row][column].getZ();
    }

    /**
     * @return height of the terrain at the given position based on the function passed in the InputFile
     */
    public static double getHeight(double x, double y) {
        return getHeight(new Vector2D(x, y));
    }


    public static void setHeight(Vector2D point, double height) {
        int column = (int) ((point.getX() + Terrain.TERRAIN_WIDTH) / Terrain.STEP);
        int row = (int) ((point.getY() + Terrain.TERRAIN_HEIGHT)/ Terrain.STEP);
        points[row][column].setZ(height);
    }


}