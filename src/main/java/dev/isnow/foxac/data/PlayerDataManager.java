package dev.isnow.foxac.data;

import com.github.retrooper.packetevents.protocol.player.User;
import dev.isnow.foxac.FoxAC;

import java.util.HashMap;


/**
 * @author 5170
 * made on dev.isnow.foxac.data
 */

public class PlayerDataManager {

    private final HashMap<User, PlayerData> data = new HashMap<>();

    public PlayerData getData(User user) {
        return data.get(user);
    }

    public void initUser(User user) {
        data.put(user, new PlayerData(user));

        FoxAC.getInstance().getCheckManager().loadChecks(data.get(user));
    }
}
