import ddf.minim.AudioListener;
import ddf.minim.AudioSignal;

/**
 * Created with IntelliJ IDEA.
 * User: tobi
 * Date: 29.04.13
 * Time: 11:44
 * To change this template use File | Settings | File Templates.
 */
class PlayBack implements AudioSignal, AudioListener { //Just a simple "re-route" audio class.
    float[] left, right;

    PlayBack() {
    }

    //Getting.
    public void samples(float[] arg0) {
        left = arg0;
    }

    public void samples(float[] arg0, float[] arg1) {
        left = arg0;
        right = arg1;
    }
    //Sending back.
    public void generate(float[] arg0) {
        System.arraycopy(left, 0, arg0, 0, arg0.length);
    }

    public void generate(float[] arg0, float[] arg1) {
        System.out.println(arg0[0]);
        if (left!=null && right!=null){
            System.arraycopy(left, 0, arg0, 0, arg0.length);
            System.arraycopy(right, 0, arg1, 0, arg1.length);
        }
    }
}