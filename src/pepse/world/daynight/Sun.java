package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sun {
    private static final float initialSunCenterX = 200f;
    private static final float initialSunCenterY = 200f;

    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        GameObject sun = new GameObject(
                new Vector2(300, 300),
                new Vector2(50, 50),
                new OvalRenderable(Color.YELLOW)
        );
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        Vector2 initialSunCenter = new Vector2(initialSunCenterX, initialSunCenterY);
        Vector2 cycleCenter = new Vector2(0, windowDimensions.y());

        new Transition<Float>(
                sun,
                (Float angle) -> sun.setCenter
                        (initialSunCenter.subtract(cycleCenter)
                                .rotated(angle)
                                .add(cycleCenter)),
                0f,
                360f,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength * 2,
                Transition.TransitionType.TRANSITION_LOOP,
                null
        );

        return sun;
    }
}
