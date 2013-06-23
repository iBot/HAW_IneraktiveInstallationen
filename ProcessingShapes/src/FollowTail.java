import java.awt.Color;

import processing.core.PApplet;
import processing.core.PVector;


public class FollowTail extends PApplet{

	Shape shape;
	PApplet applet;
	BoundingBox box;
	PVector position;
	public int num;
	public float mx[];
	public float my[];
	float x,y;
	
	public FollowTail(BoundingBox bb, Shape shape, PApplet applet){
		box = bb;
		this.shape = shape;
		this.applet = applet;
		num = 30;
		mx = new float[num];
		my = new float[num];
		System.out.println("FOLLOWTAIL: Width: "+applet.width+" Height: "+applet.height);
		 x = (int)random(bb.getLeftTop().x, bb.getWidth() + bb.getLeftTop().x);
		 y = (int)random(bb.getLeftTop().y, bb.getHeight() + bb.getLeftTop().y);
	}
	
	public void update(PVector position){
		applet.ellipse(100, 100, 20, 20);
		System.out.println("update FollowTail");
		int which = applet.frameCount % num;
		System.out.println("which: "+which);
		x += (position.x-x)*0.05f;
	    y += (position.y-y)*0.05f;
	    System.out.println("X: "+x+" Y: "+y);
//		mx[which] = x * (box.getWidth()/applet.width*1.0f)+box.getLeftTop().x;
//      my[which] = y * (box.getHeight()/applet.height*1.0f)+box.getLeftTop().y;
	    mx[which] = x;
	    my[which] = y;
        System.out.println("MX: "+mx.toString());
        System.out.println("MY: "+my.toString());
        
        for (int i = 0; i < num; i++) {
            // which+1 is the smallest (the oldest in the array)
            int index = (which+1 + i) % num;
            System.out.println("INDEX: "+index+" X: "+mx[index]+" Y: "+my[index]);
//            if (shape.getPolygon().contains(mx[index], my[index])){
                applet.ellipse(mx[index], my[index], i, i);
//            } 
        }
	}
}
