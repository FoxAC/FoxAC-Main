package dev.isnow.foxac.check;

import dev.isnow.foxac.FoxAC;
import dev.isnow.foxac.config.impl.CheckInConfig;
import dev.isnow.foxac.data.PlayerData;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 5170
 * made on dev.isnow.foxac.check
 */

@Getter
public class CheckManager {

    private PlayerData data;
    private final ArrayList<Check> loadedChecks = new ArrayList<>();

    // Not using reflections because of the obfuscation at release.
    public CheckManager(PlayerData data) {
        this.data = data;
        loadChecks();

    }

    public void loadChecks() {

        List<Check> checks = new ArrayList<>();

        for (Check check : checks) {
            CheckInConfig checkInConfig = FoxAC.getInstance().getConfiguration().getChecks().get(check.getName() + check.getType());
            if (checkInConfig.getEnabled()) {
                loadedChecks.add(check);

            }
        }


    }
}
