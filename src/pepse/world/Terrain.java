package pepse.world;

import danogl.GameObject;
import danogl.util.Vector2;
import pepse.utils.NoiseGenerator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the ground
 */
public class Terrain {
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final float NOISE_FACTOR = Block.SIZE * 7;
    private final float groundHeightAtX0;
    private final NoiseGenerator noiseGenerator;
    private static final int TERRAIN_DEPTH = 30;
    private final Map<Integer, Column> columns = new HashMap<Integer, Column>();

    /**
     * Create the terrain
     * @param windowDimensions Dimensions of the window
     * @param seed Seed to use for the PRNG
     */
    public Terrain(Vector2 windowDimensions, int seed) {
        groundHeightAtX0 = windowDimensions.y() * (2f / 3f);
        noiseGenerator = new NoiseGenerator(seed, (int)groundHeightAtX0);
    }

    /**
     * Return the ground height a coordinate
     * @param x X coordinate
     * @return Height at x
     */
    public float groundHeightAt(float x) {
        float noise = (float) noiseGenerator.noise(x, NOISE_FACTOR);
        float height = groundHeightAtX0 + noise;
        int div = Math.floorDiv((int) height, Block.SIZE);
        return div * Block.SIZE;
    }

    /**
     * Create ground in a range
     * @param minX Left boundary
     * @param maxX Right boundary
     * @return GameObjects to add to the engine
     */
    public List<GameObject> createInRange(int minX, int maxX) {

        int currentX = (minX / Block.SIZE) * Block.SIZE;
        List<GameObject> blocks = new ArrayList<>();

        while (currentX <= maxX) {
            int maxHeight = (int) groundHeightAt(currentX);
            int minHeight = maxHeight + (TERRAIN_DEPTH * Block.SIZE);
            var column = new Column(currentX, minHeight, maxHeight, BASE_GROUND_COLOR);
            columns.put(currentX, column);
            currentX += Block.SIZE;
            blocks.addAll(column.getBlocks());
        }
        return blocks;
    }

    /**
     * Remove ground in a range
     * @param minX Left boundary
     * @param maxX Right boundary
     * @return GameObjects to remove from the engine
     */
    public List<GameObject> removeInRange(int minX, int maxX) {

        int currentX = (minX / Block.SIZE) * Block.SIZE;
        List<GameObject> blocks = new ArrayList<>();

        while (currentX <= maxX) {
            currentX += Block.SIZE;
            if(columns.containsKey(currentX)) {
                blocks.addAll(columns.get(currentX).getBlocks());
                columns.remove(currentX);
            }
        }
        return blocks;
    }
}
