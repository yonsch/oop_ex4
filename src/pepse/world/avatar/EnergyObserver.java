package pepse.world.avatar;

/**
 * Observer interface for energy update.
 */
@FunctionalInterface
public interface EnergyObserver {
    /**
     * Invoked when the avatar's energy level changes.
     * @param curEnergy the new energy value (between 0 and 100).
     */
    void uponEnergyChanged(int curEnergy);
}