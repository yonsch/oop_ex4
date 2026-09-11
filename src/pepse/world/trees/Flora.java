package pepse.world.trees;

import danogl.GameObject;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Responsible for procedurally generating vegetation trees in the game world.
 * This class creates trees across a specified horizontal range based on deterministic
 * random seeds tied to X coordinates. It utilizes functional callbacks to query terrain height.
 */
public class Flora {
    private static final int MEASURE_UNIT = Block.SIZE;
    private static final float TREE_PROBABILITY = 0.1f;
    private static final int MIN_TRUNK_HEIGHT = 4;
    private static final int MAX_TRUNK_HEIGHT_ADDITION = 6;
    private static final int SEED = 21;

    private final Function<Float, Float> groundHeightAt;
    private final Consumer<Integer> energyCallback;
    private final Map<Integer, StaticTree> trees = new HashMap<>();

    /**
     * Constructs a new Flora instance.
     * @param groundHeightAt a function mapping an X coordinate to the corresponding terrain ground
     *                      height (Y).
     * @param energyCallback a callback invoked with an integer amount to modify the avatar's
     *                       energy when a fruit is eaten.
     */
    public Flora(Function<Float, Float> groundHeightAt, Consumer<Integer> energyCallback) {
        this.groundHeightAt = groundHeightAt;
        this.energyCallback = energyCallback;
    }

    /**
     * Generates all tree objects within the specified horizontal range.
     * @param minX the starting X coordinate of the generation range.
     * @param maxX the ending X coordinate of the generation range.
     * @return an ArrayList containing all the generated GameObject instances within the range.
     */
    public ArrayList<GameObject> createInRange(int minX, int maxX) {
        ArrayList<GameObject> woodland = new ArrayList<>();

        int minimalX = (minX / MEASURE_UNIT) * MEASURE_UNIT;
        int maximalX = (maxX / MEASURE_UNIT) * MEASURE_UNIT;

        for(int x = minimalX; x < maximalX; x+= MEASURE_UNIT) {
            Random rand = new Random(Objects.hash(x, SEED));

            if(rand.nextFloat() <= TREE_PROBABILITY) {
                x += MEASURE_UNIT; //to inhibit clustering
                float terrainY = groundHeightAt.apply((float) x);

                int trunkHeightAddition = rand.nextInt(MAX_TRUNK_HEIGHT_ADDITION + 1);
                int trunkHeightInBlocks = MIN_TRUNK_HEIGHT + trunkHeightAddition;

                float topLeftTrunkY = terrainY - (trunkHeightInBlocks * MEASURE_UNIT);
                Vector2 topLeftTrunk = new Vector2(x, topLeftTrunkY);

                StaticTree tree = new StaticTree(
                        topLeftTrunk, trunkHeightInBlocks, rand, energyCallback
                );
                woodland.addAll(tree.getTree());
                trees.put(x, tree);
            }
        }
        return woodland;
    }

    /**
     * Remove flora within a range. Returned objects need to be removed from the engine's list.
     * @param minX Left boundary of the range
     * @param maxX Right boundary of the range
     * @return A list of all game objects to remove from the engine
     */
    public List<GameObject> removeInRange(int minX, int maxX) {
        ArrayList<GameObject> items = new ArrayList<>();

        int minimalX = (minX / MEASURE_UNIT) * MEASURE_UNIT;
        int maximalX = (maxX / MEASURE_UNIT) * MEASURE_UNIT;

        for(int x = minimalX; x < maximalX; x+= MEASURE_UNIT) {
            if(trees.containsKey(x)) {
                items.addAll(trees.get(x).getTree());
                trees.remove(x);
            }
        }
        return items;
    }
}
