/**
 * Point-class
 */
public class Point {
    public final int x;
    public final int y;

    /**
     * Point Constructer
     *
     * @param x x-coordinate of the point
     * @param y y-coordinate for the point
     */
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