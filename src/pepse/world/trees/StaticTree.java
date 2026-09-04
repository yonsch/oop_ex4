package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Combines a trunk and treetop into a static tree.
 */
public class StaticTree {

    private static final int MEASURE_UNIT = 30;
    private static final int TREE_EDGE = 8 * MEASURE_UNIT;
    private static final int TREE_RADIUS = TREE_EDGE / 2;
    private final ArrayList<GameObject> treeObjects = new ArrayList<>();

    public StaticTree(Vector2 topTrunkLeftCorner, int trunkHeight, Random rand) {

        // TRUNK CONSTRUCTION
        Color trunkColor = PepseGameManager.BROWN;
        Vector2 trunkDims = new Vector2(MEASURE_UNIT, MEASURE_UNIT * trunkHeight);
        RectangleRenderable trunkRenderable = new RectangleRenderable(trunkColor);
        TreeTrunk treeTrunk = new TreeTrunk(topTrunkLeftCorner, trunkDims, trunkRenderable);

        // TREETOP CONSTRUCTION
        int treetopX = (int) (topTrunkLeftCorner.x() - TREE_RADIUS + (MEASURE_UNIT / 2f));
        int treetopY = (int) (topTrunkLeftCorner.y() - (TREE_RADIUS * 1.5)); // so short trees tops won't overlap completely with the trunk
        Vector2 topTreetop = new Vector2(treetopX, treetopY);
        Treetop treetop = new Treetop(topTreetop, rand);
        ArrayList<GameObject> leaves = treetop.getLeaves();

        // ADDITION TO TREE OBJECTS
        treeObjects.add(treeTrunk);
        treeObjects.addAll(leaves);
    }

    public ArrayList<GameObject> getTree() { return this.treeObjects; }

}
