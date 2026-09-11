package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.utils.ColorSupplier;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a column of blocks.
 */
public class Column {
    private final List<GameObject> blocks = new ArrayList<>();

    /**
     * Create a column.
     * @param x X coordinate of the left edge of a block
     * @param minHeight Height of the column bottom
     * @param maxHeight Height of the column top
     * @param color Base color to use for the blocks
     */
    public Column(int x, int minHeight, int maxHeight, Color color) {
        for (int y = maxHeight; y <= minHeight; y += Block.SIZE) {
            Block block = createBlock(x, y, color);

            if(y == maxHeight) {
                block.setTag(PepseGameManager.GROUND_SURFACE_TAG);
            } else {
                block.setTag(PepseGameManager.GROUND_INNER_TAG);
            }

            blocks.add(block);
        }
    }

    /**
     * Get the blocks
     * @return Blocks in the column
     */
    public List<GameObject> getBlocks() {
        return blocks;
    }

    /**
     * Helper function to create a block.
     * @param x X coordinate
     * @param y Y coordinate
     * @param color Base color of the block
     * @return New block
     */
    private Block createBlock(int x, int y, Color color) {
        return new Block(
                new Vector2(x, y),
                new RectangleRenderable(ColorSupplier.approximateColor(
                        color))
        );
    }
}
