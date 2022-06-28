package dev.isnow.foxac.data;

import com.github.retrooper.packetevents.protocol.player.User;

import java.util.HashMap;

public class PlayerDataManager {

    private final HashMap<User, PlayerData> data = new HashMap<>();

    public PlayerData getData(User user) {
        return data.get(user);
    }

    public void createData(User user) {
        data.put(user, new PlayerData(user));
    }
}
