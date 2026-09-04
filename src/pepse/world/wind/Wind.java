package pepse.world.wind;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.util.Vector2;

import java.util.Random;

/**
 * Singleton class that manages the wind system across the simulation.
 * Applies scheduled, oscillating rotation and dimension transitions to GameObjects
 * based on the wind strength and object's weight.
 */
public class Wind {
    private static final WindStrength DEFAULT_WIND_STRENGTH = WindStrength.STRONG;

    // The single instance of Wind
    private static final Wind INSTANCE = new Wind();

    private WindStrength currentWindStrength;

    /**
     * Private constructor.
     */
    private Wind() { this.currentWindStrength = DEFAULT_WIND_STRENGTH; }

    /**
     * Returns the singleton instance of the Wind manager.
     *
     * @return the Wind instance.
     */
    public static Wind getInstance() {
        return INSTANCE;
    }

    /**
     * Updates the global wind strength.
     * @param strength the new wind strength to apply across the simulation.
     */
    public void setWindStrength(WindStrength strength) { this.currentWindStrength = strength; }

    /**
     * Returns the current wind strength.
     * @return the current wind strength.
     */
    public WindStrength getWindStrength() { return this.currentWindStrength; }

    /**
     * Applies wind sway transitions to a target GameObject.
     * @param target the GameObject to blow in the wind.
     * @param weight the weight classification of the target object.
     * @param rand   random number generator used for calculating staggered initial delays.
     */
    public void blow(GameObject target, ObjectWeight weight, Random rand) {
        blow(target, this.currentWindStrength, weight, rand);
    }

    /**
     * Applies wind sway transitions to a target GameObject and object weight resistance threshold.
     * @param target   the GameObject to apply wind effects to.
     * @param strength the specific wind strength to apply.
     * @param weight   the weight classification of the target object.
     * @param rand     random number generator used for calculating initial delays.
     */
    public void blow(GameObject target, WindStrength strength,
                     ObjectWeight weight, Random rand) {
        int netPower = strength.getPower() - weight.getWeightCost() + 1;

        // return if the object is too heavy for the wind to sway
        if (netPower <= 0) { return; }

        float maxAngle = weight.getBaseAngle() * netPower;
        float widthDelta = weight.getBaseWidthDelta() * netPower;

        float angleDelay = rand.nextFloat() * weight.getMaxDelay();
        float dimDelay = rand.nextFloat() * weight.getMaxDelay();

        // Rotation sway
        new ScheduledTask(target, angleDelay, false,
                () -> new Transition<Float>(
                        target,
                        target.renderer()::setRenderableAngle,
                        -maxAngle,
                        maxAngle,
                        Transition.LINEAR_INTERPOLATOR_FLOAT,
                        weight.getAngleDuration(),
                        Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                        null
                )
        );

        // Dimension sway
        if (widthDelta > 0) {
            Vector2 initialDims = target.getDimensions();
            Vector2 minDims = new Vector2(initialDims.x() - widthDelta, initialDims.y());
            Vector2 maxDims = new Vector2(initialDims.x() + widthDelta, initialDims.y());

            new ScheduledTask(target, dimDelay, false,
                    () -> new Transition<Vector2>(
                            target,
                            target::setDimensions,
                            minDims,
                            maxDims,
                            Transition.LINEAR_INTERPOLATOR_VECTOR,
                            weight.getWidthDuration(),
                            Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                            null
                    )
            );
        }
    }
}