package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.wind.ObjectWeight;
import pepse.world.wind.Wind;

import java.util.Random;

public class TreeTrunk extends GameObject {

    public TreeTrunk(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        this.setTag(PepseGameManager.TRUNK_TAG);

        Wind.getInstance().blow(this, ObjectWeight.HEAVY, new Random());

        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
}
