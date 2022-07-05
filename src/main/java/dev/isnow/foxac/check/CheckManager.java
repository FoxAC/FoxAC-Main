package dev.isnow.foxac.check;

import dev.isnow.foxac.FoxAC;
import dev.isnow.foxac.check.impl.combat.aura.AuraA;
import dev.isnow.foxac.config.impl.CheckInConfig;
import dev.isnow.foxac.data.PlayerData;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        checks.add(new AuraA());

        for (Check check : checks) {
            CheckInConfig checkInConfig = FoxAC.getInstance().getConfiguration().getChecks().get(check.getName().toLowerCase(Locale.ROOT) + check.getType().toLowerCase(Locale.ROOT));
            if (checkInConfig.getEnabled()) {
                Bukkit.broadcastMessage("Check " + check.getName() + check.getType() + " Loaded!");
                check.setData(data);
                loadedChecks.add(check);
            }
        }


    }
}
