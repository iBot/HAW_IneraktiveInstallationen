import java.awt.Color;

import processing.core.PApplet;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;
import java.awt.*;

public class Test extends PApplet {

	SimpleOpenNI context;

	Shape shape;

	@Override
	public void setup() {
		// size for fullscreen window, renderer OpenGL
		// Don't forget to import native libraries for your OS. If OpenGL
		// doesn't work, render without OpenGL...
		size(displayWidth, displayHeight, OPENGL);
		// size(displayWidth, displayHeight, P3D);
		// size(displayWidth, displayHeight, P2D);
		// size(1000, 400, P3D);

		background(Color.BLACK.getRGB());
		//
		stroke(0, 255, 0);
		strokeWeight(3);
		smooth();

		context = new SimpleOpenNI(this);

		UserManager userManager = new UserManager(context);

		System.out.println(context + "");
		// enable depthMap generation
		context.enableDepth();

		// enable skeleton generation for all joints
		context.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL, userManager);

		context.setMirror(true);

		shape = new Shape(619, 405);

		shape.add(-376, -209);
		shape.add(378, -210);
		shape.add(379, 208);
		shape.add(-378, 210);
		shape.initTail(this);
	}

	public void draw() {
		background(Color.BLACK.getRGB());
		context.update();
		pushMatrix();

		// set the color for filling the shape
		fill(Color.YELLOW.getRGB());
		translate(shape.x, shape.y);

		beginShape();

		// uncomment to draw NO shape outlines
		noStroke();
		// uncomment to set the outline color
		// stroke(Color.BLACK.getRGB());
		// uncomment to set the outline weight
		// strokeWeight(3);

		// draw a vertex between all points of a shape
		for (Point point : shape.getPoints()) {
			vertex(point.x, point.y);
		}

		endShape(CLOSE);

		popMatrix();

		for (int i = 0; i < 3; i++) {
			if (context.isTrackingSkeleton(i)) {
				PVector jointPos_conv = new PVector();
				PVector jointPos = new PVector();
				context.getJointPositionSkeleton(i,
						SimpleOpenNI.SKEL_LEFT_HAND, jointPos);
				context.convertRealWorldToProjective(jointPos, jointPos_conv);
				fill(Color.BLUE.getRGB());
				float eX = jointPos_conv.x
						* (shape.getBoundingBox().getWidth() * 2)
						/ displayWidth + shape.getBoundingBox().getLeftTop().x;
				float eY = jointPos_conv.y
						* (shape.getBoundingBox().getHeight() * 2)
						/ displayHeight + shape.getBoundingBox().getLeftTop().y;

				ellipse(eX, eY, 20, 20);
				PVector handPos = new PVector(eX, eY);
				for (int j = 0; j < shape.tail.length; j++) {
					if (shape.getPolygon().contains(handPos.x, handPos.y)) {
						shape.tail[j].checkEdges();
						shape.tail[j].update(handPos);
						shape.tail[j].display();
					}
				}
			}

		}
	}
}
