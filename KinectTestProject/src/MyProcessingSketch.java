import processing.core.*;

import SimpleOpenNI.*;

public class MyProcessingSketch extends PApplet {

	SimpleOpenNI  context;

	public void setup()
	{
	  context = new SimpleOpenNI(this);
	   
	  // enable depthMap generation 
	  context.enableDepth();
	  
	  // enable camera image generation
	  context.enableRGB();
	 
	  background(200,0,0);
	  size(context.depthWidth() + context.rgbWidth() + 10, context.rgbHeight()); 
	}

	public void draw()
	{
	  // update the cam
	  context.update();
	  
	  // draw depthImageMap
	  image(context.depthImage(),0,0);
	  
	  // draw camera
	  image(context.rgbImage(),context.depthWidth() + 10,0);
	}
	
	 public static void main(String args[]) {
		    PApplet.main(new String[] { "--present", "MyProcessingSketch" });
		  }
}
