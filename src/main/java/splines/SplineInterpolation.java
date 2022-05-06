package splines;

import objects.Terrain;
import objects.TerrainGenerator;
import physics.Vector2D;
import physics.Vector3D;

import static objects.Terrain.*;

/**
 *
 * ! IDEA: have the array of points with very small step and interpolate if u need point between
 * linear Bézier curves from P0 to P1 and from P1 to P2 respectively.
 * Rearranging the preceding equation yields:
 *
 * B(t) = (1-t)^2 * P0 + 2(1-t)t * P1 + t^2 * P2
 *
 * check for n = 5
 *
 * use float[] points from terrain and modify it dynamically
 * ! work on the points array from the Terrain
 */
public class SplineInterpolation {

    /**
     * The curve starts at P0 going toward P1 and arrives at P3 coming from the direction of P2.
     * Usually, it will not pass through P1 or P2;
     * these points are only there to provide directional information
     *
     * @param t [0, 1] step from each point to another
     * uses 4 points to interpolate terrain
     */
    private double cubicBezierCurve(Vector3D p0, Vector3D p1, Vector3D p2, Vector3D p3, double t) {
        return
                Math.pow(1 - t, 3) * p0.getZ() +
                        3 * Math.pow(1 - t, 2) * t * p1.getZ() +
                        3 * (1 - t) * Math.pow(t, 2) * p2.getZ() +
                        Math.pow(t, 3) * p3.getZ();
    }



    /**
     * TODO check is indeed returns height
     * @param points set of control points
     * @param start starting index in the p array
     * @param end ending index in the p array
     * @param t [0, 1] current time
     * @return height at current point start + t
     */
    private double generalBezierCurve(Vector3D[] points, int start, int end, double t) {
        if (start + 1 == end) {
            return points[0].getZ();
        }
        else {
            double p1 = generalBezierCurve(points, start , end - 1, t);
            double p2 = generalBezierCurve(points, start + 1, end, t);
            return (1 - t) * p1 + t * p2;
        }
    }


    public void interpolateTerrain(Spline spline) {

        double radius = spline.getRADIUS();

        Vector3D centre = new Vector3D(
                spline.getPosition().getX(),
                spline.getPosition().getY(),
                spline.getHeight()
        );
        System.out.println(centre);
        System.out.println("oldHeight: "+ getHeight(spline.getPosition().getX(),
                spline.getPosition().getY()));

        // loop over the edge of the circle and find the corresponding coordinates
        for (double y = -radius; y < radius; y+= Terrain.STEP) {
            double x = Math.sqrt(radius * radius - y * y);

            // how to map the point to the array?
            Vector3D leftEdgePoint  = new Vector3D(
                    centre.getX() - x,
                    centre.getY() + y,
                    Terrain.getHeight(centre.getX() - x,centre.getY() + y)
            );
            Vector3D rightEdgePoint  = new Vector3D(
                    centre.getX() + x,
                    centre.getY() + y,
                    Terrain.getHeight(centre.getX() + x,centre.getY() + y)
            );

            interpolateOverTwoPoints(leftEdgePoint, centre);
            interpolateOverTwoPoints(rightEdgePoint, centre);
        }

    }

    private void interpolateOverTwoPoints(Vector3D edge, Vector3D centre) {
        Vector3D[] points = {edge, centre};
        Vector2D path = new Vector2D(
                points[points.length - 1].getX() - points[0].getX(),
                points[points.length - 1].getY() - points[0].getY()
        );
        for (double t = 0; t <= 1; t+= Terrain.STEP) {
            double newHeight = generalBezierCurve(points, 0, points.length, t);
            Vector2D newPoint = new Vector2D(
                    points[0].getX() + path.multiply(t).getX(),
                    points[0].getY() + path.multiply(t).getY()
            );
            setHeight(newPoint, newHeight);
        }
        alterMesh();
    }




    /**
     * this method has to affect TerrainGenerator.getHeight()
     */
    public void interpolateLine(Vector2D[] points) {

//        Vector2D path = new Vector2D(
//                points[points.length - 1].getX() - points[0].getX(),
//                points[points.length - 1].getY() - points[0].getY()
//        );
//        for (double t = 0; t <= 1; t+= Terrain.STEP) {
//            Vector2D step = path.multiply(t);
//            Vector2D newPoint = points[0].add(step);
//            System.out.println("newPoint: "+newPoint);
//            double newHeight = generalBezierCurve(points, 0, points.length, t);
//            System.out.println("newHeight: "+newHeight);
//        }
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
