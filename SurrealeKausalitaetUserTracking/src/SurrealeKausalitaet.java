import SimpleOpenNI.SimpleOpenNI;
import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Main class of the program, contains the logic, interaction and visual effects
 */
public class SurrealeKausalitaet extends PApplet {

    /**
     * This List will contain all the Shapes which are not assigned to any user
     */
    public static List<Shape> shapes = Collections.synchronizedList(new LinkedList<Shape>());
    private static Random rand = new Random();
    /**
     * The SimpleOpenNI context (Kinect sensor) for this program
     */
    SimpleOpenNI context;
    private Map<Integer, Float> users = new HashMap<>();
    /**
     * By converting rela world position to perspective positions the result is a vector within the kinect resolution of 640 * 480 pixels.
     * This scaleFactor will be used to scale the x coordinate (between 0 and 640) to the display width (between 0 and displayWidth)
     */
    private float scaleFactorX;
    /**
     * Will be set on true as soon as the kinect is configured. Nice for testing
     * without kinect: If kinect is not configured, don't use kinect stuff...
     */

    private UDPClient udpClient = new UDPClient();
    private UDPServer udpServer = new UDPServer();
    private Thread serverThread;

    /**
     * Run this program as Java application to start the PAppplet in fullscreen
     * mode
     */
    static public void main(String args[]) {
        PApplet.main(new String[]{"--present", "--bgcolor=#000000",
                "--present-stop-color=#000000", "SurrealeKausalitaet"});
    }

    /**
     * Setup all basic stuff for the program
     */
    @Override
    public void setup() {
        size(displayWidth, displayHeight, OPENGL);
        scaleFactorX = displayWidth / 640f;

        colorMode(HSB, displayWidth, 99, 99);

        initShapes();
        background(Color.BLACK.getRGB());
        stroke(0, 255, 0);
        strokeWeight(3);
        smooth();
        setupKinect();

        Runnable server = new Runnable() {
            @Override
            public void run() {
                udpServer.startJob();
            }
        };

        serverThread = new Thread(server);
        serverThread.start();




    }

    /**
     * Setting up the kinect connection and all the stuff that is necessary to
     * use the kinect sensor
     */
    private void setupKinect() {
        // instantiate a new context
        context = new SimpleOpenNI(this);

        System.out.println(context + "");
        // enable depthMap generation
        context.enableDepth();

        // enable skeleton generation for all joints
        context.enableUser(SimpleOpenNI.SKEL_PROFILE_NONE);

        context.setMirror(true);
    }

    private void end() {
        if (serverThread != null) {
            serverThread.interrupt();
            udpServer.stopJob();
        }
        exit();
    }

    /**
     * Draws all everything which will be displayed
     */
    @Override
    public void draw() {
        if (keyPressed) {
            if (key == 'q' || key == 'Q') {
                end();
            }
        }


        background(Color.BLACK.getRGB());
        context.update();

        int validUsers = 0;
        float hueDif = 0;
        for (Map.Entry<Integer, Float> userEntry : users.entrySet()) {
            int userID = userEntry.getKey();
            PVector position = getPosition(userID);
            if (Float.isNaN(position.x)) {
                Float oldPosition = userEntry.getValue();
                if (!oldPosition.isNaN()) {
                    hueDif += oldPosition * scaleFactorX;
                    validUsers++;
                }
            } else {
                userEntry.setValue(position.x);
                hueDif += position.x * scaleFactorX;
                validUsers++;
            }

        }

        String ownExchangeValues = String.format("%d#%s", validUsers, hueDif);


        udpClient.sendMessage(ownExchangeValues);

        String other = udpServer.getMessage();
        int otherNumberOfUsers = 0;
        float otherHueDif = 0f;
        String[] values;
        if (other != null) {
            values = other.split("#");
            try {
                otherNumberOfUsers = Integer.parseInt(values[0]);
                otherHueDif = Float.parseFloat(values[1]);
            } catch (NumberFormatException nfe) {
                //if the Values are not valid, they will be ignored
            }
        }
        float commonHueDif = hueDif + otherHueDif;
        int commonNumOfUsers = validUsers + otherNumberOfUsers;
        if (commonNumOfUsers > 0) {
            commonHueDif = commonHueDif / commonNumOfUsers;
        }

        //Draw all shapes assigned to an user
        for (Shape shape : shapes) {
            drawColoredShape(shape, getChangedColor2(commonHueDif, shape));
        }


    }

