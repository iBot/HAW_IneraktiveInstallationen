/**
 * Created with IntelliJ IDEA.
 * User: tobi
 * Date: 13.05.13
 * Time: 08:58
 * To change this template use File | Settings | File Templates.
 */
public class BoundingBox {

    Point p1, p2, mid;


    public BoundingBox(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.mid = new Point(p2.x-p1.x, p2.y-p1.y);
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    public Point getMidPoint(){
        return mid;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BoundingBox{");
        sb.append("p1=").append(p1);
        sb.append(", p2=").append(p2);
        sb.append(", mid=").append(mid);
        sb.append('}');
        return sb.toString();
    }
}
