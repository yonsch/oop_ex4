package pepse.world.trees;

import danogl.util.Vector2;
import pepse.utils.ColorSupplier;
import pepse.world.Block;

import java.awt.Color;
import java.util.Random;

/**
 * Encapsulates the visual and behavioral properties of a Fruit.
 * Constructed via its nested static Builder pattern.
 */
public class FruitProperties {

    // Base fruit properties
    public static final Color BASE_COLOR = Color.RED;
    public static final int BASE_SIZE = Block.SIZE;
    public static final int BASE_ENERGY = 20;

    // Large fruit modifications
    private static final float LARGE_SIZE_MULTIPLIER = 1.5f;
    private static final int LARGE_ENERGY_BONUS = 10;

    // Golden fruit modifications
    private static final float GOLD_BLEND_RATIO = 0.8f;
    private static final int GOLDEN_ENERGY_BONUS = 2;

    // Rotten fruit modifications
    private static final int ROTTEN_ENERGY_PENALTY = 30;

    // Instance fields of FruitProperties
    private final Color color;
    private final Vector2 dimensions;
    private final int energyGain;

    /**
     * Private constructor accepting the Builder, mirroring the classic Builder pattern.
     * @param builder the builder containing configured values.
     */
    private FruitProperties(Builder builder) {
        this.color = builder.color;
        this.dimensions = builder.dimensions;
        this.energyGain = builder.energyGain;
    }

    /**
     * Returns the color of the fruit.
     * @return Color of the fruit.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the dimensions (width, height) of the fruit.
     * @return Vector2 representing the fruit's dimensions.
     */
    public Vector2 getDimensions() {
        return dimensions;
    }

    /**
     * Returns the energy delta applied when this fruit is eaten.
     * @return integer representing the energy delta.
     */
    public int getEnergyGain() {
        return energyGain;
    }

    /**
     * Static Builder class for constructing configured FruitProperties instances.
     */
    public static class Builder {

        private Color color = BASE_COLOR;
        private Vector2 dimensions = Vector2.ONES.mult(BASE_SIZE);
        private int energyGain = BASE_ENERGY;

        /**
         * Applies the large fruit modification.
         * @param isLarge true to apply the large modification, false to skip.
         * @return this builder instance for method chaining.
         */
        public Builder setLarge(boolean isLarge) {
            if(isLarge) {
                this.dimensions = this.dimensions.mult(LARGE_SIZE_MULTIPLIER);
                this.energyGain += LARGE_ENERGY_BONUS;
            }
            return this;
        }

        /**
         * Applies the golden fruit modification.
         * @param isGolden true to apply the golden modification, false to skip.
         * @return this builder instance for method chaining.
         */
        public Builder setGolden(boolean isGolden) {
            if(isGolden) {
                this.color = ColorSupplier.blendGold(this.color, GOLD_BLEND_RATIO);
                this.energyGain *= GOLDEN_ENERGY_BONUS;
            }
            return this;
        }

        /**
         * Applies the rotten fruit modification.
         * @param isRotten true to apply the rotten modification, false to skip.
         * @return this builder instance for method chaining.
         */
        public Builder setRotten(boolean isRotten) {
            if(isRotten) {
                this.color = this.color.darker().darker().darker();
                this.energyGain -= ROTTEN_ENERGY_PENALTY;
            }
            return this;
        }

        /**
         * Randomly assigns fruit attributes based on predetermined probabilities:
         *   Large: 1/3
         *   Golden: 1/4
         *   Rotten: 1/5
         * @param rand seeded Random instance.
         * @return this builder instance for method chaining.
         */
        public Builder randomizeAttributes(Random rand) {
            setLarge(rand.nextInt(3) == 0);
            setGolden(rand.nextInt(4) == 0);
            setRotten(rand.nextInt(5) == 0);
            return this;
        }

        /**
         * Builds and returns the new FruitProperties instance.
         * @return a new FruitProperties instance configured by this builder.
         */
        public FruitProperties build() {
            return new FruitProperties(this);
        }
    }
}