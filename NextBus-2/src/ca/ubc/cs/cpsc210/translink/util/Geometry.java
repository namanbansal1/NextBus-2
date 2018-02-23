package ca.ubc.cs.cpsc210.translink.util;




/**
 * Compute relationships between points, lines, and rectangles represented by LatLon objects
 */
public class Geometry {
    /**
     * Return true if the point is inside of, or on the boundary of, the rectangle formed by northWest and southeast
     *
     * @param northWest the coordinate of the north west corner of the rectangle
     * @param southEast the coordinate of the south east corner of the rectangle
     * @param point     the point in question
     * @return true if the point is on the boundary or inside the rectangle
     */
    public static boolean rectangleContainsPoint(LatLon northWest, LatLon southEast, LatLon point) {

        return between(southEast.getLatitude(), northWest.getLatitude(), point.getLatitude()) &&
                between(northWest.getLongitude(), southEast.getLongitude(), point.getLongitude());
    }


    /**
     * Return true if the rectangle intersects the line
     *
     * @param northWest the coordinate of the north west corner of the rectangle
     * @param southEast the coordinate of the south east corner of the rectangle
     * @param src       one end of the line in question
     * @param dst       the other end of the line in question
     * @return true if any point on the line is on the boundary or inside the rectangle
     */
    public static boolean rectangleIntersectsLine(LatLon northWest, LatLon southEast, LatLon src,
                                                  LatLon dst) {

        double slopeline;
        double rectline;
        double slopeline1;
        double rectline1;
        double slopeline2;
        double rectline2;
        double slopeline3;
        double rectline3;


        slopeline = (dst.getLongitude()-src.getLongitude())/(dst.getLatitude()-src.getLatitude());
        rectline = slopeline*(northWest.getLatitude()-src.getLatitude())+ src.getLongitude();
        slopeline1 = (dst.getLongitude()-src.getLongitude())/(dst.getLatitude()-src.getLatitude());
        rectline1 = slopeline1*(southEast.getLatitude()-src.getLatitude())+ src.getLongitude();
        slopeline2 = (dst.getLongitude()-src.getLongitude())/(dst.getLatitude()-src.getLatitude());
        rectline2 = slopeline2*(northWest.getLongitude()-src.getLatitude())+ src.getLongitude();
        slopeline3 = (dst.getLongitude()-src.getLongitude())/(dst.getLatitude()-src.getLatitude());
        rectline3 = slopeline3*(southEast.getLongitude()-src.getLatitude())+ src.getLongitude();

        return  between(southEast.getLatitude(),northWest.getLatitude(),rectline)||
                between(southEast.getLatitude(),northWest.getLatitude(),rectline1)||
                between(southEast.getLatitude(),northWest.getLatitude(),rectline2)||
                between(northWest.getLatitude(),southEast.getLatitude(),rectline3)||
                between(southEast.getLatitude(), northWest.getLatitude(), src.getLatitude()) &&
                        between(northWest.getLongitude(), southEast.getLongitude(), src.getLongitude())||
                between(southEast.getLatitude(), northWest.getLatitude(), dst.getLatitude()) &&
                        between(northWest.getLongitude(), southEast.getLongitude(), dst.getLongitude());



    }


    /**
     * A utility method that you might find helpful in implementing the two previous methods
     * Return true if x is >= lwb and <= upb
     *
     * @param lwb the lower boundary
     * @param upb the upper boundary
     * @param x   the value in question
     * @return true if x is >= lwb and <= upb
     */
    private static boolean between(double lwb, double upb, double x) {
        return lwb <= x && x <= upb;
    }
}
