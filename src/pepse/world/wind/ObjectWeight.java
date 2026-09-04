package pepse.world.wind;

/**
 * Represents the weight and profile of a GameObject exposed to wind.
 * Holds the base movement amplitudes, transition cycle durations, and delay bounds.
 */
public enum ObjectWeight {
    /**
     * Light elements such as leaves, which sway with larger angles and flexible widths.
     */
    LIGHT(1, 4f, 2.0f, 4f, 1.5f, 1.5f),

    /**
     * Medium-weight elements such as hanging fruits, swaying with smaller amplitudes.
     */
    MEDIUM(2, 2.5f, 2.5f, 1.5f, 2.0f, 1.5f),

    /**
     * Heavy elements such as trunks and avatars, resistant to light winds.
     */
    HEAVY(3, 1.2f, 3.0f, 0f, 1.0f, 1.0f);

    private final int weightCost;
    private final float baseAngle;
    private final float angleDuration;
    private final float baseWidthDelta;
    private final float widthDuration;
    private final float maxDelay;

    /**
     * Constructs an ObjectWeight category with specific sway attributes.
     * @param weightCost      the resistance cost required to move this object.
     * @param baseAngle       the base rotation angle amplitude in degrees.
     * @param angleDuration   the time in seconds to complete one full rotation cycle.
     * @param baseWidthDelta  the base pixel deviation for horizontal dimension squash/stretch.
     * @param widthDuration   the time in seconds to complete one full width fluctuation cycle.
     * @param maxDelay        the upper bound in seconds for the random start delay.
     */
    ObjectWeight(int weightCost, float baseAngle, float angleDuration,
                 float baseWidthDelta, float widthDuration, float maxDelay) {
        this.weightCost = weightCost;
        this.baseAngle = baseAngle;
        this.angleDuration = angleDuration;
        this.baseWidthDelta = baseWidthDelta;
        this.widthDuration = widthDuration;
        this.maxDelay = maxDelay;
    }

    /**
     * Returns the weight resistance cost.
     * @return the weight cost integer.
     */
    public int getWeightCost() { return weightCost; }

    /**
     * Returns the base rotation angle amplitude in degrees.
     * @return the base rotation angle.
     */
    public float getBaseAngle() { return baseAngle; }

    /**
     * Returns the duration in seconds of a single rotation transition cycle.
     * @return the rotation duration.
     */
    public float getAngleDuration() { return angleDuration; }

    /**
     * Returns the base width deviation in pixels for stretching and squashing.
     * @return the base width delta.
     */
    public float getBaseWidthDelta() { return baseWidthDelta; }

    /**
     * Returns the duration in seconds of a single width transition cycle.
     * @return the width duration.
     */
    public float getWidthDuration() { return widthDuration; }

    /**
     * Returns the maximum initial start delay in seconds.
     * @return the maximum delay.
     */
    public float getMaxDelay() { return maxDelay; }
}