package pepse.world.wind;

/**
 * Represents the wind strengths available in the simulation.
 * Stronger wind forces allow heavier objects to sway.
 */
public enum WindStrength {
    /**
     * Gentle breeze; only moves light objects such as leaves.
     */
    LIGHT(1),

    /**
     * Moderate wind; causes leaves to sway more and fruits to sway gently.
     */
    MODERATE(2),

    /**
     * Strong wind; causes movement across all objects.
     */
    STRONG(3);

    private final int power;

    /**
     * Constructs a WindStrength constant with an assigned power magnitude.
     * @param power the integer power level representing wind strength.
     */
    WindStrength(int power) { this.power = power; }

    /**
     * Returns the integer power level of this wind strength.
     * @return the power level.
     */
    public int getPower() { return this.power; }
}