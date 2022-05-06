package splines;

import objects.Terrain;
import objects.TerrainGenerator;
import physics.Vector;
import physics.Vector2D;

import java.util.ArrayList;

/**
 * linear Bézier curves from P0 to P1 and from P1 to P2 respectively.
 * Rearranging the preceding equation yields:
 *
 * B(t) = (1-t)^2 * P0 + 2(1-t)t * P1 + t^2 * P2
 *
 * check for n = 5
 *
 * use float[] points from terrain and modify it dynamically
 */
public class SplineInterpolation {

    private final double STEP = 0.1;

    /**
     * The curve starts at P0 going toward P1 and arrives at P3 coming from the direction of P2.
     * Usually, it will not pass through P1 or P2;
     * these points are only there to provide directional information
     *
     * @param t [0, 1] step from each point to another
     * uses 4 points to interpolate terrain
     */
    private double cubicBezierCurve(Vector2D p0, Vector2D p1, Vector2D p2, Vector2D p3, double t) {
        return
                Math.pow(1 - t, 3) * TerrainGenerator.getHeight(p0) +
                        3 * Math.pow(1 - t, 2) * t * TerrainGenerator.getHeight(p1) +
                        3 * (1 - t) * Math.pow(t, 2) * TerrainGenerator.getHeight(p2) +
                        Math.pow(t, 3) * TerrainGenerator.getHeight(p3);
    }



    /**
     * TODO check is indeed returns height
     * @param points set of control points
     * @param start starting index in the p array
     * @param end ending index in the p array
     * @param t [0, 1] current time
     * @return height at current point start + t
     */
    private double generalBezierCurve(Vector2D[] points, int start, int end, double t) {
        if (start + 1 == end) {
            return TerrainGenerator.getHeight(points[0]);
        }
        else {
            double p1 = generalBezierCurve(points, start , end - 1, t);
            double p2 = generalBezierCurve(points, start + 1, end, t);
            return (1 - t) * p1 + t * p2;
        }
    }


    private void interpolateTerrain(Spline spline) {

        // TODO play with these values
        double step = 0.01; // step between points form the edge of the circle
        int n = 5; // number of points at each line from edge to the centre of the circle

        double radius = spline.getRADIUS();
        Vector2D centre = spline.getPosition();

        ArrayList<Vector2D[]> lines = new ArrayList<>();
        // loop over the edge of the circle and find the corresponding coordinates
        for (double y = -radius; y < radius; y+= step) {
            double x = Math.sqrt(radius * radius - y * y);

            Vector2D leftEdgePoint  = new Vector2D(centre.getX() - x, centre.getY() + y);
            Vector2D rightEdgePoint = new Vector2D(centre.getX() + x, centre.getY() + y);

            // find n middle points between the edgePoint and centrePoint
            Vector2D[] leftEdgeLine = findLine(leftEdgePoint, centre, n);
            Vector2D[] rightEdgeLine = findLine(rightEdgePoint, centre, n);

            interpolateLine(leftEdgeLine);
            interpolateLine(rightEdgeLine);
        }

        // recalculate the points of the triangularMesh after altering the height
        Terrain.computePoints();
    }


    /**
     * this method has to affect TerrainGenerator.getHeight()
     */
    public void interpolateLine(Vector2D[] points) {

        Vector2D path = new Vector2D(
                points[points.length - 1].getX() - points[0].getX(),
                points[points.length - 1].getY() - points[0].getY()
        );
        for (double t = 0; t <= 1; t+= STEP) {
            Vector2D step = path.multiply(t);
            Vector2D newPoint = points[0].add(step);
            double newHeight = generalBezierCurve(points, 0, points.length, t);
        }
    }


    /**
     * @param edge point at the edge of the circle
     * @param target centre of the circle
     * @param n number of points between the edge and the target
     * @return the array of n points between the edge and the target
     */
    private Vector2D[] findLine(Vector2D edge, Vector2D target, int n) {

        double distance = Math.sqrt(Math.pow(edge.getX(), 2) + Math.pow(target.getX(), 2));
        double step = distance / n;

        Vector2D[] points = new Vector2D[n];
        int counter = 0;

        // equation of the line from edge to target: y = a * x + b
        double a = (target.getY() - edge.getY()) / target.getX() - edge.getX();
        double b = target.getY() / (a * target.getY());

        if (edge.getX() < target.getX()) {
            for (double x = edge.getX(); x <= target.getX(); x += step) {
                points[counter] = new Vector2D(x, a * x + b);
                counter++;
            }
        }
        else {
            for (double x = target.getX(); x <= edge.getX(); x += step) {
                points[counter] = new Vector2D(x, a * x + b);
                counter++;
            }
        }
        return points;
    }




}
