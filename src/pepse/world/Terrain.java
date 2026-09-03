package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.utils.NoiseGenerator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Terrain {
    private static final Color BASE_GROUND_COLOR = new Color(212,
            123, 74);
    private static final float NOISE_FACTOR = Block.SIZE * 7;
    private final float groundHeightAtX0;
    private final NoiseGenerator noiseGenerator;
    private static final int TERRAIN_DEPTH = 30;

    public Terrain(Vector2 windowDimensions, int seed) {
        groundHeightAtX0 = windowDimensions.y() * 2f / 3f;
        noiseGenerator = new NoiseGenerator(seed, (int)groundHeightAtX0);
    }

    public Block createBlock(int x, int y) {
        Block block = new Block(
                new Vector2(x, y),
                new RectangleRenderable(BASE_GROUND_COLOR)
        );
//        block.setTag(GROUND_TAG);
        return block;
    }

    public float groundHeightAt(float x) {
        float noise = (float) noiseGenerator.noise(x, NOISE_FACTOR);
        float height = groundHeightAtX0 + noise;
        int div = Math.floorDiv((int) height, Block.SIZE);
        float finalHeight = div * Block.SIZE;
        return finalHeight;
    }

    public List<GameObject> createColumn(int x) {
        int maxHeight = (int) groundHeightAt(x);
        int minHeight = maxHeight + (TERRAIN_DEPTH * Block.SIZE);
        List<GameObject> column = new ArrayList<>();
        for (int y = maxHeight; y <= minHeight; y += Block.SIZE) {
            column.add(createBlock(x, y));
        }
        return column;
    }

    public List<GameObject> createInRange(int minX, int maxX) {
        List<GameObject> blocks = new ArrayList<>();
        int currentX = (minX / Block.SIZE) * Block.SIZE;
        while (currentX <= maxX) {
            var column = createColumn(currentX);
            currentX += Block.SIZE;
            blocks.addAll(column);
        }
        return blocks;
    }
}
