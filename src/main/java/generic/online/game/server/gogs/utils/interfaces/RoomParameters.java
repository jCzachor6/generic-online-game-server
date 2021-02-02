package generic.online.game.server.gogs.utils.interfaces;

public interface RoomParameters {
    int CAPACITY_ANYONE = -1;
    int CAPACITY_PRESET_USERS = 0;

    default int capacity() {
        return CAPACITY_ANYONE;
    }
}
