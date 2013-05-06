package orginal;

import processing.core.PApplet;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tobi (iBot), 08th April 2013
 *  Based on:
 *      auto generated code from Mapper.jar
 *      some example code from the project 'Interaktive Installationen', HAW Hamburg, SS2013
 * Used external libraries:
 *  Processing 2.08b, jogl
 *      http://www.processing.org/
 */

/**
 * This class contains code, to color different Shapes
 */
public class ProcessingShapesOriginal extends PApplet {

    /**
     * This List will contain all the Shapes which we will fill with different colors
     */
    private List<Shape> shapes = new LinkedList<>();

    /**
     * The time variable will be used to calculate the time difference between the update intervals
     */
    private long time;

    /**
     * Run this program as Java application to start the PAppplet in fullscreen mode
     */
    static public void main(String args[]) {
        PApplet.main(new String[]{"--present", "--bgcolor=#000000", "--present-stop-color=#000000",
                "orginal.ProcessingShapesOriginal"});
    }


    /**
     * This methode will be called from a super class or somewhere else... idk...
     * Whatever: Do all the stuff tu setup your processing sketch here!
     */
    @Override
    public void setup() {
        // size for fullscreen window, renderer OpenGL
        //  Don't forget to import native libraries for your OS. If OpenGL doesn't work, render without OpenGL...
//        size(displayWidth, displayHeight, OPENGL);
        size(displayWidth, displayHeight);

        initShapes();
        background(Color.BLACK.getRGB());
        time = System.currentTimeMillis();
    }

    /**
     * Another method that will be called automatically...
     * Do all the drawing stuff in this method.
     */
    @Override
    public void draw() {

        // update only after 1000 sec.
        if ((System.currentTimeMillis() - time) > 1000) {

            // iterrate over all the shapes to update them
            for (Shape shape : shapes) {

                // draw random colored shapes
                drawShape(shape, new Color(random(1), random(1), random(1)));
            }

            // set time to current system time
            time = System.currentTimeMillis();
        }
    }

    /**
     * This method will create all the shapes for this sketch and add them to the List shapes
     * If you use the Mapper.jar to map surfaces:
     *  Use 'Export' > 'Processing' to create the 'snippet.txt' and replace the content of this method with the content
     *  from the initShapes() method of that file.
     * Examples shapes are created with a display resolution of 1920 *1080
     */
    private void initShapes() {
        shapes.add(new Shape(526, 453));
        shapes.add(new Shape(997, 499));
        shapes.add(new Shape(1466, 551));

        shapes.get(0).add(-148, -85);
        shapes.get(0).add(-142, -172);
        shapes.get(0).add(61, -255);
        shapes.get(0).add(149, -250);
        shapes.get(0).add(123, 256);
        shapes.get(0).add(-12, 252);
        shapes.get(0).add(54, -181);

        shapes.get(1).add(-200, -124);
        shapes.get(1).add(-118, -120);
        shapes.get(1).add(-85, -199);
        shapes.get(1).add(-2, -217);
        shapes.get(1).add(72, -192);
        shapes.get(1).add(104, -111);
        shapes.get(1).add(-43, 42);
        shapes.get(1).add(-211, 190);
        shapes.get(1).add(-216, 281);
        shapes.get(1).add(190, 298);
        shapes.get(1).add(189, 209);
        shapes.get(1).add(-69, 195);
        shapes.get(1).add(210, -97);
        shapes.get(1).add(217, -190);
        shapes.get(1).add(155, -265);
        shapes.get(1).add(38, -298);
        shapes.get(1).add(-78, -273);
        shapes.get(1).add(-180, -227);

        shapes.get(2).add(-188, -358);
        shapes.get(2).add(-187, -247);
        shapes.get(2).add(-42, -244);
        shapes.get(2).add(40, -182);
        shapes.get(2).add(50, -111);
        shapes.get(2).add(-19, -60);
        shapes.get(2).add(-96, -67);
        shapes.get(2).add(-110, 37);
        shapes.get(2).add(-20, 49);
        shapes.get(2).add(68, 112);
        shapes.get(2).add(73, 173);
        shapes.get(2).add(5, 230);
        shapes.get(2).add(-191, 219);
        shapes.get(2).add(-189, 335);
        shapes.get(2).add(7, 359);
        shapes.get(2).add(133, 329);
        shapes.get(2).add(179, 249);
        shapes.get(2).add(192, 82);
        shapes.get(2).add(142, 35);
        shapes.get(2).add(190, -13);
        shapes.get(2).add(180, -200);
        shapes.get(2).add(131, -305);
        shapes.get(2).add(12, -351);
    }



    /**
     * This method draws a shape and fill it with an color
     * @param shape
     * @param color
     */
    private void drawShape(Shape shape, Color color) {
        // I didn't analyse what pushMatrix(), translate(x,y) and popMatrix() do yet, but without calling this methods,
        //  the shapes will be drawn on a wrong position.
        // TODO: Understand what this methods exactly does...
        pushMatrix();

        // set the color for filling the shape
        fill(color.getRGB());

        translate(shape.x, shape.y);

        beginShape();

        // uncomment to draw NO shape outlines
//        noStroke();
        // uncomment to set the outline color
        stroke(Color.BLACK.getRGB());
        // uncomment to set the outline weight
        strokeWeight(3);

        // draw a vertex between all points of a shape
        for (Point point : shape.points) {
            vertex(point.x, point.y);
        }

        endShape(CLOSE);
        popMatrix();
    }

    private class Shape extends Point {
        public List<Point> points = new LinkedList<>();

        public Shape(int x, int y) {
            super(x, y);
        }

        public void add(int x, int y) {
            points.add(new Point(x, y));
        }
    }

    private class Point {
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
