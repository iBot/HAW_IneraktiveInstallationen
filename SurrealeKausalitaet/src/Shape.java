import processing.core.PApplet;
import processing.core.PImage;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Just a shape.
 */
public class Shape extends Point {


    /**
     * TODO: What are num, mx[], my[] and tail[] for? Do we still need them?
     */
    public int num = 20;
    //	public FollowTail tail[] = new FollowTail[5];
    public Tail tail[] = new Tail[1];
    /**
     * Contains coordinates of all vertexes of the shape
     */
    private List<Point> points = new LinkedList<>();
    /**
     * An Image for the shape
     */
    private PImage image;
    /**
     * BoundingBox, will not be updated automatically when adding new vertex-points
     */
    private BoundingBox boundingBox;
    /**
     * Color of the shape
     */
    private Color color;
    private float mx[] = new float[num];
    private float my[] = new float[num];

    public ColorToChange getColorToChange() {
        return colorToChange;
    }

    public void setColorToChange(ColorToChange colorToChange) {
        this.colorToChange = colorToChange;
    }

    private ColorToChange colorToChange;

    /**
     * Shape Constructor
     *
     * @param x x-coordinate of the shape
     * @param y y-coordinate for the shape
     */
    public Shape(int x, int y) {
        this(x, y, null);
    }

    /**
     * Shape Constructor with PImage
     *
     * @param x     x-coordinate of the shape
     * @param y     y-coordinate for the shape
     * @param image Image of the shape
     */
    public Shape(int x, int y, PImage image) {
        super(x, y);
        this.image = image;
        colorToChange = ColorToChange.GREEN;

    }

    /**
     * Initializes the tail, drawn inside the tape
     *
     * @param applet The applet in whose context the tail should be displayed
     */
    public void initTail(PApplet applet) {
        for (int i = 0; i < tail.length; i++) {
            tail[i] = new Tail(getBoundingBox(), applet, this);
        }
    }

    /**
     * Returns the BoundingBox of the Shape. If the BoundingBox has not been calculated before,
     * it will happen before returning it, but it will not be recalculated, if it already exists.
     * If you've added any new points that should be considered in the BoundingBox, remove or
     * recalculate the BoundingBox before calling this method.
     *
     * @return the BoundingBox of the Shape
     */
    public BoundingBox getBoundingBox() {
        if (boundingBox == null) {
            recalculateBoundingBox();
        }
        return boundingBox;
    }

    /**
     * recalculates the BoundingBox
     */
    public void recalculateBoundingBox() {
        boundingBox = new BoundingBox(this);
    }

    /**
     * Get points with coordinates of all vertexes
     *
     * @return List with sll points with coordinates of all vertexes
     */
    public List<Point> getPoints() {
        return new LinkedList<>(points);
    }

    public void setPoints(List<Point> points) {
        this.points = new LinkedList<>(points);
    }

    /**
     * @param x x-coordinate of new vertex
     * @param y y-coordinate of new vertex
     */
    public void add(int x, int y) {

        //int transX = this.x + x;
        //int transY = this.y + y;
        points.add(new Point(x, y));
    }

    /**
     * get image of the shape
     *
     * @return Image of the shape
     */
    public PImage getImage() {
        return image;
    }

    /**
     * set image for the shape
     *
     * @param image Image for the shape
     */
    public void setImage(PImage image) {
        this.image = image;
    }

    /**
     * Does the Shape has an image
     *
     * @return true, if image is available, else false
     */
    public boolean hasImage() {
        return (this.image == null) ? false : true;
    }

    /**
     * Removes the image of the Shape
     */
    public void removeImage() {
        this.image = null;
    }

    /**
     * Creates and returns a Polygon withe the same coordinates as an Shape. Polygon offers
     * usefull methods like contains(...) to check whether a point is in it or not.
     *
     * @return Polygon with same coordinates
     */
    public Polygon getPolygon() {
        int[] xPoints = new int[points.size()];
        int[] yPoints = new int[points.size()];
        for (int i = 0; i < xPoints.length; i++) {
            xPoints[i] = points.get(i).x + x;
            yPoints[i] = points.get(i).y + y;
        }
        return new Polygon(xPoints, yPoints, points.size());
    }

    /**
     * Get the color of the shape
     *
     * @return color of the shape
     */
    public Color getColor() {
        return color;
    }

    /**
     * Set the color for the shape
     *
     * @param color color for the shape
     */
    public void setColor(Color color) {
        this.color = color;
    }
}
