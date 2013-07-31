

/**
 * Created with IntelliJ IDEA.
 * User: tobi
 * Date: 13.05.13
 * Time: 08:58
 * To change this template use File | Settings | File Templates.
 */
public class BoundingBox {

    private Point leftTop, rightBottom, mid;
    private Shape shape;

    /**
     * BoundingBox Constructor
     *
     * @param leftTop     left top corner of the bounding box
     * @param rightBottom right bottom corner of the bounding box
     */
    public BoundingBox(Point leftTop, Point rightBottom) {
        this.leftTop = leftTop;
        this.rightBottom = rightBottom;
        this.mid = new Point(this.rightBottom.x - this.leftTop.x, this.rightBottom.y - this.leftTop.y);
    }

    /**
     * BoundingBox Constructor
     *
     * @param shape Shape, that the BB bases upon
     */
    public BoundingBox(Shape shape) {

        this.shape = shape;
        int rightBottomX = 0, leftTopX = 10000, rightBottomY = 0, leftTopY = 100000;

        for (Point point : shape.getPoints()) {

            int x = point.x + shape.x;
            int y = point.y + shape.y;

            if (x > rightBottomX) rightBottomX = x;
            if (x < leftTopX) leftTopX = x;
            if (y > rightBottomY) rightBottomY = y;
            if (y < leftTopY) leftTopY = y;
        }

        leftTop = new Point(leftTopX, leftTopY);
        rightBottom = new Point(rightBottomX, rightBottomY);

        this.mid = new Point(((rightBottom.x - leftTop.x) / 2) + leftTop.x, ((rightBottom.y - leftTop.y) / 2) + leftTop.y);
    }

    /**
     * get left top
     *
     * @return left top Point
     */
    public Point getLeftTop() {
        return leftTop;
    }

    /**
     * get right bottom
     *
     * @return right bottom point
     */
    public Point getRightBottom() {
        return rightBottom;
    }

    /**
     * get mid
     *
     * @return mid point
     */
    public Point getMidPoint() {
        return mid;
    }

    /**
     * get width
     *
     * @return width
     */
    public int getWidth() {
        return Math.abs(rightBottom.x - leftTop.x);
    }

    /**
     * get heigth
     *
     * @return heigth
     */
    public int getHeight() {
        return Math.abs(rightBottom.y - leftTop.y);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BoundingBox{");
        sb.append("leftTop=").append(leftTop);
        sb.append(", rightBottom=").append(rightBottom);
        sb.append(", mid=").append(mid);
        sb.append('}');
        return sb.toString();
    }
}
