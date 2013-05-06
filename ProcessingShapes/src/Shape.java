import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tobi
 * Date: 29.04.13
 * Time: 08:28
 * To change this template use File | Settings | File Templates.
 */
public class Shape extends Point{
    public List<Point> points = new LinkedList<>();

    public Shape(int x, int y) {
        super(x, y);
    }

    public void add(int x, int y) {
        points.add(new Point(x, y));
    }

    public Polygon getPolygon(){
        int[] xPoints = new int[points.size()];
        int[] yPoints = new int[points.size()];
        for (int i = 0; i < xPoints.length; i++){
            if (i==0){
                xPoints[i]=points.get(i).x;
                yPoints[i]=points.get(i).y;
            } else {
                xPoints[i]=points.get(i).x + xPoints[0];
                yPoints[i]=points.get(i).y + yPoints[0];
            }
        }
        return new Polygon(xPoints,yPoints,points.size());
    }
}
