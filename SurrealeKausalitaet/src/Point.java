/**
 * Created with IntelliJ IDEA.
 * User: tobi
 * Date: 29.04.13
 * Time: 08:29
 * To change this template use File | Settings | File Templates.
 */
public class Point {
    public final int x;
    public final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Point{");
        sb.append("x=").append(x);
        sb.append(", y=").append(y);
        sb.append('}');
        return sb.toString();
    }
}