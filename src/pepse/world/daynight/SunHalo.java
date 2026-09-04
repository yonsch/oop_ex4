package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo {
    public static GameObject create(GameObject sun) {
        GameObject sunHalo = new GameObject(
                new Vector2(300, 300),
                new Vector2(90, 90),
                new OvalRenderable(new Color(255, 255, 0, 40))
        );
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.addComponent((Float) -> sunHalo.setCenter(sun.getCenter()));
        return sunHalo;
    }
}
