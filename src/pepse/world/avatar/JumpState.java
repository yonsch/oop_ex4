package pepse.world.avatar;

import java.awt.event.KeyEvent;

/**
 * Represents the state when the avatar is in the air (jumping or falling).
 */
public class JumpState implements AvatarState {

    private final boolean jumpedFromGround;
    private boolean yetToSecondlyJump = true;

    /**
     * @param jumpedFromGround true if the avatar actively jumped from the ground,
     *                         false if the avatar walked/fell into the air.
     */
    public JumpState(boolean jumpedFromGround) {
        this.jumpedFromGround = jumpedFromGround;
    }

    @Override
    public void enter(Avatar avatar) {
        avatar.renderer().setRenderable(avatar.getAvatarAnimation().getJumpAnimation());

        if(jumpedFromGround) {
            // ome-time payment
            avatar.updateEnergy(JUMP_ENERGY_GAIN);
            avatar.transform().setVelocityY(Avatar.VELOCITY_Y);
        }
    }

    @Override
    public AvatarState tick(Avatar avatar) {
        boolean left = avatar.getInputListener().isKeyPressed(KeyEvent.VK_LEFT);
        boolean right = avatar.getInputListener().isKeyPressed(KeyEvent.VK_RIGHT);
        boolean space = avatar.getInputListener().isKeyPressed(KeyEvent.VK_SPACE);

        // MIDAIR MOVEMENT
        if(left && !right) {
            avatar.transform().setVelocityX(-Avatar.VELOCITY_X);
            avatar.getAvatarAnimation().faceLeft(avatar);
        }
        else if(!left && right) {
            avatar.transform().setVelocityX(Avatar.VELOCITY_X);
            avatar.getAvatarAnimation().faceRight(avatar);
        }
        else { avatar.transform().setVelocityX(IDLE_VELOCITY);}

        // DOUBLE JUMP LOGIC
        if(yetToSecondlyJump && space && avatar.getVelocity().y() > IDLE_VELOCITY &&
        avatar.getEnergy() >= DOUBLE_JUMP_ENERGY_DEMAND) {
            avatar.updateEnergy(DOUBLE_JUMP_ENERGY_GAIN);
            avatar.transform().setVelocityY(Avatar.VELOCITY_Y);
            yetToSecondlyJump = false;
        }

        // LANDING LOGIC
        if(avatar.getVelocity().y() == IDLE_VELOCITY) {
            if((left ^ right) && avatar.getEnergy() >= RUN_ENERGY_DEMAND) {
                return new RunState();
            }
            return new IdleState();
        }

        // DEFAULT CASE - STILL JUMPING
        return this;
    }

    @Override
    public void exit(Avatar avatar) {}
}
