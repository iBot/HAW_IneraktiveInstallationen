/**
 * Created with IntelliJ IDEA.
 * User: tobi
 * Date: 13.05.13
 * Time: 08:58
 * To change this template use File | Settings | File Templates.
 */
public class BoundingBox {

    Point leftTop, rightBottom, mid;


    public BoundingBox(Point leftTop, Point rightBottom) {
        this.leftTop = leftTop;
        this.rightBottom = rightBottom;
        this.mid = new Point(this.rightBottom.x- this.leftTop.x, this.rightBottom.y- this.leftTop.y);
    }

    public BoundingBox(Shape shape){
        int x1 = shape.x, y1 = shape.y, x2 = shape.x, y2 = shape.y, xp1 = shape.x, yp1 = shape.y;

        for(Point p : shape.getPoints()){
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

        leftTop=new Point(x1, y1);
        rightBottom=new Point(x2, y2);
        this.mid = new Point(rightBottom.x- leftTop.x, rightBottom.y- leftTop.y);
    }

    public Point getLeftTop() {
        return leftTop;
    }

    public Point getRightBottom() {
        return rightBottom;
    }

    public Point getMidPoint(){
        return mid;
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
