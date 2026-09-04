package pepse.world.trees;

import danogl.GameObject;
import danogl.util.Vector2;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

public class Flora {
    private static final int MEASURE_UNIT = 30;
    private static final float TREE_PROBABILITY = 0.1f;
    private static final int MIN_TRUNK_HEIGHT = 4;
    private static final int MAX_TRUNK_HEIGHT_ADDITION = 6;
    private static final int SEED = 21;
    private final Function<Float, Float> groundHeightAt;

    public Flora(Function<Float, Float> groundHeightAt) {
        this.groundHeightAt = groundHeightAt;
    }

    public ArrayList<GameObject> createInRange(int minX, int maxX) {
        ArrayList<GameObject> woodland = new ArrayList<>();

        int minimalX = (int) ((minX / MEASURE_UNIT) * MEASURE_UNIT);
        int maximalX = (int) ((maxX / MEASURE_UNIT) * MEASURE_UNIT);

        for(int x = minimalX; x < maximalX; x+= MEASURE_UNIT) {
            Random rand = new Random(Objects.hash(x, SEED));

            if(rand.nextFloat() <= TREE_PROBABILITY) {
                x += MEASURE_UNIT; //to inhibit clustering
                float terrainY = groundHeightAt.apply((float) x);

                int trunkHeightAddition = rand.nextInt(MAX_TRUNK_HEIGHT_ADDITION + 1);
                int trunkHeightInBlocks = MIN_TRUNK_HEIGHT + trunkHeightAddition;

                float topLeftTrunkY = terrainY - (trunkHeightInBlocks * MEASURE_UNIT);
                Vector2 topLeftTrunk = new Vector2(x, topLeftTrunkY);

                StaticTree tree = new StaticTree(topLeftTrunk, trunkHeightInBlocks, rand);
                woodland.addAll(tree.getTree());
            }
        }
        return woodland;
    }
}