    /**
     * This method draws a shape and fill it with an color
     *
     * @param shape the shape which will contain the tail
     * @param hue   the color of the shape
     */
    private void drawColoredShape(Shape shape, float hue) {

        pushMatrix();
        hue = hue % displayWidth;
        // set the color for filling the shape
        colorMode(HSB, displayWidth, 99, 99);
        int neu = color(hue, 99, 99);
        Color colorNeu = new Color(neu);
//        System.out.println("ColorNeu " + colorNeu);
        fill(colorNeu.getRGB());
        translate(shape.x, shape.y);

        beginShape();

        // set the outline color
        stroke(Color.BLACK.getRGB());
        // set the outline weight
        strokeWeight(3);

        // draw a vertex between all points of a shape
        for (Point point : shape.getPoints()) {
            vertex(point.x, point.y);
        }

        endShape(CLOSE);
        popMatrix();

    }

    /**
     * Detects position of a users
     *
     * @param userID the user id of the user whose position will be detected
     * @return PVector with coordinates of the joint
     */
    private PVector getPosition(int userID) {

        PVector position = new PVector();
        PVector position_conv = new PVector();
        //Get "Center of Mass" of the user
        context.getCoM(userID, position);
        context.convertRealWorldToProjective(position, position_conv);
        return position_conv;
    }

    /**
     * This Method is used to recalculate the Color of a Shape, based on the
     * position of a given joint
     *
     * @param position
     * @param shape    the shape, whose color should be adjusted
     * @return the recalculated color
     */
//    private Color getChangedColor(PVector position, Shape shape) {
//
//        float pos = position.x * scaleFactorX;
//
//        float haelfte = displayWidth / 2f;
//        Color c = shape.getColor();
//        int red = 0, green = 0, blue = 0;
//        float delta = 60;
//        float abweichungsFaktor;
//        if (pos >= haelfte) {
//            abweichungsFaktor = 1 + ((pos - haelfte) * delta / 100 / haelfte);
//        } else {
//            abweichungsFaktor = 1 - ((haelfte - pos) * delta / 100 / haelfte);
//        }
//
//
//        red = Math.round(c.getRed() * abweichungsFaktor);
//        green = Math.round(c.getGreen() * abweichungsFaktor);
//        blue = Math.round(c.getBlue() * abweichungsFaktor);
//
//
//        if (red < 0)
//            red = 0;
//        if (red > 255)
//            red = 255;
//        if (green < 0)
//            green = 0;
//        if (green > 255)
//            green = 255;
//        if (blue < 0)
//            blue = 0;
//        if (blue > 255)
//            blue = 255;
//
//        Color neu = c;
//        if (shape.getColorToChange().equals(ColorToChange.RED)) {
//            neu = new Color(red, c.getGreen(), c.getBlue());
//        } else if (shape.getColorToChange().equals(ColorToChange.BLUE)) {
//            neu = new Color(c.getRed(), c.getGreen(), blue);
//        } else {
//            neu = new Color(c.getRed(), green, c.getBlue());
//        }
//        return neu;
//    }

//    /**
//     * This Method is used to recalculate the Color of a Shape, based on the position of
//     * a given joint
//     *
//     * @param pos
//     * @param shape the shape, whose color should be adjusted
//     * @return the recalculated color
//     */
//    private Color getChangedSaturationColor(PVector pos, Shape shape) {
//        // jointPos contains coordinats of realWorld
//
//        float posX = pos.x * scaleFactorX;
////        float hue = posX / displayWidth;
//        Color c = shape.getColor();
//        float[] hsb = new float[3];
//        // System.out.println("Red: " + c.getRed() + " Green: " + c.getGreen()
//        // + " getBlue: " + c.getBlue() + " hsb: " + hsb);
//        Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsb);
//        colorMode(HSB, displayWidth, 99, 99);
//        int neu = color(posX, hsb[1], hsb[2]);
//        Color colorNeu = new Color(neu);
//        return colorNeu;
//    }

