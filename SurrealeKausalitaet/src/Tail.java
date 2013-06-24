import java.awt.Color;

import processing.core.PApplet;
import processing.core.PVector;

public class Tail extends PApplet {
	PVector location;

	PVector velocity;

	PVector acceleration;

	PVector jitter;

	float topspeed;

	float[] holdX = new float[20];
	float[] holdY = new float[20];

	BoundingBox bb;
	PApplet applet;
	Shape shape;

	public Tail(BoundingBox bb, PApplet applet, Shape shape){
		location = new PVector(random(bb.getLeftTop().x, 
				bb.getWidth()+bb.getLeftTop().x), random(bb.getLeftTop().y, 
						bb.getHeight()+bb.getLeftTop().y));
		this.applet = applet;
		this.shape = shape;
		velocity = new PVector(0, 0);
		this.bb = bb;
		// adds a random jitter factor to each worm to stop them clumping
		float l = (.5f - random(1));
		float m = (.5f - random(1));
		jitter = new PVector(l, m);

		topspeed = 7;
	}

	void update(PVector position) {

		PVector dir = PVector.sub(position, location);

		dir.normalize();

		acceleration = dir;

		acceleration.add(jitter);

		velocity.add(acceleration);

		velocity.limit(topspeed);

		location.add(velocity);

	}

	void display() {

//		stroke(location.x, location.y, location.y);

		applet.fill(Color.WHITE.getRGB());

		applet.ellipse(holdX[0], holdY[0], 20, 20);

		// generates the tail drawing
		for (int i = 0; i < 20; i++) {
			if(shape.getPolygon().contains(holdX[i], holdY[i]))
				applet.ellipse(holdX[i], holdY[i], 10 - i/2, 10 - i/2);
		}

		// stores the current location and shifts previous locations down the
		// tail
		for (int i = 19; i >= 1; i--) {
			holdX[i] = holdX[i - 1];
			holdY[i] = holdY[i - 1];
		}
		holdX[0] = location.x;
		holdY[0] = location.y;
		// println(holdX[0]);
		// println(holdY[0]);
	}

	void checkEdges() {

		if ((location.x > bb.getWidth() + bb.getLeftTop().x)
				|| (location.x < bb.getLeftTop().x)) {

			velocity.x = velocity.x * -1;

		}

		if ((location.y > bb.getHeight() + bb.getLeftTop().y)
				|| (location.y < bb.getLeftTop().y)) {

			velocity.y = velocity.y * -1;

		}

	}
}
