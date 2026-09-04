package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.utils.ColorSupplier;
import pepse.world.wind.ObjectWeight;
import pepse.world.wind.Wind;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Generates an 8 on 8 grid of leaves.
 */
public class Treetop {
    private static final int MEASURE_UNIT = 30;
    private static final int TREETOP_EDGE = 8;
    private static final float LEAF_PROBABILITY = 0.7f;
    private final ArrayList<GameObject> leaves = new ArrayList<>();
    private Random rand;

    /**
     * Constructs a treetop canopy.
     * @param topLeft top-left coordinate of the canopy area.
     * @param rand    seeded random instance for deterministic generation.
     */
    public Treetop(Vector2 topLeft, Random rand) {
        this.rand = rand;

        float topLeftX = topLeft.x();
        float topLeftY = topLeft.y();

        for(int row = 0; row < TREETOP_EDGE; row++) {
            for(int col = 0; col < TREETOP_EDGE; col++) {
                if(rand.nextFloat() <= LEAF_PROBABILITY) {
                    float leafX = topLeftX + (col * MEASURE_UNIT);
                    float leafY = topLeftY + (row * MEASURE_UNIT);

                    GameObject leaf = createLeaf(new Vector2(leafX, leafY));
                    leaves.add(leaf);
                }
            }
        }

    }

    public ArrayList<GameObject> getLeaves() { return this.leaves; }

    private GameObject createLeaf(Vector2 position) {
        Color color = ColorSupplier.approximateColor(PepseGameManager.GREEN);
        RectangleRenderable renderable = new RectangleRenderable(color);
        Vector2 dims = Vector2.ONES.mult(MEASURE_UNIT);
        GameObject leaf = new GameObject(position, dims, renderable);
        leaf.setTag(PepseGameManager.LEAF_TAG);
        Wind.getInstance().blow(leaf, ObjectWeight.LIGHT, rand);
        return leaf;
    }

}
