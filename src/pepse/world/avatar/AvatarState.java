package pepse.world.avatar;

public interface AvatarState {

    int IDLE_VELOCITY = 0;

    int IDLE_ENERGY_GAIN = 1;
    int RUN_ENERGY_GAIN = -2;
    int JUMP_ENERGY_GAIN = -20;
    int DOUBLE_JUMP_ENERGY_GAIN = -50;

    int RUN_ENERGY_DEMAND = 2;
    int JUMP_ENERGY_DEMAND = 20;
    int DOUBLE_JUMP_ENERGY_DEMAND = 50;

    void enter(Avatar avatar);
    AvatarState tick(Avatar avatar);
    void exit(Avatar avatar);
}