    /**
     * This Method is used to recalculate the Color of a Shape, based on the position of
     * a given joint
     *
     * @param shape the shape, whose color should be adjusted
     * @return the recalculated color
     */
    private float getChangedColor2(float hueDif, Shape shape) {

        float hue = shape.getHue();
        float newHue = hue + hueDif;
//        System.out.println(hueDif);
        return newHue;
//        int neu =  color(newHue, 99, 99);
//        Color colorNeu = new Color(neu);
//        return colorNeu;
    }

    /**
     * when a person ('user') enters the field of view
     *
     * @param userId id of the new user
     */
    @SuppressWarnings("unused")
    public void onNewUser(int userId) {
        println("detected" + userId);
        users.put(userId, Float.NaN);
    }

    /**
     * when a person ('user') leaves the field of view
     *
     * @param userId id of the loft user
     */
    @SuppressWarnings("unused")
    public void onLostUser(int userId) {
        println("lost: " + userId);

        users.remove(userId);
    }

    /**
     * This method will create all the shapes for this sketch and add them to
     * the List shapes If you use the Mapper.jar to map surfaces: Use 'Export' >
     * 'Processing' to create the 'snippet.txt' and replace the content of this
     * method with the content from the initShapes() method of that file.
     * Examples shapes are created with a display resolution of 1920 *1080
     */
    private void initShapes() {
//        shapes.add(new Shape(389, 438));
//        shapes.add(new Shape(138, 450));
//        shapes.add(new Shape(494, 417));
//        shapes.add(new Shape(601, 457));
//        shapes.add(new Shape(601, 527));
//        shapes.add(new Shape(406, 536));
//        shapes.add(new Shape(185, 585));
//        shapes.add(new Shape(137, 496));
//
//        shapes.get(0).add(-113, -46);
//        shapes.get(0).add(63, -77);
//        shapes.get(0).add(114, 40);
//        shapes.get(0).add(-108, 77);
//
//        shapes.get(1).add(127, -59);
//        shapes.get(1).add(-111, -61);
//        shapes.get(1).add(-133, -16);
//        shapes.get(1).add(-130, -7);
//        shapes.get(1).add(133, 61);
//
//        shapes.get(2).add(-28, -53);
//        shapes.get(2).add(28, -39);
//        shapes.get(2).add(15, 54);
//
//        shapes.get(3).add(-71, -72);
//        shapes.get(3).add(82, 68);
//        shapes.get(3).add(80, 73);
//        shapes.get(3).add(-82, 19);
//
//        shapes.get(4).add(-82, -41);
//        shapes.get(4).add(-55, 36);
//        shapes.get(4).add(82, 42);
//        shapes.get(4).add(78, 12);
//
//        shapes.get(5).add(104, -48);
//        shapes.get(5).add(-126, -10);
//        shapes.get(5).add(-63, 48);
//        shapes.get(5).add(126, 25);
//
//        shapes.get(6).add(144, 2);
//        shapes.get(6).add(-57, 61);
//        shapes.get(6).add(-144, -38);
//        shapes.get(6).add(86, -60);
//
//        shapes.get(7).add(-121, -37);
//        shapes.get(7).add(122, 23);
//        shapes.get(7).add(-101, 37);
//        // Orange --> gr��n
//        shapes.get(0).setColor(new Color(0xD04328));
//        shapes.get(0).setColorToChange(ColorToChange.GREEN);
//        // //Braun - gr��n
//        // shapes.get(1).setColor(new Color(0x63382D));
//        // shapes.get(1).setColorToChange(ColorToChange.GREEN);
//        // //Ocker -->gr��n
//        shapes.get(2).setColor(new Color(0xC2782F));
//        shapes.get(2).setColorToChange(ColorToChange.GREEN);
//        // //Laubgr��n -> gr��n ver��ndern
//        shapes.get(3).setColor(new Color(0x346E45));
//        shapes.get(3).setColorToChange(ColorToChange.GREEN);
//        // //Kobaldblau --> gr��n
//        shapes.get(4).setColor(new Color(0x344761));
//        shapes.get(4).setColorToChange(ColorToChange.GREEN);
//        // //Rot-Orange -->gr��n
//        shapes.get(5).setColor(new Color(0x5A4826));
//        shapes.get(5).setColorToChange(ColorToChange.GREEN);
//        //
//        // -->gr��n
//        shapes.get(6).setColor(new Color(0x796659));
//        shapes.get(6).setColorToChange(ColorToChange.GREEN);
////        blau
//        shapes.get(7).setColor(new Color(0x3A7487));
//        shapes.get(7).setColorToChange(ColorToChange.BLUE);
//        // blau
//        shapes.get(1).setColor(new Color(0x7D9F64));
//        shapes.get(1).setColorToChange(ColorToChange.BLUE);
//        // gr��n
//        // shapes.get(0).setColor(new Color(0xC63D30));
//        // shapes.get(0).setColorToChange(ColorToChange.GREEN);
//        // gr��n
//        // shapes.get(0).setColor(new Color(0x1C764F));
//        // shapes.get(0).setColorToChange(ColorToChange.GREEN);

        shapes.add(new Shape(202, 109));
        shapes.add(new Shape(428, 196));
        shapes.add(new Shape(874, 408));
        shapes.add(new Shape(649, 423));
        shapes.add(new Shape(611, 224));
        shapes.add(new Shape(327, 442));
        shapes.add(new Shape(444, 450));
        shapes.add(new Shape(391, 612));
        shapes.add(new Shape(352, 539));
        shapes.add(new Shape(326, 372));
        shapes.add(new Shape(455, 374));
        shapes.add(new Shape(651, 529));
        shapes.add(new Shape(930, 169));
        shapes.add(new Shape(1219, 98));
        shapes.add(new Shape(1224, 656));
        shapes.add(new Shape(509, 78));

        shapes.get(0).add(-61, -80);
        shapes.get(0).add(-85, 81);
        shapes.get(0).add(86, 65);
        shapes.get(0).add(57, -18);

        shapes.get(1).add(-57, -53);
        shapes.get(1).add(-95, 64);
        shapes.get(1).add(88, 89);
        shapes.get(1).add(96, -36);
        shapes.get(1).add(-53, -89);

        shapes.get(2).add(-135, -171);
        shapes.get(2).add(-157, -62);
        shapes.get(2).add(-11, -107);
        shapes.get(2).add(-18, -191);
        shapes.get(2).add(-154, -247);
        shapes.get(2).add(-158, -213);
        shapes.get(2).add(-184, -190);
        shapes.get(2).add(-185, -136);
        shapes.get(2).add(-258, -142);
        shapes.get(2).add(-238, -72);
        shapes.get(2).add(-217, -37);
        shapes.get(2).add(-162, -30);
        shapes.get(2).add(-210, -5);
        shapes.get(2).add(-118, -4);
        shapes.get(2).add(-145, 41);
        shapes.get(2).add(25, 53);
        shapes.get(2).add(-156, 247);
        shapes.get(2).add(259, 16);
        shapes.get(2).add(156, -18);
        shapes.get(2).add(121, 45);
        shapes.get(2).add(38, 4);
        shapes.get(2).add(-44, -16);
        shapes.get(2).add(220, -66);
        shapes.get(2).add(-180, -50);

        shapes.get(3).add(386, -263);
        shapes.get(3).add(388, -286);
        shapes.get(3).add(414, -298);
        shapes.get(3).add(442, -305);
        shapes.get(3).add(462, -299);
        shapes.get(3).add(487, -288);
        shapes.get(3).add(510, -272);
        shapes.get(3).add(555, -247);
        shapes.get(3).add(564, -233);
        shapes.get(3).add(585, -210);
        shapes.get(3).add(601, -179);
        shapes.get(3).add(612, -162);
        shapes.get(3).add(604, -120);
        shapes.get(3).add(620, -27);
        shapes.get(3).add(607, 26);
        shapes.get(3).add(597, 75);
        shapes.get(3).add(506, 209);
        shapes.get(3).add(386, 266);
        shapes.get(3).add(310, 298);
        shapes.get(3).add(221, 316);
        shapes.get(3).add(-125, 317);
        shapes.get(3).add(-459, 312);
        shapes.get(3).add(-586, 281);
        shapes.get(3).add(-599, 266);
        shapes.get(3).add(-606, 235);
        shapes.get(3).add(-610, 199);
        shapes.get(3).add(-601, 109);
        shapes.get(3).add(-603, -106);
        shapes.get(3).add(-620, -257);
        shapes.get(3).add(-555, -316);
        shapes.get(3).add(-550, -262);
        shapes.get(3).add(-565, -159);
        shapes.get(3).add(-507, -35);
        shapes.get(3).add(-428, -116);
        shapes.get(3).add(-466, -11);
        shapes.get(3).add(-525, 6);
        shapes.get(3).add(-554, -35);
        shapes.get(3).add(-547, 81);
        shapes.get(3).add(-549, 214);
        shapes.get(3).add(-354, 283);
        shapes.get(3).add(-112, 291);
        shapes.get(3).add(178, 273);
        shapes.get(3).add(354, 238);
        shapes.get(3).add(423, 199);
        shapes.get(3).add(485, 132);
        shapes.get(3).add(534, 27);
        shapes.get(3).add(529, -31);
        shapes.get(3).add(550, -105);
        shapes.get(3).add(551, -174);
        shapes.get(3).add(506, -208);
        shapes.get(3).add(461, -229);
        shapes.get(3).add(441, -188);
        shapes.get(3).add(393, -154);
        shapes.get(3).add(360, -194);
        shapes.get(3).add(334, -241);
        shapes.get(3).add(330, -270);
        shapes.get(3).add(365, -271);

        shapes.get(4).add(62, -148);
        shapes.get(4).add(-104, 149);
        shapes.get(4).add(105, -134);

        shapes.get(5).add(-22, -19);
        shapes.get(5).add(5, -36);
        shapes.get(5).add(26, -20);
        shapes.get(5).add(36, 3);
        shapes.get(5).add(16, 27);
        shapes.get(5).add(-16, 37);
        shapes.get(5).add(-34, 23);
        shapes.get(5).add(-35, -1);

        shapes.get(6).add(-15, -36);
        shapes.get(6).add(-38, -21);
        shapes.get(6).add(-39, 0);
        shapes.get(6).add(-39, 23);
        shapes.get(6).add(-18, 39);
        shapes.get(6).add(3, 39);
        shapes.get(6).add(25, 33);
        shapes.get(6).add(36, 14);
        shapes.get(6).add(39, -10);
        shapes.get(6).add(25, -27);
        shapes.get(6).add(6, -38);

        shapes.get(7).add(-133, -54);
        shapes.get(7).add(-88, 28);
        shapes.get(7).add(-29, 55);
        shapes.get(7).add(41, 49);
        shapes.get(7).add(110, 15);
        shapes.get(7).add(134, -35);
        shapes.get(7).add(74, -10);
        shapes.get(7).add(18, 6);
        shapes.get(7).add(-37, 3);
        shapes.get(7).add(-71, -7);

        shapes.get(8).add(17, -29);
        shapes.get(8).add(-31, 30);
        shapes.get(8).add(23, 13);
        shapes.get(8).add(31, -7);

        shapes.get(9).add(-44, 15);
        shapes.get(9).add(0, 2);
        shapes.get(9).add(44, 25);
        shapes.get(9).add(-7, -25);

        shapes.get(10).add(-41, 21);
        shapes.get(10).add(27, -24);
        shapes.get(10).add(42, 25);
        shapes.get(10).add(19, 1);

        shapes.get(11).add(-52, -65);
        shapes.get(11).add(-76, 36);
        shapes.get(11).add(38, 65);
        shapes.get(11).add(76, 9);
        shapes.get(11).add(-9, -60);

        shapes.get(12).add(-99, -106);
        shapes.get(12).add(-101, -7);
        shapes.get(12).add(-45, 17);
        shapes.get(12).add(-21, 132);
        shapes.get(12).add(58, 135);
        shapes.get(12).add(19, -28);
        shapes.get(12).add(101, -72);
        shapes.get(12).add(63, -134);
        shapes.get(12).add(-86, -106);

        shapes.get(13).add(-96, -71);
        shapes.get(13).add(97, 73);
        shapes.get(13).add(97, -72);

        shapes.get(14).add(-81, 79);
        shapes.get(14).add(82, -78);
        shapes.get(14).add(82, 79);

        shapes.get(15).add(-7, -19);
        shapes.get(15).add(44, 36);
        shapes.get(15).add(-44, -13);
        shapes.get(15).add(35, -36);

        for (Shape s : shapes) {
            float hue = rand.nextFloat() * displayWidth;
            s.setHue(hue);
        }

    }

}
