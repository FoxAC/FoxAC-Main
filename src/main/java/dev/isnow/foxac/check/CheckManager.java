package dev.isnow.foxac.check;

import com.github.retrooper.packetevents.protocol.player.User;
import dev.isnow.foxac.FoxAC;
import dev.isnow.foxac.check.impl.combat.aura.AuraA;
import dev.isnow.foxac.config.impl.CheckInConfig;
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

    public void loadChecks(User user) {
        for(Check check : getExistingChecks) {
            CheckInConfig checkInConfig = FoxAC.getInstance().getConfiguration().getChecks().get(check.getName() + check.getType());
            if(checkInConfig.getEnabled()) {
                FoxAC.getInstance().getDataManager().getData(user).getLoadedChecks().add(check);
            }
        }
    }
}
