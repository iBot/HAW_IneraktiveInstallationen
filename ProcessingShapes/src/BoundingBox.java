import com.jogamp.opengl.util.packrect.Rect;
import processing.*;

/**
 * Created with IntelliJ IDEA.
 * User: tobi
 * Date: 13.05.13
 * Time: 08:58
 * To change this template use File | Settings | File Templates.
 */
public class BoundingBox {

    Point leftTop, rightBottom, mid;
    Shape shape;

    public BoundingBox(Point leftTop, Point rightBottom) {
        this.leftTop = leftTop;
        this.rightBottom = rightBottom;
        this.mid = new Point(this.rightBottom.x- this.leftTop.x, this.rightBottom.y- this.leftTop.y);
    }

    public BoundingBox(Shape shape){

    	this.shape = shape;
        int rightBottomX = 0, leftTopX = 10000, rightBottomY = 0, leftTopY = 100000;

        // draw a vertex between all points of a shape
        for (Point point : shape.getPoints()) {
            //noFill();


            int x = point.x+shape.x;
            int y = point.y+shape.y;

            if(x > rightBottomX) rightBottomX = x;
            if(x < leftTopX) leftTopX = x;
            if(y > rightBottomY) rightBottomY = y;
            if(y < leftTopY) leftTopY = y;
        }

        leftTop=new Point(leftTopX, leftTopY);
        rightBottom=new Point(rightBottomX, rightBottomY);

//        leftTop=new Point(leftTopX, leftTopY);
//        rightBottom=new Point(rightBottomX, rightBottomY);
        this.mid = new Point(((rightBottom.x- leftTop.x)/2) +leftTop.x, ((rightBottom.y- leftTop.y)/2)+leftTop.y);
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

    public int getWidth()
    {
    	return Math.abs(rightBottom.x-leftTop.x);
    }
    
    public int getHeight()
    {
    	return Math.abs(rightBottom.y-leftTop.y);
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
