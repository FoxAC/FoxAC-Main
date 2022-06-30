package dev.isnow.foxac.check.impl.combat.aura;

import dev.isnow.foxac.check.Check;
import dev.isnow.foxac.data.PlayerData;

/**
 * @author 5170
 * made on dev.isnow.foxac.check.combat.aura
 */

public class AuraA extends Check {

    public AuraA() {
        super("Aura", "A", true);
    }

    @Override
    public void process(PlayerData data) {
        if(data.getActionProcessor().isAttacking() && data.getActionProcessor().isSprinting()) {
            fail();
        }
    }
}
