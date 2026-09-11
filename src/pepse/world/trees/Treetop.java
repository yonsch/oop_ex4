package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.utils.ColorSupplier;
import pepse.world.Block;
import pepse.world.wind.ObjectWeight;
import pepse.world.wind.Wind;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Generates an 8 on 8 grid of leaves.
 */
public class Treetop {

    private static final Color GREEN = new Color(50, 200, 30);
    private static final int MEASURE_UNIT = Block.SIZE;
    private static final int TREETOP_EDGE = 8;
    private static final float LEAF_PROBABILITY = 0.7f;
    private final ArrayList<GameObject> leavesAndFruits = new ArrayList<>();
    private Random rand;
    private static final float FRUIT_PROBABILITY = 0.1f;

    /**
     * Constructs a treetop canopy.
     * @param topLeft        top-left coordinate of the canopy area.
     * @param rand           seeded random instance for deterministic generation.
     * @param energyCallback callback invoked when a fruit is eaten.
     */
    public Treetop(Vector2 topLeft, Random rand, Consumer<Integer> energyCallback) {
        this.rand = rand;

        float topLeftX = topLeft.x();
        float topLeftY = topLeft.y();

        for (int row = 0; row < TREETOP_EDGE; row++) {
            for (int col = 0; col < TREETOP_EDGE; col++) {
                float x = topLeftX + (col * MEASURE_UNIT);
                float y = topLeftY + (row * MEASURE_UNIT);
                addLeavesLogic(x, y);
                addFlowersLogic(x, y, energyCallback);
            }
        }

    }

    /**
     * Returns the treetop which is consisted of leaves and fruits.
     * @return ArrayList contating the treetop objects.
     */
    public ArrayList<GameObject> getLeaves() {
        return this.leavesAndFruits;
    }

    private GameObject createLeaf(Vector2 position) {
        Color color = ColorSupplier.approximateColor(GREEN);
        RectangleRenderable renderable = new RectangleRenderable(color);
        Vector2 dims = Vector2.ONES.mult(MEASURE_UNIT);
        GameObject leaf = new GameObject(position, dims, renderable);
        leaf.setTag(PepseGameManager.LEAF_TAG);
        Wind.getInstance().blow(leaf, ObjectWeight.LIGHT, rand);
        return leaf;
    }

    private void addLeavesLogic(float x, float y) {
        if(rand.nextFloat() <= LEAF_PROBABILITY) {
            GameObject leaf = createLeaf(new Vector2(x, y));
            leavesAndFruits.add(leaf);
        }
    }

    private void addFlowersLogic(float x, float y, Consumer<Integer> energyCallback) {
        if(rand.nextFloat() <= FRUIT_PROBABILITY) {
            FruitProperties props = new FruitProperties.Builder()
                    .randomizeAttributes(rand)
                    .build();

            Fruit fruit = new Fruit(new Vector2(x, y), props, energyCallback, rand);
            leavesAndFruits.add(fruit);
        }
    }
}
