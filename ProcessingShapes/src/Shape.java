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
    private Color color;

	public int num = 20;
	public float mx[] = new float[num];
	public float my[] = new float[num];

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
        boundingBox=new BoundingBox(this);
    }

    public List<Point> getPoints() {
        return new LinkedList<>(points);
    }

    public void setPoints(List<Point> points) {
        this.points = new LinkedList<>(points);
    }

    public void add(int x, int y) {
    	
    	//int transX = this.x + x;
    	//int transY = this.y + y;
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
//            if (i == 0) {
//                xPoints[i] = points.get(i).x;
//                yPoints[i] = points.get(i).y;
//            } else {
                xPoints[i] = points.get(i).x + x;
                yPoints[i] = points.get(i).y + y;
//            }
        }
        return new Polygon(xPoints, yPoints, points.size());
    }

	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor(){
		return color;
	}
}
