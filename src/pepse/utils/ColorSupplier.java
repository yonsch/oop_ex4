package pepse.utils;

import java.awt.*;
import java.util.Random;

/**
 * Provides procedurally-generated colors around a pivot.
 * @author Dan Nirel
 */
public final class ColorSupplier {
    private static final int DEFAULT_COLOR_DELTA = 10;
    private final static Random random = new Random();

    /**
     * Returns a color similar to baseColor, with a default delta.
     *
     * @param baseColor A color that we wish to approximate.
     * @return A color similar to baseColor.
     */
    public static Color approximateColor(Color baseColor) {
        return approximateColor(baseColor, DEFAULT_COLOR_DELTA);
    }


    /**
     * Returns a color similar to baseColor, with a difference of at most colorDelta.
     * Where the difference is equal along all channels
     *
     * @param baseColor A color that we wish to approximate.
     * @param colorDelta The maximal difference (per channel) between the sampled color and the base color.
     * @return A color similar to baseColor.
     */
    public static Color approximateMonoColor(Color baseColor, int colorDelta){
        int channel = randomChannelInRange(baseColor.getRed()-colorDelta, baseColor.getRed()+colorDelta);
        return new Color(channel, channel, channel);
    }



    /**
     * Returns a color similar to baseColor, with a default delta.
     * Where the difference is equal along all channels
     *
     * @param baseColor A color that we wish to approximate.
     * @return A color similar to baseColor.
     */
    public static Color approximateMonoColor(Color baseColor) {
        return approximateMonoColor(baseColor, DEFAULT_COLOR_DELTA);
    }


    /**
     * Returns a color similar to baseColor, with a difference of at most colorDelta.
     *
     * @param baseColor A color that we wish to approximate.
     * @param colorDelta The maximal difference (per channel) between the sampled color and the base color.
     * @return A color similar to baseColor.
     */
    public static Color approximateColor(Color baseColor, int colorDelta) {

        return new Color(
                randomChannelInRange(baseColor.getRed()-colorDelta, baseColor.getRed()+colorDelta),
                randomChannelInRange(baseColor.getGreen()-colorDelta, baseColor.getGreen()+colorDelta),
                randomChannelInRange(baseColor.getBlue()-colorDelta, baseColor.getBlue()+colorDelta));
    }

    /**
     * This method generates a random value for a color channel within the given range [min, max].
     *
     * @param min The lower bound of the given range.
     * @param max The upper bound of the given range.
     * @return A random number in the range [min, max], clipped to [0,255].
     */
    private static int randomChannelInRange(int min, int max) {
        int channel = random.nextInt(max-min+1) + min;
        return Math.min(255, Math.max(channel, 0));
    }

    // Classic metallic gold RGB: (255, 215, 0)
    private static final Color GOLD = new Color(255, 215, 0);

    /**
     * Blends the base color with Gold.
     *
     * @param base The original Color.
     * @param ratio Blend strength from 0.0 (pure base) to 1.0 (pure gold).
     */
    public static Color blendGold(Color base, float ratio) {
        ratio = Math.max(0.0f, Math.min(1.0f, ratio)); // Clamp between 0 and 1

        int r = (int) (base.getRed()   * (1 - ratio) + GOLD.getRed()   * ratio);
        int g = (int) (base.getGreen() * (1 - ratio) + GOLD.getGreen() * ratio);
        int b = (int) (base.getBlue()  * (1 - ratio) + GOLD.getBlue()  * ratio);

        return new Color(r, g, b, base.getAlpha());
    }
}
