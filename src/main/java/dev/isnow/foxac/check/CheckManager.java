package dev.isnow.foxac.check;

import dev.isnow.foxac.FoxAC;
import dev.isnow.foxac.check.impl.combat.aura.AuraA;
import dev.isnow.foxac.config.impl.CheckInConfig;
import dev.isnow.foxac.data.PlayerData;
import lombok.Getter;

import java.util.ArrayList;

/**
 * @author 5170
 * made on dev.isnow.foxac.check
 */

@Getter
public class CheckManager {

    public final ArrayList<Check> getExistingChecks = new ArrayList<>();

    // Not using reflections because of the obfuscation at release.
    public CheckManager() {
        getExistingChecks.add(new AuraA());

    }

    public void loadChecks(PlayerData data) {
        for(Check check : getExistingChecks) {
            CheckInConfig checkInConfig = FoxAC.getInstance().getConfiguration().getChecks().get(check.getName() + check.getType());
            if(checkInConfig.getEnabled()) {
                check.setData(data);
                data.getLoadedChecks().add(check);
            }
        }
    }
}
