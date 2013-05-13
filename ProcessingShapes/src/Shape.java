import processing.core.PImage;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tobi
 * Date: 29.04.13
 * Time: 08:28
 * To change this template use File | Settings | File Templates.
 */
public class Shape extends Point {
    private List<Point> points = new LinkedList<>();
    private PImage image;
    private BoundingBox boundingBox;

    public Shape(int x, int y) {
        this(x, y, null);
    }

    public Shape(int x, int y, PImage image) {
        super(x, y);
        this.image = image;
    }

    public BoundingBox getBoundingBox() {
        if (boundingBox == null) {
            recalculateBoundingBox();
        }
        return boundingBox;
    }

    public void recalculateBoundingBox() {
        int x1 = x, y1 = y, x2 = x, y2 = y, xp1 = x, yp1 = y;

        for(Point p : points){
            System.out.println(p);

            if (xp1 + p.x < x1) {
                x1 = xp1 + p.x;
            }

            if (yp1 + p.y < y1) {
                y1 = yp1 + p.y;
            }

            if (xp1 + p.x > x2) {
                x2 = xp1 + p.x;
            }

            if (yp1 + p.y > y2) {
                y2 = yp1 + p.y;
            }
        }

        boundingBox = new BoundingBox(new Point(x1, y1), new Point(x2, y2));

    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public void add(int x, int y) {
        points.add(new Point(x, y));
    }

    public PImage getImage() {
        return image;
    }

    public void setImage(PImage image) {
        this.image = image;
    }

    public boolean hasImage() {
        return (this.image == null) ? false : true;
    }

    public void removeImage() {
        this.image = null;
    }

    public Polygon getPolygon() {
        int[] xPoints = new int[points.size()];
        int[] yPoints = new int[points.size()];
        for (int i = 0; i < xPoints.length; i++) {
            if (i == 0) {
                xPoints[i] = points.get(i).x;
                yPoints[i] = points.get(i).y;
            } else {
                xPoints[i] = points.get(i).x + xPoints[0];
                yPoints[i] = points.get(i).y + yPoints[0];
            }
        }
        return new Polygon(xPoints, yPoints, points.size());
    }
}
