package pepse.world.avatar;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.util.Vector2;
import pepse.PepseGameManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Represents the main playable character (Avatar) in the PEPSE simulation.
 * The Avatar responds to user inputs for horizontal movement and jumping,
 * maintains an energy resource that depletes with actions and replenishes
 */
public class Avatar extends GameObject {

    /** Height of the Avatar object */
    public static final float AVATAR_HEIGHT = 50;

    protected static final float VELOCITY_X = 400;
    protected static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 600;
    private static final Vector2 AVATAR_DIMENSIONS = new Vector2(30, AVATAR_HEIGHT);

    private static final int ENERGY_MIN = 0;
    private static final int ENERGY_MAX = 100;
    private final Function<Float, Float> groundHeightAt;
    private static final float EPSILON = 7f;

    private final UserInputListener inputListener;
    private final AvatarAnimation avatarAnimation;
    private int energy;
    private AvatarState curState;
    private GameObject currentSurface = null;

    private final List<AvatarLocationObserver> locationObservers = new ArrayList<>();
    private final List<EnergyObserver> energyObservers = new ArrayList<>();


    // ~~~~~~~~~~~~~~
    //   CONSTRUCTOR
    // ~~~~~~~~~~~~~~
    /**
     * Constructs a new Avatar instance.
     * @param topLeftCorner  the initial top-left position of the avatar in world coordinates.
     * @param inputListener  the input listener used to capture user keyboard actions.
     * @param imageReader    the image reader used to load avatar animation frames.
     * @param groundHeightAt a function returning the ground surface height (Y) for a given X coordinate.
     */
    public Avatar(Vector2 topLeftCorner,
                  UserInputListener inputListener,
                  ImageReader imageReader,
                  Function<Float, Float> groundHeightAt) {
        super(topLeftCorner, AVATAR_DIMENSIONS, null);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);

        this.inputListener = inputListener;
        this.avatarAnimation = new AvatarAnimation(imageReader);
        this.energy = ENERGY_MAX;
        this.groundHeightAt = groundHeightAt;
        this.setTag(PepseGameManager.AVATAR_TAG);

