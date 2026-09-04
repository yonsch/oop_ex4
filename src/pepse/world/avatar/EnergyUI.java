package pepse.world.avatar;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.util.function.Supplier;

/**
 * An element that displays the avatar's current energy percentage on screen.
 */
public class EnergyUI extends GameObject {

    private static final Vector2 TOP_LEFT_CORNER = new Vector2(20, 20);
    private static final Vector2 DIMENSIONS = new Vector2(40, 40);
    private static final String ENERGY_STRING_FORMAT = "%d%%";

    private final Supplier<Integer> energySupplier;
    private final TextRenderable textRenderable;
    private int latestEnergy = -1;

    // Clock logic to prevent the energy display from changing too often
    private static final float REFRESH_INTERVAL = 0.1f;
    private float timeSinceLastRefresh = 0f;

    /**
     * Constructs a new EnergyUI instance with a default text renderable.
     * @param energySupplier a functional callback supplying the current energy value.
     */
    public EnergyUI(Supplier<Integer> energySupplier) {
        this(energySupplier, new TextRenderable("100%"));
    }

    /**
     * Constructs a new EnergyUI instance with a custom text renderable.
     * @param energySupplier a functional callback supplying the current energy value.
     * @param textRenderable the renderable used to display text.
     */
    public EnergyUI(Supplier<Integer> energySupplier, TextRenderable textRenderable) {
        super(TOP_LEFT_CORNER, DIMENSIONS, textRenderable);
        this.energySupplier = energySupplier;
        this.textRenderable = textRenderable;
        setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        timeSinceLastRefresh += deltaTime;

        // update if enough time has passed and reset clock
        if(timeSinceLastRefresh >= REFRESH_INTERVAL) {
            // reset clock
            timeSinceLastRefresh %= REFRESH_INTERVAL;
            // update step
            int curEnergy = energySupplier.get();
            if(curEnergy != latestEnergy) {
                latestEnergy = curEnergy;
                textRenderable.setString(String.format(ENERGY_STRING_FORMAT, curEnergy));
            }
        }
    }
}