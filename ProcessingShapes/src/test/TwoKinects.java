package test;

/* --------------------------------------------------------------------------
 * SimpleOpenNI Multi Camera Test
 * --------------------------------------------------------------------------
 * Processing Wrapper for the OpenNI/Kinect library
 * http://code.google.com/p/simple-openni
 * --------------------------------------------------------------------------
 * prog:  Max Rheiner / Interaction Design / zhdk / http://iad.zhdk.ch/
 * date:  11/08/2011 (m/d/y)
 * ----------------------------------------------------------------------------
 * Be aware that you shouln't put the cameras at the same usb bus(usb performance!).
 * On linux/OSX  you can use 'lsusb' to see on which bus the camera is
 * ----------------------------------------------------------------------------
 */

import processing.core.PApplet;
import SimpleOpenNI.*;


public class TwoKinects extends PApplet{
	


SimpleOpenNI  cam1;
SimpleOpenNI  cam2;

public void setup()
{
  size(640 * 2 + 10,480 * 2 + 10); 

  // start OpenNI, loads the library
  SimpleOpenNI.start();
  
  // print all the cams 
  StrVector strList = new StrVector();
  SimpleOpenNI.deviceNames(strList);
  for(int i=0;i<strList.size();i++)
    println(i + ":" + strList.get(i));

  // init the cameras
  cam1 = new SimpleOpenNI(this);
  cam2 = new SimpleOpenNI(this);

  // set the camera generators
  cam1.enableDepth();
  cam1.enableIR();
 
  cam2.enableDepth();
  cam2.enableIR();
  
 
 
  background(10,200,20);
}

public void draw()
{
  // update the cam
    cam1.update();
    cam2.update();
//  SimpleOpenNI.updateAll();
  
  // draw depthImageMap
  image(cam1.depthImage(),0,0);
  image(cam1.irImage(),0,480 + 10);
//
//    System.out.println("is null? "+cam2);
  image(cam2.depthImage(),640,0);
  image(cam2.irImage(),640,480);
  
}
}
