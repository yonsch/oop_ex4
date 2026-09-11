package pepse.world.avatar;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

/**
 * A display of the avatar's current energy percentage.
 * Implements EnergyObserver interface so it is notified directly whenever energy changes.
 */
public class EnergyUI extends GameObject implements EnergyObserver {

    private static final Vector2 TOP_LEFT_CORNER = new Vector2(20, 20);
    private static final Vector2 DIMENSIONS = new Vector2(40, 40);
    private static final String ENERGY_STRING_FORMAT = "%d%%";

    private final TextRenderable textRenderable;

    /**
     * Constructs a new EnergyUI instance initialized to 100%.
     */
    public EnergyUI() {
        this(new TextRenderable("100%"));
    }

    /**
     * Constructs a new EnergyUI instance with a custom TextRenderable (clumped between 0 and 100).
     * @param textRenderable the renderable used to display text.
     */
    public EnergyUI(TextRenderable textRenderable) {
        super(TOP_LEFT_CORNER, DIMENSIONS, textRenderable);
        this.textRenderable = textRenderable;
        setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
    }

    /**
     * Updates the text display when notified of an energy change.
     * @param currentEnergy the new energy percentage.
     */
    @Override
    public void uponEnergyChanged(int currentEnergy) {
        textRenderable.setString(String.format(ENERGY_STRING_FORMAT, currentEnergy));
    }
}