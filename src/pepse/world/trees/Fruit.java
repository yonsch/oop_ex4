package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.wind.ObjectWeight;
import pepse.world.wind.Wind;

import java.util.Random;
import java.util.function.Consumer;

/**
 * Represents a Fruit. When eaten by the avatar,
 * provides/deducts energy and respawns after a day cycle.
 */
public class Fruit extends GameObject {
    private static final float RESPAWN_DELAY_SECONDS = PepseGameManager.DAY_CYCLE;

    private final FruitProperties properties;
    private final Consumer<Integer> energyConsumer;
    private boolean isEaten = false;

    /**
     * Constructs a new Fruit instance.
     * @param topLeftCorner  position of the fruit.
     * @param properties     the configured properties of this fruit.
     * @param energyConsumer callback invoked when the fruit is consumed to modify avatar energy.
     * @param rand           random generator for initial wind delay.
     */
    public Fruit(Vector2 topLeftCorner,
                 FruitProperties properties,
                 Consumer<Integer> energyConsumer,
                 Random rand) {
        super(topLeftCorner, properties.getDimensions(), new OvalRenderable(properties.getColor()));
        this.properties = properties;
        this.energyConsumer = energyConsumer;
        this.setTag(PepseGameManager.FRUIT_TAG);

        Wind.getInstance().blow(this, ObjectWeight.MEDIUM, rand);
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return !isEaten && super.shouldCollideWith(other);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        // there is no collision if the fruit had already been eaten this cycle
        if(!isEaten && other.getTag().equals(PepseGameManager.AVATAR_TAG)) {
            eat();
        }
    }

    /**
     * Consumes the fruit, granting energy and scheduling its respawn.
     */
    private void eat() {
        this.isEaten = true;
        this.renderer().setOpaqueness(0f); // make the fruit invisible for this cycle

        if(energyConsumer != null) {
            energyConsumer.accept(properties.getEnergyGain());
        }
        new ScheduledTask(
                this,
                RESPAWN_DELAY_SECONDS,
                false,
                this::respawn
        );
    }

    /**
     * Respawns the fruit at its original location with its original appearance.
     */
    private void respawn() {
        this.isEaten = false;
        this.renderer().setOpaqueness(1f);
    }

    public FruitProperties getProperties() {
        return properties;
    }
}