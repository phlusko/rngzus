package rngzus.com.dd.p4u1.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Paul on 12/27/2016.
 */

public class PaulGraphics {

    public static final float width = 1224;
    public static final float height = 1824;
    public static final float GAMEHEIGHT = Gdx.graphics.getHeight();
    public static final float GAMEWIDTH = Gdx.graphics.getWidth();

    static public Vector2 pixelToCoord(Vector2 arg0) {

        float x = (arg0.x * width) / GAMEWIDTH;
        float y = ((GAMEHEIGHT - arg0.y) * height) / GAMEHEIGHT;

        return new Vector2(x, y);
    }

    static public float getAngleFromVectors(Vector2 arg0, Vector2 center) {
        Vector2 ray = center.cpy().sub(arg0);
        ray = ray.nor();
        double angle = Math.acos(ray.y);
        angle = Math.toDegrees(angle);
        if (arg0.x < center.x) {
            angle = 360 - angle;
        }
        return (float)Math.floor(angle);
    }

    static public float getAngleFromArcLength(float length, float radius) {
        float circ = 2*((float)Math.PI)*radius;
        float angle = 360 * (length / circ);
        return angle;
    }

    static public Vector2 getOrtho(Vector2 arg0, Vector2 arg1){
        Vector2 ray = arg0.cpy().sub(arg1);
        ray.scl(1/ray.len());
        Vector2 ortho = new Vector2(-ray.y, ray.x);
        return ortho;
    }
}