        // Initialize with the default (idle) state
        changeState(new IdleState());
    }

    // ~~~~~~~~~~~
    //   GETTERS
    // ~~~~~~~~~~~
    /**
     * Returns the player's current energy value (between 0 and 100).
     * @return the avatar's current energy.
     */
    public int getEnergy() { return energy; }

    /**
     * Returns the user input listener associated with this avatar.
     * @return the UserInputListener instance.
     */
    public UserInputListener getInputListener() { return inputListener; }

    /**
     * Returns the avatar's animation controller.
     * @return the AvatarAnimation instance.
     */
    public AvatarAnimation getAvatarAnimation() { return avatarAnimation; }

    /**
     * Checks whether the avatar is currently standing on a surface object (ground or trunk).
     * @return true if the avatar is currently on a surface, false otherwise.
     */
    public boolean isOnSurface() { return currentSurface != null; }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~
    //   CLASS FUNCTIONALITIES
    // ~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Updates the avatar's energy by adding or subtracting the specified amount,
     * ensuring the result remains clamped between ENERGY_MIN ENERGY_MAX.
     * If the energy level changes, notifies all subscribes Energy observers.
     * @param num the amount of energy to add (positive) or subtract (negative).
     */
    public void updateEnergy(int num) {
        int oldEnergy = this.energy;
        this.energy = Math.max(ENERGY_MIN, Math.min(ENERGY_MAX, this.energy + num));

        if(this.energy != oldEnergy) {
            notifyEnergyObservers();
        }
    }

    /**
     * Transitions the avatar to a new state, invoking exit logic on the old state
     * and entry logic on the new state.
     * @param newState the target AvatarState to transition into.
     */
    public void changeState(AvatarState newState) {
        if(curState != null) {
            curState.exit(this);
        }
        curState = newState;
        curState.enter(this);
    }

    /**
     * Helper method to verify whether a given game object is a surface object (trunk / terrain).
     * @param other the game object to check.
     * @return true if the object is ground surface, inner ground, or a tree trunk.
     */
    private boolean isSurfaceObj(GameObject other) {
        return other.getTag().equals(PepseGameManager.GROUND_SURFACE_TAG) ||
                // inner brick isn't technically a surface obj. however, there have been
                // cases where the avatar sank into the ground. so for lack of a better
                // name idea, this stays isSurfaceObj.
                other.getTag().equals(PepseGameManager.GROUND_INNER_TAG) ||
                other.getTag().equals(PepseGameManager.TRUNK_TAG);
    }

    /**
     * Corrects the avatar's vertical position by aligning its bottom edge with the top of the
     * surface it is currently colliding with, snapping it back to the surface.
     */
    public void snapToSurface() {
        if(currentSurface == null) { return; }

        if(currentSurface.getTag().equals(PepseGameManager.GROUND_INNER_TAG)) {
            if(groundHeightAt != null) {
                float avatarCenterX = getCenter().x();
                float surfaceTopY = groundHeightAt.apply(avatarCenterX);

                transform().setTopLeftCornerY(surfaceTopY - getDimensions().y());
                transform().setVelocityY(0);
            }
        } else if(currentSurface.getTag().equals(PepseGameManager.GROUND_SURFACE_TAG)) {
            float blockTopY = currentSurface.getTopLeftCorner().y();
            float avatarBottomY = getTopLeftCorner().y() + getDimensions().y();

            if(avatarBottomY > blockTopY && avatarBottomY - blockTopY <= EPSILON) {
                transform().setTopLeftCornerY(blockTopY - getDimensions().y());
                transform().setVelocityY(0);
            }
        }
    }

    /**
     * Clears the reference to the current surface object.
     */
    public void clearSurface() {
        this.currentSurface = null;
    }

    // ~~~~~~~~~~~~~~~~~~~~~
    //   OBSERVERS LOGIC
    // ~~~~~~~~~~~~~~~~~~~~~
    /**
     * Add a location observer
     * @param observer Location observer
     */
    public void locationSubscribe(AvatarLocationObserver observer) {
        locationObservers.add(observer);
    }

    /**
     * Registers an Energy observer to receive notifications on energy changes.
     * Automatically notifies the new observer of the current energy level.
     * @param observer the EnergyObserver to register.
     */
    public void energyObserversubscribe(EnergyObserver observer) {
        energyObservers.add(observer);
        observer.uponEnergyChanged(this.energy);
    }

    /**
     * Unregisters an Energy observer from receiving energy changes.
     * @param observer the {@link EnergyObserver} to remove.
     */
    public void unregisterEnergyObserver(EnergyObserver observer) {
        energyObservers.remove(observer);
    }

    /**
     * Notifies all registered Energy observers of the current energy level.
     */
    private void notifyEnergyObservers() {
        for(EnergyObserver observer : energyObservers) {
            observer.uponEnergyChanged(this.energy);
        }
    }

    // ~~~~~~~~~~~~~
    //   OVERRIDES
    // ~~~~~~~~~~~~~
    /**
     * Updates the avatar's state and physics for the current frame.
     * @param deltaTime the time passed since the last frame in seconds.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        AvatarState nextState = curState.tick(this);
        for (AvatarLocationObserver observer : locationObservers) {
            observer.updateAvatarLocation(this.getCenter().x());
        }
        if(nextState != null && nextState != curState) {
            changeState(nextState);
        }
    }

    /**
     * Handles collision initiation between the avatar and another game object.
     * @param other     the other GameObject involved in the collision.
     * @param collision details regarding the collision point and normal.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        currentSurface = other;

        if(isSurfaceObj(other) && getVelocity().y() > 0) {
            transform().setVelocityY(0);
        }
    }

    /**
     * Handles ongoing collision contact between the avatar and another game object.
     * @param other     the other GameObject involved in the collision.
     * @param collision details regarding the collision point and normal.
     */
    @Override
    public void onCollisionStay(GameObject other, Collision collision) {
        super.onCollisionStay(other, collision);
        currentSurface = other;
    }

    /**
     * Handles the termination of collision contact between the avatar and another game object.
     * @param other the other GameObject that was previously colliding.
     */
    @Override
    public void onCollisionExit(GameObject other) {
        super.onCollisionExit(other);
        if(other == currentSurface) {
            currentSurface = null;
        }
    }
}