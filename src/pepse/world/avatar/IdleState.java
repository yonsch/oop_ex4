package pepse.world.avatar;

import java.awt.event.KeyEvent;

/**
 * Represents the state when the avatar is staying put.
 */
public class IdleState implements AvatarState {

    @Override
    public void enter(Avatar avatar) {
        avatar.renderer().setRenderable(avatar.getAvatarAnimation().getIdleAnimation());
        avatar.transform().setVelocityX(IDLE_VELOCITY);
    }

    @Override
    public AvatarState tick(Avatar avatar) {
        avatar.updateEnergy(IDLE_ENERGY_GAIN);
        boolean left = avatar.getInputListener().isKeyPressed(KeyEvent.VK_LEFT);
        boolean right = avatar.getInputListener().isKeyPressed(KeyEvent.VK_RIGHT);
        boolean space = avatar.getInputListener().isKeyPressed(KeyEvent.VK_SPACE);

        // Air Case:
        // passively in the air (like falling off a cliff)
        if(avatar.getVelocity().y() != 0) {
            return new JumpState(false);
        }

        // Ground Cases:
        // case 1: jump is initiated
        if(space && avatar.getEnergy() >= JUMP_ENERGY_DEMAND) {
            return new JumpState(true);
        }

        // case 2: run is initiated
        // ^ - return true iff only one of left or right is true
        if((left ^ right) && avatar.getEnergy() >= RUN_ENERGY_DEMAND) {
            return new RunState();
        }

        // default: return cur idle state
        return this;
    }

    @Override
    public void exit(Avatar avatar) {}
}
