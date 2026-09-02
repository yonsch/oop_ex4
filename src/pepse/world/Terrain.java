package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Terrain {
    private static final Color BASE_GROUND_COLOR = new Color(212,
            123, 74);

    public Terrain(Vector2 windowDimensions, int seed) {
    }

    public Block createBlock(int x, int y) {
        Block block = new Block(
                new Vector2(x, y),
                new RectangleRenderable(BASE_GROUND_COLOR)
        );
//        block.setTag(GROUND_TAG);
        return block;
    }
}
