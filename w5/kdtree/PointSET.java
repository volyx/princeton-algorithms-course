import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> points = new TreeSet<>();

    public PointSET() { // construct an empty set of points

    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point2D : points) {
            StdDraw.text(point2D.x(), point2D.y(), point2D.toString());
            StdDraw.point(point2D.x(), point2D.y());
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        TreeSet<Point2D> pointsInRect = new TreeSet<>();
        for (Point2D p : points) {
            if (rect.xmin() <= p.x() && p.x() <= rect.xmax()
                    && rect.ymin() <= p.y() && p.y() <= rect.ymax()) {
                pointsInRect.add(p);
            }
        }

        return pointsInRect;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (points.isEmpty()) {
            return null;
        }

        double minDistance = Double.POSITIVE_INFINITY;
        Point2D minPoint = null;
        for (Point2D point : points) {
            if (point.distanceSquaredTo(p) < minDistance) {
                minDistance = point.distanceSquaredTo(p);
                minPoint = point;
            }
        }
        return minPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET pointSET = new PointSET();
        pointSET.insert(new Point2D(0.0, 0.0));
    }
}
