package pepse.world.avatar;

import danogl.gui.ImageReader;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;

/**
 * Manages the animations for the Avatar class for the different states.
 */
public class AvatarAnimation {
    private static final double TIME_PER_FRAME = 0.2;

    private static final String[] IDLE_IMAGE_PATHS = {
            "assets/idle_0.png", "assets/idle_1.png",
            "assets/idle_2.png", "assets/idle_3.png"
    };

    private static final String[] JUMP_IMAGE_PATHS = {
            "assets/jump_0.png", "assets/jump_1.png",
            "assets/jump_2.png", "assets/jump_3.png"
    };

    private static final String[] RUN_IMAGE_PATHS = {
            "assets/run_0.png", "assets/run_1.png",
            "assets/run_2.png", "assets/run_3.png",
            "assets/run_4.png", "assets/run_5.png"
    };

    private final AnimationRenderable idleAnimation;
    private final AnimationRenderable jumpAnimation;
    private final AnimationRenderable runAnimation;

    /**
     * Initializes the avatar animation renderables.
     * @param imageReader used to read image paths.
     */
    public AvatarAnimation(ImageReader imageReader) {
        this.idleAnimation = new AnimationRenderable(
                IDLE_IMAGE_PATHS, imageReader, true, TIME_PER_FRAME
        );
        this.jumpAnimation = new AnimationRenderable(
                JUMP_IMAGE_PATHS, imageReader, true, TIME_PER_FRAME
        );
        this.runAnimation = new AnimationRenderable(
                RUN_IMAGE_PATHS, imageReader, true, TIME_PER_FRAME
        );
    }

    /**
     * Returns the idle animation.
     */
    public Renderable getIdleAnimation() {
        return idleAnimation;
    }

    /**
     * Returns the jump animation.
     */
    public Renderable getJumpAnimation() {
        return jumpAnimation;
    }

    /**
     * Returns the run animation.
     */
    public Renderable getRunAnimation() {
        return runAnimation;
    }

    /**
     * Sets the avatar to face left.
     * @param avatar the avatar to orient.
     */
    public void faceLeft(Avatar avatar) {
        avatar.renderer().setIsFlippedHorizontally(true);
    }

    /**
     * Sets the avatar to face right.
     * @param avatar the avatar to orient.
     */
    public void faceRight(Avatar avatar) {
        avatar.renderer().setIsFlippedHorizontally(false);
    }
}