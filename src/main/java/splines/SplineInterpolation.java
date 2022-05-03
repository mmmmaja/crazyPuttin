package splines;

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
     * these points are only there to provide directional information.
     *
     * @param t [0, 1] what is a h???
     *
     *
     *  1. change the terrain
     */
    private double cubicBezierCurve(double p0, double p1, double p2, double p3, double t) {
        return
                Math.pow(1 - t, 3) * p0 +
                        3 * Math.pow(1 - t, 2) * t * p1 +
                        3 * (1 - t) * Math.pow(t, 2) * p2 +
                        Math.pow(t, 3) * p3;
    }



    /**
     * @param p set of control points
     * @param start starting index in the p array
     * @param end ending index in the p array
     * @param t [0, 1] current time
     */
    private double generalBezierCurve(double[] p, int start, int end, double t) {
        if (start + 1 == end) {
            return p[0];
        }
        else {
            double p1 = generalBezierCurve(p, start , end - 1, t);
            double p2 = generalBezierCurve(p, start + 1, end, t);
            return (1 - t) * p1 + t * p2;
        }
    }


    /**
     * use STEP
     */
//    public void interpolateTerrain(Spline spline) {
//        Terrain.points
//        double radius = spline.getRADIUS();
//        Vector2D mid = new Vector2D(spline.getPosition().getX() - radius)
//
//        for (double i = -Terrain.TERRAIN_HEIGHT; i < Terrain.TERRAIN_HEIGHT; i+= STEP) {
//            for (double j = -Terrain.TERRAIN_WIDTH; j < Terrain.TERRAIN_WIDTH; j+= STEP) {
//
//            }
//        }
//    }



}
